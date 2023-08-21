package com.rizwansayyed.zene.service.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.rizwansayyed.zene.domain.roomdb.RoomDBImpl
import com.rizwansayyed.zene.utils.downloader.DownloadFilesOkHttp.downloadImageFile
import com.rizwansayyed.zene.utils.downloader.DownloadFilesOkHttp.downloadMP3File
import com.rizwansayyed.zene.utils.downloader.opensource.State
import com.rizwansayyed.zene.utils.downloader.opensource.YTExtractor
import com.rizwansayyed.zene.utils.downloader.opensource.bestQuality
import com.rizwansayyed.zene.utils.downloader.opensource.getAudioOnly
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import java.io.File

@HiltWorker
class WorkManagerDownloadSongs @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val roomDBImpl: RoomDBImpl
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val offlineSongsLists = roomDBImpl.offlineSongs().first()

        var isAnyFailed = false

        offlineSongsLists.forEach {
            if (it.img.contains("https://")) {
                val downloadFile = downloadImageFile(it.songName, it.img)

                if (downloadFile != null) {
                    it.img = downloadFile
                    roomDBImpl.insert(it).collect()
                }
            }

            if (!File(it.songPath).exists()) {
                val yt = YTExtractor(con = context).apply { extract(it.pid) }

                if (yt.state == State.SUCCESS) {
                    val files = yt.getYTFiles()?.getAudioOnly()?.bestQuality()
                    if (files != null) {
                        val savedPath = downloadMP3File(it.songName, files.url ?: "")
                        it.songPath = savedPath ?: ""
                    }
                } else {
                    isAnyFailed = true
                }
            }
        }

        if (isAnyFailed) return Result.failure()


        return Result.success()
    }
}

const val workManagerDownloadSongs = "WORK_MANAGER_DOWNLOAD_SONGS"

fun startDownloadSongsWorkManager() {
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED).build()

    val uploadWorkRequest = OneTimeWorkRequestBuilder<WorkManagerDownloadSongs>()
        .addTag(workManagerDownloadSongs)
        .setConstraints(constraints).build()

    WorkManager.getInstance(context).enqueue(uploadWorkRequest)

}