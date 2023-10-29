package com.rizwansayyed.zene.data.utils.config

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.remoteconfig.YtApiKeyResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

const val REMOTE_INSTAGRAM_APP_ID = "instagramAppID"
const val REMOTE_YT_API_KEYS = "ytApiKeys"

class RemoteConfigManager @Inject constructor() : RemoteConfigInterface {

    override suspend fun instagramAppID(): String {
        return config(false)?.getString(REMOTE_INSTAGRAM_APP_ID) ?: ""
    }

    override suspend fun ytApiKeys(): YtApiKeyResponse? {
        return try {
            val data = config(false)?.getString(REMOTE_YT_API_KEYS)
            moshi.adapter(YtApiKeyResponse::class.java).fromJson(data!!)
        } catch (e: Exception) {
            null
        }

    }


    override suspend fun config(doReset: Boolean): FirebaseRemoteConfig? {
        return withContext(Dispatchers.IO) {
            try {
                val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
                val configSettings = remoteConfigSettings {
                    minimumFetchIntervalInSeconds = 3600
                }
                remoteConfig.setConfigSettingsAsync(configSettings)
                remoteConfig.fetchAndActivate().await()
                if (doReset) remoteConfig.reset()
                remoteConfig
            } catch (e: Exception) {
                null
            }
        }
    }
}