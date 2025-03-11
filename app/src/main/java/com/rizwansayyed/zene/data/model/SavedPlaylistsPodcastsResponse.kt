package com.rizwansayyed.zene.data.model

typealias SavedPlaylistsPodcastsResponse = List<SavedPlaylistsPodcastsResponseItem>

data class SavedPlaylistsPodcastsResponseItem(
    val artist: String?,
    val email: String?,
    val id: String?,
    val img: String?,
    val name: String?,
    val timestamp: Long?,
    val type: String?,
) {
    fun isPodcast() = type == "PODCAST"
    fun isUserPlaylist() = type == "USER_PLAYLISTS"
}