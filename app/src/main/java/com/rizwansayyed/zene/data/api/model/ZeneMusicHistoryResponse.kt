package com.rizwansayyed.zene.data.api.model

import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.Utils.Share.WEB_BASE_URL
import com.rizwansayyed.zene.utils.Utils.URLS.BASE_URL
import com.rizwansayyed.zene.utils.Utils.URLS.LIKED_SONGS_ON_ZENE_PLAYLISTS
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking


typealias ZeneMusicHistoryResponse = List<ZeneMusicHistoryItem>

data class ZeneMusicHistoryItem(
    val _id: String?,
    val email: String?,
    val name: String?,
    val artists: String?,
    val id: String?,
    val thumbnail: String?,
    val deviceInfo: String?,
    val deviceType: String?,
    val timestamp: Long?,
    val timesItsPlayed: Long?,
    val type: String?,
) {
    fun asMusicData(): ZeneMusicDataItems {
        return ZeneMusicDataItems(name, artists, id, thumbnail, "", type)
    }
}


fun likedMusicData(): ZeneSavedPlaylistsResponseItem = runBlocking {
    val email = userInfoDB.firstOrNull()?.email ?: ""
    val likedSongs = context.resources.getString(R.string.liked_songs)
    val thumbnail = "https://www.zenemusic.co/liked_thumbnail.jpg"

    return@runBlocking ZeneSavedPlaylistsResponseItem(
        "", email, "${email}_${LIKED_SONGS_ON_ZENE_PLAYLISTS}",
        thumbnail, likedSongs, false, System.currentTimeMillis()
    )
}