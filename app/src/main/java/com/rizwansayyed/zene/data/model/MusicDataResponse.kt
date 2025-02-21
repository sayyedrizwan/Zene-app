package com.rizwansayyed.zene.data.model

enum class MusicDataTypes {
    NONE, SONGS, RADIO, VIDEOS, PLAYLISTS, ALBUMS, ARTISTS, PODCAST, PODCAST_CATEGORIES, NEWS, MOVIES, AI_MUSIC, TEXT
}

data class AIDataResponse(
    val isExpire: Boolean?,
    val trending: List<ZeneMusicData?>?,
    val new: List<ZeneMusicData?>?,
    val list: List<ZeneMusicData?>?
)

data class SearchTrendingResponse(
    val artists: List<ZeneMusicData?>?,
    val songs: List<ZeneMusicData?>?,
    val globalSongs: List<ZeneMusicData?>?,
    val keywords: List<ZeneMusicData?>?
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

data class NewPlaylistResponse(
    val isExpire: Boolean?,
    val playlistID: String?,
)

data class UserPlaylistResponse(
    val email: String?,
    val id: String?,
    val img: String?,
    val name: String?,
    val exists: Boolean?,
    val track_count: Int?,
)

data class PlayerLyricsInfoResponse(
    val plainLyrics: String?,
    val syncedLyrics: String?
)

data class MediaLikedResponse(val isLiked: Boolean?)

typealias ZeneMusicDataList = List<ZeneMusicData>

data class ZeneMusicData(
    val artists: String?,
    val id: String?,
    val name: String?,
    val path: String?,
    val thumbnail: String?,
    val type: String?,
    val extra: String? = null,
    val extraInfo: String? = null,
    val isExpire: Boolean? = false
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
            "VIDEOS" -> MusicDataTypes.VIDEOS
            "RADIO" -> MusicDataTypes.RADIO
            "ARTISTS" -> MusicDataTypes.ARTISTS
            "PODCAST_CATEGORIES" -> MusicDataTypes.PODCAST_CATEGORIES
            "PODCAST" -> MusicDataTypes.PODCAST
            "NEWS" -> MusicDataTypes.NEWS
            "MOVIES" -> MusicDataTypes.MOVIES
            "AI_MUSIC" -> MusicDataTypes.AI_MUSIC
            "TEXT" -> MusicDataTypes.TEXT
            else -> MusicDataTypes.NONE
        }
    }
}
