package com.rizwansayyed.zene.data.onlinesongs.soundcloud.implementation


import com.rizwansayyed.zene.data.onlinesongs.radio.OnlineRadioService
import com.rizwansayyed.zene.data.onlinesongs.soundcloud.SoundCloudApiService
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SoundCloudImpl @Inject constructor(
    private val soundcloudApi: SoundCloudApiService, private val remoteConfig: RemoteConfigInterface
) : SoundCloudImplInterface {

    override suspend fun artistsSocialNetwork(q: String) = flow {
        val key = remoteConfig.allApiKeys()?.soundcloud ?: ""
        val username = soundcloudApi.search(q, key).collection?.first()?.urn
        emit(soundcloudApi.profile(username, key))
    }.flowOn(Dispatchers.IO)
}