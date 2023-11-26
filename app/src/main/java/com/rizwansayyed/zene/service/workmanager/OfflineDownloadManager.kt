package com.rizwansayyed.zene.service.workmanager

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.downloadAFileFromURL
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File

@HiltWorker
class OfflineDownloadManager @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val songDownloader: SongDownloaderInterface
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val songId =
                inputData.getString(Intent.EXTRA_TEXT) ?: return@withContext Result.success()
            val file = File(songDownloadPath, "$songId.mp3")

            try {
                val hdSong =
                    songDownloader.download(songId).first() ?: return@withContext Result.failure()
                val fileSize = downloadAFileFromURL(hdSong, file) {
                    Log.d("TAG", "doWork: data size percentage $it")
                }

                Log.d("TAG", "doWork: data size $fileSize")
                return@withContext Result.success()
            } catch (e: Exception) {
                return@withContext Result.failure()
            }
        }
    }


    companion object {

        private val songDownloadPath = File(context.filesDir, "download_songs").apply {
            mkdirs()
        }

        fun startOfflineDownloadWorkManager(id: String) {
            val builder = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)

            val data = Data.Builder().apply {
                putString(Intent.EXTRA_TEXT, id)
            }
            val uploadWorkRequest =
                OneTimeWorkRequestBuilder<OfflineDownloadManager>().setInputData(data.build())
                    .setInputData(data.build()).setConstraints(builder.build()).build()
            WorkManager.getInstance(context).enqueue(uploadWorkRequest)
        }
    }

}