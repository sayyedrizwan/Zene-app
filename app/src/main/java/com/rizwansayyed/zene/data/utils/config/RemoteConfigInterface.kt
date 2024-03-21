package com.rizwansayyed.zene.data.utils.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigApiKeyResponse
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigPresentAppDownloadResponse

interface RemoteConfigInterface {

    suspend fun config(doReset: Boolean): FirebaseRemoteConfig?
    suspend fun allApiKeys(): RemoteConfigApiKeyResponse?
    suspend fun downloadsAppLists(): RemoteConfigPresentAppDownloadResponse?
}