package com.rizwansayyed.zene.service.workmanager

import android.content.Context
import android.util.Log
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
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.social.SocialMediaScrapsImplInterface
import com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation.LastFMImplInterface
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.timestampDifference
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
import kotlin.math.log
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours


@HiltWorker
class ArtistsInfoWorkManager @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val bingScraps: BingScrapsInterface,
    private val roomDb: RoomDBInterface,
    private val socialMediaScrap: SocialMediaScrapsImplInterface,
    private val lastFMImpl: LastFMImplInterface
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val artists = roomDb.pinnedArtists().first()

            artists.forEach { a ->
                CoroutineScope(Dispatchers.IO).launch {
                    if (timestampDifference(a.updatedTime) > 2.days.inWholeSeconds) {
                        roomDb.artistsThumbnailUpdate(
                            lastFMImpl.artistsUsername(a.name).first()?.image ?: ""
                        ).collect()
                    }

                    if (isHaveAllSocialMedia(a)) {
                        if (timestampDifference(a.updatedTime) > 3.hours.inWholeSeconds) {
                            val info = roomDb.artistsData(a.name).first()
                            socialMediaScrap.getAllArtistsData(info)
                        }
                    } else {
                        val info = bingScraps.bingOfficialAccounts(a).first()
                        roomDb.insert(info).collect()
                        socialMediaScrap.getAllArtistsData(info)
                    }

                    if (isActive) cancel()
                }
            }

            Result.success()
        }
    }

    private fun isHaveAllSocialMedia(info: PinnedArtistsEntity): Boolean {
        if (info.instagramUsername.length <= 3) return false
        if (info.xChannel.length <= 3) return false
        if (info.facebookPage.length <= 3) return false
        if (info.tiktokPage.length <= 3) return false
        if (info.youtubeChannel.length <= 3) return false
        Log.d("", "")
        return true
    }

    companion object {

        private const val artistsInfoWorkManager = "artists_info_work_manager"

        fun startArtistsInfoWorkManager() = CoroutineScope(Dispatchers.IO).launch {
            val builder = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)

            val uploadWorkRequest =
                OneTimeWorkRequestBuilder<ArtistsInfoWorkManager>().setConstraints(builder.build())
                    .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                artistsInfoWorkManager, ExistingWorkPolicy.KEEP, uploadWorkRequest
            )
        }
    }

}
