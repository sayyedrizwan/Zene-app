package com.rizwansayyed.zene.domain.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.ALBUM_HEADER_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.ALBUM_HEADER_TIMESTAMP
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.ARTISTS_SUGGESTIONS_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.ARTISTS_SUGGESTIONS_TIMESTAMP
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.BETA_DIALOG
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.DO_MUSIC_PLAYER_LOOP
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.FOOTER_ALBUMS_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.IP_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.MUSIC_PLAYER_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.SONGS_ALL_FOR_YOU_ALL_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.SONGS_ALL_FOR_YOU_ALL_TIMESTAMP
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.SONGS_SUGGESTIONS_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.SONGS_SUGGESTIONS_FOR_YOU_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.SONGS_SUGGESTIONS_FOR_YOU_TIMESTAMP
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.SONGS_SUGGESTIONS_TIMESTAMP
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_ARTISTS_OF_WEEK_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_ARTISTS_SONGS_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_ARTISTS_SONGS_TIMESTAMP
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_ARTISTS_TIMESTAMP
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_COUNTRY_SONGS_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_COUNTRY_SONGS_TIMESTAMP
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_COUNTRY_SONGS_YT_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_COUNTRY_SONGS_YT_TIMESTAMP
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_GLOBAL_SONGS_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.TOP_GLOBAL_SONGS_TIMESTAMP
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.TRENDING_SONGS_TOP_K_POP_DATA
import com.rizwansayyed.zene.domain.datastore.DataStoreUtil.DATA_STORE_KEY.TRENDING_SONGS_TOP_K_POP_TIMESTAMP

object DataStoreUtil {

    object DATA_STORE_KEY {
        const val DB_STORE = "datastore_zene"

        const val ALBUM_HEADER_DATA = "album_header_data"
        const val FOOTER_ALBUMS_DATA = "footer_albums_data"
        const val ALBUM_HEADER_TIMESTAMP = "album_header_timestamp"
        const val TOP_ARTISTS_OF_WEEK_DATA = "top_artists_of_week"
        const val TOP_ARTISTS_TIMESTAMP = "top_artists_timestamp"
        const val TOP_GLOBAL_SONGS_DATA = "top_global_songs_data"
        const val TOP_GLOBAL_SONGS_TIMESTAMP = "top_global_songs_timestamp"
        const val TOP_COUNTRY_SONGS_YT_DATA = "top_country_songs_yt_data"
        const val TOP_COUNTRY_SONGS_YT_TIMESTAMP = "top_country_songs_yt_timestamp"
        const val TOP_COUNTRY_SONGS_DATA = "top_country_songs_data"
        const val TOP_COUNTRY_SONGS_TIMESTAMP = "top_country_songs_timestamp"
        const val SONGS_SUGGESTIONS_DATA = "songs_suggestions_data"
        const val SONGS_SUGGESTIONS_TIMESTAMP = "songs_suggestions_timestamp"
        const val TRENDING_SONGS_TOP_K_POP_DATA = "trending_songs_top_k_pop_data"
        const val TRENDING_SONGS_TOP_K_POP_TIMESTAMP = "trending_songs_top_k_pop_timestamp"
        const val SONGS_SUGGESTIONS_FOR_YOU_DATA = "songs_suggestions_for_you_data"
        const val SONGS_SUGGESTIONS_FOR_YOU_TIMESTAMP = "songs_suggestions_for_you_timestamp"
        const val ARTISTS_SUGGESTIONS_DATA = "artists_suggestions_data"
        const val ARTISTS_SUGGESTIONS_TIMESTAMP = "artists_suggestions_timestamp"
        const val SONGS_ALL_FOR_YOU_ALL_DATA = "songs_all_for_you_all_data"
        const val SONGS_ALL_FOR_YOU_ALL_TIMESTAMP = "songs_all_for_you_all_timestamp"
        const val TOP_ARTISTS_SONGS_DATA = "top_artists_songs_data"
        const val TOP_ARTISTS_SONGS_TIMESTAMP = "top_artists_songs_timestamp"
        const val IP_DATA = "ip_data"
        const val MUSIC_PLAYER_DATA = "music_player_data"
        const val DO_MUSIC_PLAYER_LOOP = "do_music_player_loop"
        const val BETA_DIALOG = "beta_dialog"
    }

    val album_header_data = stringPreferencesKey(ALBUM_HEADER_DATA)
    val footer_albums_data = stringPreferencesKey(FOOTER_ALBUMS_DATA)
    val album_header_timestamp = longPreferencesKey(ALBUM_HEADER_TIMESTAMP)

    val top_artists_of_week_data = stringPreferencesKey(TOP_ARTISTS_OF_WEEK_DATA)
    val top_artists_timestamp = longPreferencesKey(TOP_ARTISTS_TIMESTAMP)


    val top_global_songs_data = stringPreferencesKey(TOP_GLOBAL_SONGS_DATA)
    val top_global_songs_timestamp = longPreferencesKey(TOP_GLOBAL_SONGS_TIMESTAMP)


    val top_country_songs_data = stringPreferencesKey(TOP_COUNTRY_SONGS_DATA)
    val top_country_songs_timestamp = longPreferencesKey(TOP_COUNTRY_SONGS_TIMESTAMP)


    val top_country_songs_yt_data = stringPreferencesKey(TOP_COUNTRY_SONGS_YT_DATA)
    val top_country_songs_yt_timestamp = longPreferencesKey(TOP_COUNTRY_SONGS_YT_TIMESTAMP)


    val trending_songs_top_k_pop_data = stringPreferencesKey(TRENDING_SONGS_TOP_K_POP_DATA)
    val trending_songs_top_k_pop_timestamp = longPreferencesKey(TRENDING_SONGS_TOP_K_POP_TIMESTAMP)

    val songs_suggestions_for_you_data = stringPreferencesKey(SONGS_SUGGESTIONS_FOR_YOU_DATA)
    val songs_suggestions_for_you_timestamp = longPreferencesKey(SONGS_SUGGESTIONS_FOR_YOU_TIMESTAMP)


    val artists_suggestions_data = stringPreferencesKey(ARTISTS_SUGGESTIONS_DATA)
    val artists_suggestions_timestamp = longPreferencesKey(ARTISTS_SUGGESTIONS_TIMESTAMP)

    val songs_all_for_you_all_data = stringPreferencesKey(SONGS_ALL_FOR_YOU_ALL_DATA)
    val songs_all_for_you_all_timestamp = longPreferencesKey(SONGS_ALL_FOR_YOU_ALL_TIMESTAMP)


    val top_artists_songs_data = stringPreferencesKey(TOP_ARTISTS_SONGS_DATA)
    val top_artists_songs_timestamp = longPreferencesKey(TOP_ARTISTS_SONGS_TIMESTAMP)


    val songs_suggestions_data = stringPreferencesKey(SONGS_SUGGESTIONS_DATA)
    val songs_suggestions_timestamp = longPreferencesKey(SONGS_SUGGESTIONS_TIMESTAMP)


    val ip_data = stringPreferencesKey(IP_DATA)
    val music_player_data = stringPreferencesKey(MUSIC_PLAYER_DATA)
    val do_music_player_loop = booleanPreferencesKey(DO_MUSIC_PLAYER_LOOP)
    val beta_dialog = booleanPreferencesKey(BETA_DIALOG)
}