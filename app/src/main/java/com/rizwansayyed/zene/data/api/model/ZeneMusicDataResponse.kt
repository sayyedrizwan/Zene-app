package com.rizwansayyed.zene.data.api.model

typealias ZeneArtistsData = List<ZeneArtistsDataList>

data class ZeneArtistsDataList(
    val artists: ZeneMusicDataItems,
    val songs: List<ZeneMusicDataItems>,
    val playlists: List<ZeneMusicDataItems>,
    val videos: List<ZeneMusicDataItems>
)

typealias ZeneSearchData = List<ZeneSearchDataList>

data class ZeneSearchDataList(
    val artists: ZeneMusicDataItems,
    val songs: List<ZeneMusicDataItems>,
    val playlists: List<ZeneMusicDataItems>,
    val videos: List<ZeneMusicDataItems>,
    val albums: List<ZeneMusicDataItems>
)

typealias ZeneMusicDataResponse = List<ZeneMusicDataItems>

data class ZeneMusicDataItems(
    val name: String?,
    val artists: String?,
    val id: String?,
    val thumbnail: String?,
    val extra: String?,
    private val type: String?
) {
    fun type(): MusicType {
        return if (type == "SONGS") MusicType.SONGS
        else if (type == "PLAYLIST") MusicType.PLAYLIST
        else if (type == "ALBUMS") MusicType.ALBUMS
        else if (type == "ARTISTS") MusicType.ARTISTS
        else if (type == "VIDEO") MusicType.VIDEO
        else if (type == "MOOD") MusicType.MOOD
        else MusicType.NONE
    }
}


enum class MusicType {
    SONGS, PLAYLIST, ALBUMS, ARTISTS, VIDEO, MOOD, NONE
}