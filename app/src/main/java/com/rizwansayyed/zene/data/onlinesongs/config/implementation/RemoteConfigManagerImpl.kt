package com.rizwansayyed.zene.data.onlinesongs.config.implementation

import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.apiKeyCacheTimestamp
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.downloadAppListCacheTimestamp
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.zeneAdsCacheTimestamp
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.config.RemoteConfigService
import com.rizwansayyed.zene.data.utils.CacheFiles
import com.rizwansayyed.zene.data.utils.CacheFiles.remoteConfigDownloadList
import com.rizwansayyed.zene.data.utils.CacheFiles.remoteConfigKey
import com.rizwansayyed.zene.data.utils.CacheFiles.remoteConfigZeneAdsLists
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigApiKeyResponse
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigPresentAppDownloadResponse
import com.rizwansayyed.zene.domain.remoteconfig.RemoteConfigZeneAdsResponse
import com.rizwansayyed.zene.domain.remoteconfig.toCache
import com.rizwansayyed.zene.utils.Utils.timestampDifference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes


class RemoteConfigManager @Inject constructor(private val service: RemoteConfigService) :
    RemoteConfigInterface {

    override suspend fun allApiKeys(): RemoteConfigApiKeyResponse? = withContext(Dispatchers.IO) {
        val cache = responseCache(remoteConfigKey, RemoteConfigApiKeyResponse::class.java)
        if (cache != null && timestampDifference(apiKeyCacheTimestamp.first()) < 180.minutes.inWholeSeconds)
            return@withContext cache

        return@withContext try {
            val key = service.key()
            if (key.music.length < 3) return@withContext null
            key.toCache()?.let { writeToCacheFile(remoteConfigKey, it) }
            key
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun downloadsAppLists() = withContext(Dispatchers.IO) {
        val cache = responseCache(
            remoteConfigDownloadList, RemoteConfigPresentAppDownloadResponse::class.java
        )
        if (cache != null && timestampDifference(downloadAppListCacheTimestamp.first()) < 6.hours.inWholeSeconds)
            return@withContext cache

        return@withContext try {
            val download = service.downloads()
            if ((download.lists?.size ?: 0) <= 0) return@withContext null
            download.toCache()?.let { writeToCacheFile(remoteConfigDownloadList, it) }
            download
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun zeneAds(): RemoteConfigZeneAdsResponse? = withContext(Dispatchers.IO) {
        val cache = responseCache(remoteConfigZeneAdsLists, RemoteConfigZeneAdsResponse::class.java)
        if (cache != null && timestampDifference(zeneAdsCacheTimestamp.first()) < 2.minutes.inWholeSeconds)
            return@withContext cache

        return@withContext try {
            val ads = service.ads()
            if (ads.doShow == null) return@withContext null
            ads.toCache()?.let { writeToCacheFile(remoteConfigZeneAdsLists, it) }
            ads
        } catch (e: Exception) {
            null
        }
    }

}