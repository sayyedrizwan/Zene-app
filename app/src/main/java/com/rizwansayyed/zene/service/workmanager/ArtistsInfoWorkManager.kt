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
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImpl
import com.rizwansayyed.zene.di.ApplicationModule
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.utils.Utils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.time.Duration.Companion.seconds


@HiltWorker
class ArtistsInfoWorkManager @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val songDownloader: SongDownloaderInterface,
    private val roomDb: RoomDBInterface
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val artists = roomDb.pinnedArtists().first()

            artists.forEach { a ->

            }


            Result.success()
        }
    }

    companion object {

        private const val artistsInfoWorkManager = "artists_info_work_manager"

        fun startArtistsInfoWorkManager() = CoroutineScope(Dispatchers.IO).launch {
            val builder = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)

            val uploadWorkRequest =
                OneTimeWorkRequestBuilder<ArtistsInfoWorkManager>().setConstraints(builder.build())
                    .build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(
                    artistsInfoWorkManager,
                    ExistingWorkPolicy.KEEP,
                    uploadWorkRequest
                )
        }
    }

}
