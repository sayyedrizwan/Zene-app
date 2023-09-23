package com.rizwansayyed.zene.data.utils.config

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

const val REMOTE_INSTAGRAM_APP_ID = "instagramAppID"

class RemoteConfigManager @Inject constructor(): RemoteConfigInterface {

    override suspend fun instagramAppID(): String {
        val remote = config()
        remote.fetchAndActivate().await()
       return remote.getString(REMOTE_INSTAGRAM_APP_ID)
    }


    fun config(): FirebaseRemoteConfig {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        return remoteConfig
    }
}