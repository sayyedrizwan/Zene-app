package com.rizwansayyed.zene.service.workmanager

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.doOfflineDownloadWifiSettings
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.downloadAFileFromURL
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.Utils.copyFileAndDelete
import com.rizwansayyed.zene.utils.Utils.isConnectedToWifi
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
class OfflineDownloadManagerTest @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val songDownloader: SongDownloaderInterface,
    private val roomDb: RoomDBInterface
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            "songid".toast()
            val songId =
                inputData.getString(Intent.EXTRA_TEXT) ?: return@withContext Result.success()
            val forceDownload = inputData.getBoolean(Intent.EXTRA_TEXT, false)
            val file = File(songDownloadPathTemp, "$songId.mp3")
            val info = roomDb.offlineSongInfo(songId).first()


//            if (!isConnectedToWifi() && doOfflineDownloadWifiSettings.first() && !forceDownload) {
//                info?.progress = -1
//                info?.let { r -> roomDb.insert(r).collect() }
//                return@withContext Result.failure()
//            }

            try {
                val hdSong =
                    songDownloader.download(songId).first() ?: return@withContext Result.failure()
                val download = downloadAFileFromURL(hdSong, file) {
                    "progress".toast()
                    info?.progress = it
                    CoroutineScope(Dispatchers.IO).launch {
                        info?.let { r -> roomDb.insert(r).collect() }
                        if (isActive) cancel()
                    }
                }

                CoroutineScope(Dispatchers.IO).launch {
                    val defaultFolder = File(songDownloadPath, "$songId.mp3")
                    if (download == true) copyFileAndDelete(file, defaultFolder)

                    info?.progress = if (download == true) 100 else 0
                    info?.songPath = if (download == true) defaultFolder.path else ""
                    info?.let { roomDb.insert(it).collect() }
                    if (isActive) cancel()
                }
                return@withContext Result.success()
            } catch (e: Exception) {
                return@withContext Result.failure()
            }
        }
    }


    companion object {

        val songDownloadPath = File(context.filesDir, "download_songs").apply {
            mkdirs()
        }

        val songDownloadPathTemp = File(context.filesDir, "download_songs_temp").apply {
            mkdirs()
        }

        fun startOfflineDownloadWorkManagersss(id: String?, force: Boolean = false) {
            val builder = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)


            val data = Data.Builder().apply {
                putString(Intent.EXTRA_TEXT, id)
                putBoolean(Intent.EXTRA_TEXT, force)
            }


            val uploadWorkRequest =
                OneTimeWorkRequestBuilder<OfflineDownloadManagerTest>().setInputData(data.build())
                    .setInputData(data.build()).setConstraints(builder.build()).build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork("${id}_download", ExistingWorkPolicy.KEEP, uploadWorkRequest)
        }
    }

}