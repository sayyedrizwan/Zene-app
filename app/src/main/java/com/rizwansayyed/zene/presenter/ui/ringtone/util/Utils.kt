package com.rizwansayyed.zene.presenter.ui.ringtone.util


import android.content.ContentValues
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import android.provider.Settings
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.utils.CacheFiles.cropRingtoneInDevice
import com.rizwansayyed.zene.data.utils.CacheFiles.demoCropRingtonePath
import com.rizwansayyed.zene.data.utils.CacheFiles.demoRingtonePath
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.Utils.copyFileTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import kotlin.time.Duration.Companion.seconds


object Utils {
    private val ringtonePlayer = ExoPlayer.Builder(context).build()

    fun startPlayingRingtoneSong() = CoroutineScope(Dispatchers.Main).launch {
        val mediaItem = MediaItem.fromUri(demoRingtonePath.toUri())
        ringtonePlayer.setMediaItem(mediaItem)
        ringtonePlayer.prepare()
        ringtonePlayer.repeatMode = Player.REPEAT_MODE_ONE
        ringtonePlayer.playWhenReady = true
        ringtonePlayer.play()


        if (isActive) cancel()
    }

    fun pauseRingtoneSong() = CoroutineScope(Dispatchers.Main).launch {
        ringtonePlayer.pause()


        if (isActive) cancel()
    }

    fun playOrPauseRingtoneSong() = CoroutineScope(Dispatchers.Main).launch {
        if (ringtonePlayer.isPlaying) ringtonePlayer.pause()
        else ringtonePlayer.play()


        if (isActive) cancel()
    }

    fun setPlayerDurationDependOnSlider(ringtoneSlider: ClosedFloatingPointRange<Float>) {
        val start = (ringtoneSlider.start * ringtonePlayer.duration) / 100
        CoroutineScope(Dispatchers.Main).launch {
            ringtonePlayer.seekTo(start.toLong())

            if (isActive) cancel()
        }
    }

    fun ifSongGoingOutOfSlider(ringtoneSlider: ClosedFloatingPointRange<Float>) {
        val start = (ringtoneSlider.start * ringtonePlayer.duration) / 100
        val end = (ringtoneSlider.endInclusive * ringtonePlayer.duration) / 100
        CoroutineScope(Dispatchers.Main).launch {
            if (ringtonePlayer.currentPosition > end) {
                ringtonePlayer.seekTo(start.toLong())
            }
            if (isActive) cancel()
        }
    }


    fun progressRingtoneSong() = ringtonePlayer.currentPosition.toFloat() / ringtonePlayer.duration
    fun isRingtoneSongPlaying() = ringtonePlayer.isPlaying


    fun writeRingtoneSettings(): Boolean {
        return if (!Settings.System.canWrite(context)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + context.packageName)
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            context.resources.getString(R.string.need_permission_to_set_the_ringtone).toast()
            false
        } else
            true

    }

    fun startCroppingAndMakeRingtone(ringtoneSlider: ClosedFloatingPointRange<Float>) =
        CoroutineScope(Dispatchers.IO).launch {
            demoCropRingtonePath.deleteRecursively()
            cropRingtoneInDevice.deleteRecursively()
            val start =
                withContext(Dispatchers.Main) { (ringtoneSlider.start * ringtonePlayer.duration) / 100 }
            val end =
                withContext(Dispatchers.Main) { (ringtoneSlider.endInclusive * ringtonePlayer.duration) / 100 }

            val command = arrayOf(
                "-i", demoRingtonePath.path,
                "-map", "0:a:0",
                "-acodec", "mp3",
                "-b:a", "192k",
                "-ar", "44100",
                "-ss", (start / 1000).toInt().toString(),
                "-to", (end / 1000).toInt().toString(),
                demoCropRingtonePath.path
            )

            val session = FFmpegKit.execute(command.joinToString(" "))

            if (ReturnCode.isSuccess(session.returnCode)) {
                demoCropRingtonePath.copyFileTo(cropRingtoneInDevice)
                setRingtoneFromFile(cropRingtoneInDevice)

                delay(2.seconds)
                cropRingtoneInDevice.deleteRecursively()
            } else
                context.resources.getString(R.string.error_setting_ringtone).toast()
        }

    private fun setRingtoneFromFile(path: File) {
        val values = ContentValues()
        values.put(MediaStore.MediaColumns.TITLE, path.name)
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3")
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true)
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val newUri = context.contentResolver
                .insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
            try {
                context.contentResolver.openOutputStream(newUri!!).use { os ->
                    val size = path.length().toInt()
                    val bytes = ByteArray(size)
                    try {
                        val buf = BufferedInputStream(FileInputStream(path))
                        buf.read(bytes, 0, bytes.size)
                        buf.close()
                        os?.write(bytes)
                        os?.close()
                        os?.flush()
                    } catch (e: Exception) {
                        context.resources.getString(R.string.error_setting_ringtone).toast()
                    }
                }
            } catch (e: Exception) {
                context.resources.getString(R.string.error_setting_ringtone).toast()
            }
            RingtoneManager
                .setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri)
        } else {
            values.put(MediaStore.MediaColumns.DATA, path.absolutePath)
            val uri = MediaStore.Audio.Media.getContentUriForPath(path.absolutePath)
            context.contentResolver.delete(
                uri!!,
                MediaStore.MediaColumns.DATA + "=\"" + path.absolutePath + "\"",
                null
            )
            val newUri = context.contentResolver.insert(uri, values)
            RingtoneManager
                .setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri)
            context.contentResolver.insert(
                MediaStore.Audio.Media.getContentUriForPath(path.absolutePath)!!, values
            )
        }
    }
}