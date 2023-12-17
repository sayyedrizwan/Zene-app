package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.social

import android.util.Log
import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import com.rizwansayyed.zene.data.onlinesongs.instagram.InstagramInfoService
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SocialMediaScrapsImpl @Inject constructor(
    private val instagramService: InstagramInfoService,
    private val remoteConfig: RemoteConfigInterface
) : SocialMediaScrapsImplInterface {
    override suspend fun getAllArtistsData(a: PinnedArtistsEntity) = job.launch {
        try {
            val instagram =
                instagramService.instagramInfo(remoteConfig.instagramAppID(), a.instagramUsername)

            Log.d("TAG", "getAllArtistsData: ${instagram.data?.user?.edge_felix_video_timeline?.edges?.size}")
        }catch (e: Exception){
            Log.d("TAG", "getAllArtistsData: ${e.message}")
        }

    }

}