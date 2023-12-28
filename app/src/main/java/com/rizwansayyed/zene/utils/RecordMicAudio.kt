package com.rizwansayyed.zene.utils

import android.media.MediaRecorder
import android.os.Build
import com.rizwansayyed.zene.data.utils.CacheFiles.recordedMusicRecognitionFile
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.Utils.printStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds


class RecordMicAudio {

    companion object {
        private var mediaRecorderMic: MediaRecorder? = null

    }

    private fun makeMediaRecorder(): MediaRecorder {
      return  (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(context) else MediaRecorder()).apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioSamplingRate(16000)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                setOutputFile(recordedMusicRecognitionFile)
            else
                setOutputFile(recordedMusicRecognitionFile.path)
        }
    }

    suspend fun startRecordingMusicSearch() = withContext(Dispatchers.IO) {
        stopRecording()

        mediaRecorderMic = makeMediaRecorder()

        recordedMusicRecognitionFile.deleteRecursively()
        try {
            mediaRecorderMic?.prepare()
            mediaRecorderMic?.start()
        } catch (e: Exception) {
            e.printStack()
        }
    }

    private suspend fun stopRecording() = withContext(Dispatchers.IO) {
        try {
            mediaRecorderMic?.pause()
            mediaRecorderMic?.stop()
            mediaRecorderMic?.reset()
        } catch (e: Exception) {
            e.printStack()
        }

        delay(1.seconds)
    }
}