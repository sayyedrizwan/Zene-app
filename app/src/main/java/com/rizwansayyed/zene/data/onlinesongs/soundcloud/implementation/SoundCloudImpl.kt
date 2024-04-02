package com.rizwansayyed.zene.data.onlinesongs.soundcloud.implementation


import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.soundcloud.SoundcloudScrapImplInterface
import com.rizwansayyed.zene.data.onlinesongs.soundcloud.SoundCloudApiService
import com.rizwansayyed.zene.domain.soundcloud.SoundCloudProfileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SoundCloudImpl @Inject constructor(
    private val soundcloudApi: SoundCloudApiService,
    private val soundcloudScrap: SoundcloudScrapImplInterface
) : SoundCloudImplInterface {

    override suspend fun artistsProfileDetails(q: String) = flow {
        val key = soundcloudScrap.getClientId().first()
        val s = soundcloudApi.search(q, key).collection?.first()
        val socialProfile = soundcloudApi.profile(s?.urn, key)

        emit(SoundCloudProfileInfo(socialProfile, s?.followers_count))
    }.flowOn(Dispatchers.IO)


    override suspend fun artistsSmallInfo(q: String) = flow {
        val key = soundcloudScrap.getClientId().first()
        emit(soundcloudApi.search(q, key).collection?.first())
    }.flowOn(Dispatchers.IO)
}