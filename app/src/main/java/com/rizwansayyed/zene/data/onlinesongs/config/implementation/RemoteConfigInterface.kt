package com.rizwansayyed.zene.data.onlinesongs.config.implementation

import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigApiKeyResponse
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigPresentAppDownloadResponse
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigZeneAdsResponse

interface RemoteConfigInterface {

    suspend fun allApiKeys(): RemoteConfigApiKeyResponse?
    suspend fun downloadsAppLists(): RemoteConfigPresentAppDownloadResponse?
    suspend fun zeneAds(): RemoteConfigZeneAdsResponse?
}