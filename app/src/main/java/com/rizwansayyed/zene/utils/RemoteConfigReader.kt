package com.rizwansayyed.zene.utils

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.tasks.await

class RemoteConfigReader {

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private val configSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = 3600
    }
    suspend fun getYTKey(): String {
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.fetchAndActivate().await()
        return remoteConfig.getString("yt_api_key")
    }
}