package com.rizwansayyed.zene.data.api

import android.util.Log
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.db.DataStoreManager.getCustomTimestamp
import com.rizwansayyed.zene.data.db.DataStoreManager.setCustomTimestamp
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.Utils.timeDifferenceInMinutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter

object ApiCache {

    object ApiCacheKeys {
        const val UPDATE_AVAILABILITY_CACHE = "update_availability_cache"
        const val SPONSORS_ADS_CACHE = "sponsors_ads_cache"
        const val MOOD_LIST_CACHE = "mood_list_cache"
        const val LATEST_RELEASES_CACHE = "latest_releases_cache"
        const val TOP_MOST_LISTENING_SONG_CACHE = "top_most_listening_song_cache"
        const val TOP_MOST_LISTENING_ARTISTS_CACHE = "top_most_listening_artists_cache"
        const val SUGGESTED_SONGS_CACHE = "suggested_songs_cache"
        const val RECOMMENDED_PLAYLISTS_CACHE = "recommended_playlists_cache"
        const val RECOMMENDED_ALBUMS_CACHE = "recommended_albums_cache"
        const val RECOMMENDED_VIDEOS_CACHE = "recommended_videos_cache"
        const val SUGGESTED_TOP_SONGS_CACHE = "suggested_top_songs_cache"
    }

    private val pathFolder = File(context.cacheDir, "api_cache").apply {
        mkdir()
    }

    suspend fun getAPICache(key: String): ZeneMusicDataResponse? {
        val path = File(pathFolder, "${key}.json")

        if (!path.exists()) return null
        val content = withContext(Dispatchers.IO) { path.readText() }
        val data = moshi.adapter(Array<ZeneMusicDataItems>::class.java).fromJson(content)
            ?: return null

        if (data.isEmpty()) return null

        val ts = getCustomTimestamp(key) ?: return null
        if (timeDifferenceInMinutes(ts) > (25..30).random()) return null

        return data.toList()
    }

    suspend fun setAPICache(key: String, data: ZeneMusicDataResponse) {
        val path = File(pathFolder, "${key}.json")
        val json =
            moshi.adapter(Array<ZeneMusicDataItems>::class.java).toJson(data.toTypedArray())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val writer = FileWriter(path)
                writer.write(json)
                writer.close()
                setCustomTimestamp(key)
            } catch (e: Exception) {
                Log.d("TAG", "setAPICache: Writing Error ${e.message}")
            }
        }
    }
}