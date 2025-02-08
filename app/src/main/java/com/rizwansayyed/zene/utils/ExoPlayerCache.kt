package com.rizwansayyed.zene.utils

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.ExoDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

@UnstableApi
object ExoPlayerCache {
    private var simpleCache: SimpleCache? = null

    fun getInstance(context: Context): SimpleCache {
        return simpleCache ?: synchronized(this) {
            val cacheDir = File(context.cacheDir, "exo_cache").apply {
                mkdirs()
            }
            val cacheEvictor = LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024)
            val databaseProvider = ExoDatabaseProvider(context)
            simpleCache = SimpleCache(cacheDir, cacheEvictor, databaseProvider)
            simpleCache!!
        }
    }
}