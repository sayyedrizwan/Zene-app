package com.rizwansayyed.zene.data.utils.config

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigApiKeyResponse
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigPresentAppDownloadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject


const val REMOTE_ALL_API_KEYS = "allApiKeys"
const val REMOTE_PRESENT_APP_DOWNLOAD = "present_app_download"

class RemoteConfigManager @Inject constructor() : RemoteConfigInterface {


    override suspend fun allApiKeys(): RemoteConfigApiKeyResponse? {
        return try {
            val data = config(false)?.getString(REMOTE_ALL_API_KEYS)
            moshi.adapter(RemoteConfigApiKeyResponse::class.java).fromJson(data!!)
        } catch (e: Exception) {
            null
        }

    }
    override suspend fun downloadsAppLists(): RemoteConfigPresentAppDownloadResponse? {
        return try {
            val data = config(false)?.getString(REMOTE_PRESENT_APP_DOWNLOAD)
            moshi.adapter(RemoteConfigPresentAppDownloadResponse::class.java).fromJson(data!!)
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