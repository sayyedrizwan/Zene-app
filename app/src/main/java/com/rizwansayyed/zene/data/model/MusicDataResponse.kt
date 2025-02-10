package com.rizwansayyed.zene.data.model

enum class MusicDataTypes {
    NONE, SONGS, PLAYLISTS, ALBUMS, ARTISTS, PODCAST, PODCAST_CATEGORIES, NEWS, MOVIES, AI_MUSIC
}

data class AIDataResponse(
    val isExpire: Boolean?,
    val trending: List<ZeneMusicData?>?,
    val new: List<ZeneMusicData?>?,
    val list: List<ZeneMusicData?>?
)

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

typealias ZeneMusicDataList = List<ZeneMusicData>

data class ZeneMusicData(
    val artists: String?,
    val id: String?,
    val name: String?,
    val path: String?,
    val thumbnail: String?,
    val type: String?,
    val extra: String?
) {
    fun podcastTimestamp(): Boolean {
        if (extra?.contains("s ago") == true) return true
        else if (extra?.contains("m ago") == true) return true
        else if (extra?.contains("h ago") == true) return true

        return false
    }

    fun type(): MusicDataTypes {
        return when (type) {
            "SONGS" -> MusicDataTypes.SONGS
            "PLAYLISTS" -> MusicDataTypes.PLAYLISTS
            "ALBUMS" -> MusicDataTypes.ALBUMS
            "ARTISTS" -> MusicDataTypes.ARTISTS
            "PODCAST_CATEGORIES" -> MusicDataTypes.PODCAST_CATEGORIES
            "PODCAST" -> MusicDataTypes.PODCAST
            "NEWS" -> MusicDataTypes.NEWS
            "MOVIES" -> MusicDataTypes.MOVIES
            "AI_MUSIC" -> MusicDataTypes.AI_MUSIC
            else -> MusicDataTypes.NONE
        }
    }
}
