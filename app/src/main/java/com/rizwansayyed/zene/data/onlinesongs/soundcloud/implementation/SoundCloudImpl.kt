package com.rizwansayyed.zene.data.onlinesongs.soundcloud.implementation


import com.rizwansayyed.zene.data.onlinesongs.soundcloud.SoundCloudApiService
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.domain.soundcloud.SoundCloudProfileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SoundCloudImpl @Inject constructor(
    private val soundcloudApi: SoundCloudApiService, private val remoteConfig: RemoteConfigInterface
) : SoundCloudImplInterface {

    override suspend fun artistsProfileDetails(q: String) = flow {
        val key = remoteConfig.allApiKeys()?.soundcloud ?: ""
        val s = soundcloudApi.search(q, key).collection?.first()
        val socialProfile = soundcloudApi.profile(s?.urn, key)

        emit(SoundCloudProfileInfo(socialProfile, s?.followers_count))
    }.flowOn(Dispatchers.IO)


    override suspend fun artistsSmallInfo(q: String) = flow {
        val key = remoteConfig.allApiKeys()?.soundcloud ?: ""
        emit(soundcloudApi.search(q, key).collection?.first())
    }.flowOn(Dispatchers.IO)
}