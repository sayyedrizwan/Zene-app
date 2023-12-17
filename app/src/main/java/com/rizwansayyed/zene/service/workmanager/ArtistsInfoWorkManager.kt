package com.rizwansayyed.zene.service.workmanager

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing.BingScrapsInterface
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


@HiltWorker
class ArtistsInfoWorkManager @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val bingScraps: BingScrapsInterface,
    private val roomDb: RoomDBInterface
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val artists = roomDb.pinnedArtists().first()

            artists.forEach { a ->
                CoroutineScope(Dispatchers.IO).launch {
                    if (isHaveAllSocialMedia(a)) {
                        val info = roomDb.artistsData(a.name).first()

                    } else {
                        val info = bingScraps.bingOfficialAccounts(a).first()
                        roomDb.insert(info).collect()
                    }

                    if (isActive) cancel()
                }
            }

            Result.success()
        }
    }

    private fun isHaveAllSocialMedia(info: PinnedArtistsEntity): Boolean {
        var notHave = false
        if (info.instagramUsername.length <= 3 || !info.instagramUsername.contains("none")) return notHave
        if (info.xChannel.length <= 3 || !info.xChannel.contains("none")) return notHave
        if (info.facebookPage.length <= 3 || !info.facebookPage.contains("none")) return notHave
        if (info.tiktokPage.length <= 3 || !info.tiktokPage.contains("none")) return notHave
        if (info.youtubeChannel.length <= 3 || !info.youtubeChannel.contains("none")) return notHave

        notHave = true
        return notHave
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
