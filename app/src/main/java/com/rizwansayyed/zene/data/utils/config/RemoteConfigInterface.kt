package com.rizwansayyed.zene.data.utils.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.rizwansayyed.zene.domain.remoteconfig.YtApiKeyResponse

interface RemoteConfigInterface {

    suspend fun instagramAppID(): String

    suspend fun ytApiKeys(): YtApiKeyResponse?

    suspend fun config(doReset: Boolean): FirebaseRemoteConfig
}