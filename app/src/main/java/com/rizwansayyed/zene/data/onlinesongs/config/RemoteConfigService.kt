package com.rizwansayyed.zene.data.onlinesongs.config

import com.rizwansayyed.zene.data.utils.ZeneAPI.ADS_LISTS_CONFIG
import com.rizwansayyed.zene.data.utils.ZeneAPI.API_KEYS_CONFIG
import com.rizwansayyed.zene.data.utils.ZeneAPI.APP_DOWNLOADS_CONFIG
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigApiKeyResponse
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigPresentAppDownloadResponse
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigZeneAdsResponse
import retrofit2.http.GET

interface RemoteConfigService {

    @GET(ADS_LISTS_CONFIG)
    suspend fun ads(): RemoteConfigZeneAdsResponse

    @GET(API_KEYS_CONFIG)
    suspend fun key(): RemoteConfigApiKeyResponse

    @GET(APP_DOWNLOADS_CONFIG)
    suspend fun downloads(): RemoteConfigPresentAppDownloadResponse

}
