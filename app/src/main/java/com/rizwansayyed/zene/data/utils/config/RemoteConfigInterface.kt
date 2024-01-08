package com.rizwansayyed.zene.data.utils.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigApiKeyResponse

interface RemoteConfigInterface {

    suspend fun config(doReset: Boolean): FirebaseRemoteConfig?
    suspend fun allApiKeys(): RemoteConfigApiKeyResponse?
}