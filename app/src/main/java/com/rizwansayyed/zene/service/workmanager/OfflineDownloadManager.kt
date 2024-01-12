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
import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.downloadAFileFromURL
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImpl
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.FileDownloaderInChunks
import com.rizwansayyed.zene.utils.Utils.ExtraUtils.DOWNLOAD_SONG_WORKER
import com.rizwansayyed.zene.utils.Utils.copyFileAndDelete
import com.rizwansayyed.zene.utils.Utils.isConnectedToWifi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.time.Duration.Companion.seconds

@HiltWorker
class OfflineDownloadManager @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val songDownloader: SongDownloaderInterface,
    private val youtubeAPI: YoutubeAPIImplInterface,
    private val roomDb: RoomDBInterface
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val songId =
                inputData.getString(Intent.EXTRA_TEXT) ?: return@withContext Result.success()

            val defaultFolder = File(songDownloadPath, "$songId.mp3")

            var offlineEntity = roomDb.offlineSongInfo(songId).first()
            if (offlineEntity != null)
                if (offlineEntity.progress == 100 && defaultFolder.exists()) return@withContext Result.success()

            val songInfo = youtubeAPI.songDetail(songId).first()

            if (offlineEntity == null)
                offlineEntity = OfflineDownloadedEntity(
                    songInfo.songId ?: "",
                    songInfo.name ?: "",
                    songInfo.artists ?: "",
                    songInfo.thumbnail ?: "",
                    "", System.currentTimeMillis(), 0
                )
            else {
                offlineEntity.songName = songInfo.name ?: ""
                offlineEntity.songArtists = songInfo.artists ?: ""
                offlineEntity.thumbnail = songInfo.thumbnail ?: ""
                offlineEntity.timestamp = System.currentTimeMillis()
            }

            roomDb.insert(offlineEntity).first()

            val force = inputData.getBoolean(DOWNLOAD_SONG_WORKER, false)

            val file = File(songDownloadPathTemp, "$songId.mp3")
            val info = roomDb.offlineSongInfo(songId).first()

            if (!isConnectedToWifi() && doOfflineDownloadWifiSettings.first() && !force) {
                info?.progress = -1
                info?.let { r -> roomDb.insert(r).collect() }
                return@withContext Result.failure()
            }

            try {
                val hdSong =
                    songDownloader.download(songId).first() ?: return@withContext Result.failure()

                FileDownloaderInChunks(hdSong) { progress, status ->
                    info?.progress = progress ?: 0
                    CoroutineScope(Dispatchers.IO).launch {
                        info?.let { r -> roomDb.insert(r).collect() }
                        if (isActive) cancel()
                    }
                    if (status == true && progress == 100)  CoroutineScope(Dispatchers.IO).launch {
                        copyFileAndDelete(file, defaultFolder)

                        info?.progress = progress
                        info?.songPath = defaultFolder.path
                        info?.let { roomDb.insert(it).collect() }
                        if (isActive) cancel()
                    }
                }.startDownloadingOffline(file)
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
            if (exists()) deleteRecursively()
            mkdirs()
        }

        fun startOfflineDownloadWorkManager(id: String?, force: Boolean = false) =
            CoroutineScope(Dispatchers.IO).launch {
                if (force) {
                    WorkManager.getInstance(context).cancelAllWorkByTag("${id}_download")
                    delay(2.seconds)
                }
                val builder = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)

                val data = Data.Builder().apply {
                    putString(Intent.EXTRA_TEXT, id)
                    putBoolean(DOWNLOAD_SONG_WORKER, force)
                }

                val uploadWorkRequest =
                    OneTimeWorkRequestBuilder<OfflineDownloadManager>().setInputData(data.build())
                        .addTag("${id}_download")
                        .setInputData(data.build()).setConstraints(builder.build()).build()

                WorkManager.getInstance(context)
                    .enqueueUniqueWork("${id}_download", ExistingWorkPolicy.KEEP, uploadWorkRequest)
            }
    }

}