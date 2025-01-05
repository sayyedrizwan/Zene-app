package com.rizwansayyed.zene.data.model

enum class MusicDataTypes {
    NONE, SONGS, PLAYLISTS, ALBUMS, ARTISTS
}


data class MusicDataResponse(
    val isExpire: Boolean?,
    val albums: List<ZeneMusicData?>?,
    val favouriteArtists: List<ZeneMusicData?>?,
    val playlists: List<ZeneMusicData?>?,
    val songsToExplore: List<ZeneMusicData?>?,
    val songsYouMayLike: List<ZeneMusicData?>?,
    val topPlaylists: List<ZeneMusicData?>?,
    val topSongs: List<ZeneMusicData?>?
)

data class ZeneMusicData(
    val artists: String?,
    val id: String?,
    val name: String?,
    val path: String?,
    val thumbnail: String?,
    val type: String?,
    val extra: String?
) {
    fun type(): MusicDataTypes {
        return when (type) {
            "SONGS" -> MusicDataTypes.SONGS
            "PLAYLISTS" -> MusicDataTypes.PLAYLISTS
            "ALBUMS" -> MusicDataTypes.ALBUMS
            "ARTISTS" -> MusicDataTypes.ARTISTS
            else -> MusicDataTypes.NONE
        }
    }
}
