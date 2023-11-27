package com.rizwansayyed.zene.service.workmanager

import android.content.Context
import android.content.Intent
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.downloadAFileFromURL
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@HiltWorker
class OfflineDownloadManager @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val songDownloader: SongDownloaderInterface,
    private val roomDb: RoomDBInterface
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val songId =
                inputData.getString(Intent.EXTRA_TEXT) ?: return@withContext Result.success()
            val file = File(songDownloadPath, "$songId.mp3")
            val info = roomDb.offlineSongInfo(songId).first()

            try {
                val hdSong =
                    songDownloader.download(songId).first() ?: return@withContext Result.failure()
                val download = downloadAFileFromURL(hdSong, file) {
                    info.progress = it
                    CoroutineScope(Dispatchers.IO).launch {
                        roomDb.insert(info).collect()
                        if (isActive) cancel()
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    info.progress = if (download == true) 100 else 0
                    roomDb.insert(info).collect()
                    if (isActive) cancel()
                }
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

        fun startOfflineDownloadWorkManager(id: String?) {
            val builder = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)

            val data = Data.Builder().apply {
                putString(Intent.EXTRA_TEXT, id)
            }

            val uploadWorkRequest =
                OneTimeWorkRequestBuilder<OfflineDownloadManager>().setInputData(data.build())
                    .setInputData(data.build()).setConstraints(builder.build()).build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork("${id}_download", ExistingWorkPolicy.KEEP, uploadWorkRequest)
        }
    }

}