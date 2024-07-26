package com.rizwansayyed.zene.data.api.model

import java.util.Locale

data class ZeneArtistsInfoResponse(
    val desc: String?,
    val followers: Int?,
    val img: List<String?>?,
    val name: String?,
    val radioID: String?,
    val socialMedia: List<SocialMedia?>?,
    val topSongs: List<ZeneMusicDataItems?>?
) {
    data class SocialMedia(
        val network: String?,
        val title: String?,
        val url: String?,
        val username: String?
    )

    fun followers(): String {
        return when {
            (followers ?: 0) >= 1_000_000 ->
                String.format(Locale.getDefault(), "%.1fM", (followers ?: 0) / 1_000_000.0)
            (followers ?: 0) >= 1_000 ->
                String.format(Locale.getDefault(), "%.1fK", (followers ?: 0) / 1_000.0)
            else -> followers.toString()
        }
    }
}


data class ZeneArtistsDataResponse(
    val songs: List<ZeneMusicDataItems>,
    val albums: List<ZeneMusicDataItems>,
    val videos: List<ZeneMusicDataItems>,
    val playlists: List<ZeneMusicDataItems>,
    val artists: List<ZeneMusicDataItems>,
    val news: List<ZeneMusicDataItems?>?
)