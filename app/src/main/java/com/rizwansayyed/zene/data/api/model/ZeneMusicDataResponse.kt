package com.rizwansayyed.zene.data.api.model

typealias ZeneArtistsData = List<ZeneArtistsDataList>

data class ZeneArtistsDataList(
    val artists: ZeneMusicDataItems,
    val songs: List<ZeneMusicDataItems>,
    val playlists: List<ZeneMusicDataItems>,
    val videos: List<ZeneMusicDataItems>
)

data class ZeneSearchData(
    val artists: List<ZeneMusicDataItems>,
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
        return when (type) {
            "SONGS" -> MusicType.SONGS
            "PLAYLIST" -> MusicType.PLAYLIST
            "ALBUMS" -> MusicType.ALBUMS
            "ARTISTS" -> MusicType.ARTISTS
            "VIDEO" -> MusicType.VIDEO
            "MOOD" -> MusicType.MOOD
            else -> MusicType.NONE
        }
    }
}


enum class MusicType {
    SONGS, PLAYLIST, ALBUMS, ARTISTS, VIDEO, MOOD, NONE
}