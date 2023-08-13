package com.rizwansayyed.zene.utils

import android.widget.Toast
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object Utils {

    object URL {
        const val ALBUMS_WITH_HEADERS = "albumsWithHeaders"
        const val TOP_ARTIST_THIS_WEEK = "topArtistThisWeek"
        const val TOP_GLOBAL_SONGS_THIS_WEEK = "topGlobalSongsThisWeek"
        const val TOP_COUNTRY_SONGS = "topCountrySongs"
        const val SONG_SUGGESTIONS = "songSuggestions"
        const val SONG_SUGGESTIONS_FOR_YOU = "songSuggestionsForYou"
        const val SEARCH_SONGS = "searchSongs"
        const val SONG_PLAY_DETAILS = "songPlayDetails"
        const val TRENDING_SONGS_S_TOP_50 = "trendingSongsSTop50"
        const val TRENDING_SONGS_APPLE = "trendingSongsApple"
        const val TRENDING_SONGS_TOP_K_POP = "trendingSongsTopKPop"
        const val TRENDING_SONGS_TOP_50_K_POP = "trendingSongsTop50KPop"
        const val SIMILAR_ARTISTS = "similarArtists"


        const val IP_JSON_BASE_URL = "http://ip-api.com/"
        const val IP_JSON = "json"
    }

    object DB {
        const val RECENT_PLAYED_DB = "recent_played_db"
        const val SONG_DETAILS_DB = "song_details_db"
        const val RECENT_PLAYED_ARTISTS_DB = "recent_played_artists_db"
    }


    val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    fun String.showToast() = CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, this@showToast, Toast.LENGTH_LONG).show()
        if (isActive) cancel()
    }

    fun String.shortTextForView(maxLength: Int = 15): String {
        if (this.length <= maxLength) return this
        return this.substring(0, maxLength - 3) + "..."
    }
}