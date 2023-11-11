package com.rizwansayyed.zene.data.onlinesongs.soundcloud.implementation


import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.radio.OnlineRadioService
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
        val q = soundcloudApi.search(q, key).collection

        val socialProfile = soundcloudApi.profile(q?.first()?.urn, key)
        val profileInfo = soundcloudApi.profileDetail(q?.first()?.id, key)

        emit(SoundCloudProfileInfo(socialProfile, profileInfo.collection?.first()))
    }.flowOn(Dispatchers.IO)
}