package com.rizwansayyed.zene.utils

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.ExoDatabaseProvider
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.DefaultLoadControl
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import java.io.File

@UnstableApi
object ExoPlayerCache {
    private var simpleCache: SimpleCache? = null

    val loadControl = DefaultLoadControl.Builder().setBufferDurationsMs(
        5000, 10000, 1500, 2000
    ).build()

    fun getInstance(context: Context): Pair<DefaultTrackSelector, CacheDataSource.Factory> {
        simpleCache ?: synchronized(this) {
            val cacheDir = File(context.cacheDir, "exo_cache").apply {
                mkdirs()
            }
            val cacheEvictor = LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024)
            val databaseProvider = ExoDatabaseProvider(context)
            simpleCache = SimpleCache(cacheDir, cacheEvictor, databaseProvider)
            simpleCache!!
        }
        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(simpleCache!!)
            .setUpstreamDataSourceFactory(DefaultHttpDataSource.Factory())

        val trackSelector = DefaultTrackSelector(context).apply {
            setParameters(
                buildUponParameters().setMaxVideoBitrate(500_000).setForceLowestBitrate(true)
            )
        }

        return Pair(trackSelector, cacheDataSourceFactory)
    }
}