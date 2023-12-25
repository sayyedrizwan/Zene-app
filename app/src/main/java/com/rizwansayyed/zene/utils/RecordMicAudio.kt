package com.rizwansayyed.zene.utils

import android.media.MediaRecorder
import android.os.Build
import com.rizwansayyed.zene.data.utils.CacheFiles.recordedMusicRecognitionFile
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RecordMicAudio {

    private lateinit var mediaRecorder: MediaRecorder

    init {
        CoroutineScope(Dispatchers.IO).launch {
            mediaRecorder =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(context) else MediaRecorder()
            mediaRecorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC)
            }
        }
    }

    suspend fun startRecordingMusicSearch() = withContext(Dispatchers.IO) {
        val mediaRecorder =
            (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) MediaRecorder(context) else MediaRecorder()).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC)
            }

        recordedMusicRecognitionFile.deleteRecursively()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            mediaRecorder.setOutputFile(recordedMusicRecognitionFile)
        else
            mediaRecorder.setOutputFile(recordedMusicRecognitionFile.path)

        try {
            mediaRecorder.prepare()
            mediaRecorder.start()
        } catch (e: Exception) {
            e.message
        }
    }

    suspend fun stopRecording() = withContext(Dispatchers.IO) {
        try {
            mediaRecorder.stop()
            mediaRecorder.release()
        } catch (e: Exception) {
            e.message
        }
    }


}