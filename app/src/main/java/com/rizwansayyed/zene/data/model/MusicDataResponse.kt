package com.rizwansayyed.zene.data.model

import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.view.playlist.PlaylistsType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class MusicDataTypes {
    NONE, SONGS, RADIO, VIDEOS, PLAYLISTS, ALBUMS, ARTISTS, PODCAST, PODCAST_AUDIO, PODCAST_CATEGORIES, NEWS, MOVIES_SHOW, AI_MUSIC, CELEB_LIFESTYLE, TEXT
}

enum class MyLibraryTypes(val names: Int) {
    HISTORY(R.string.history), SAVED(R.string.saved_mix), MY_PLAYLISTS(R.string.my_playlists)
}

enum class LikeItemType {
    LOADING, LIKE, NONE
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

data class PodcastPlaylistResponse(
    val info: ZeneMusicData?,
    val list: ZeneMusicDataList? = null,
    val isAdded: Boolean? = null,
    val isExpire: Boolean? = null
) {
    fun type(): PlaylistsType {
        return if (info?.type() == MusicDataTypes.PODCAST) PlaylistsType.PODCAST else PlaylistsType.PLAYLIST_ALBUMS
    }
}

data class ArtistsResponse(
    val data: ZeneMusicData?,
    val isFollowing: Boolean?,
    val albums: ZeneMusicDataList?,
    val singles: ZeneMusicDataList?,
    val videos: ZeneMusicDataList?,
    val songs: ZeneMusicDataList?,
    val playlists: ZeneMusicDataList?,
    val artists: ZeneMusicDataList?,
    val isExpire: Boolean?
)

data class MediaPathResponse(val urlPath: String?)

data class PlayerVideoForSongsResponse(val videoID: ZeneMusicData?, val lyricsID: ZeneMusicData?)
data class CountResponse(val count: Int?)

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
    val secId: String? = null,
    val isExpire: Boolean? = false,
    var artistsList: List<ExtraData?> = emptyList(),
    var albumInfo: ExtraData? = null
) {
    data class ExtraData(val name: String?, val id: String?)

    fun podcastTimestamp(): Boolean {
        if (extra?.contains("s ago") == true) return true
        else if (extra?.contains("m ago") == true) return true
        else if (extra?.contains("h ago") == true) return true

        return false
    }

    fun timeAgo(): String {
        try {
            val epoch = (extra?.toLong() ?: 0)
            val now = System.currentTimeMillis() / 1000
            val diff = now - epoch

            return when {
                diff <= 60 -> "$diff ${context.resources.getString(R.string.minutes_ago)}"
                diff < 7 * 24 * 60 * 60 -> "${diff / (24 * 60 * 60)} ${context.resources.getString(R.string.days_ago)}"
                diff < 30 * 24 * 60 * 60 -> SimpleDateFormat("HH MMM", Locale.getDefault())
                    .format(Date(epoch * 1000))

                else -> SimpleDateFormat(
                    "HH MMM yyy", Locale.getDefault()
                ).format(Date(epoch * 1000))
            }
        } catch (e: Exception) {
            return ""
        }
    }

    fun type() = musicMediaType(type)
}


fun musicMediaType(type: String?): MusicDataTypes {
    return when (type) {
        "SONGS" -> MusicDataTypes.SONGS
        "PLAYLISTS" -> MusicDataTypes.PLAYLISTS
        "ALBUMS" -> MusicDataTypes.ALBUMS
        "VIDEOS" -> MusicDataTypes.VIDEOS
        "RADIO" -> MusicDataTypes.RADIO
        "ARTISTS" -> MusicDataTypes.ARTISTS
        "PODCAST_CATEGORIES" -> MusicDataTypes.PODCAST_CATEGORIES
        "PODCAST" -> MusicDataTypes.PODCAST
        "PODCAST_AUDIO" -> MusicDataTypes.PODCAST_AUDIO
        "NEWS" -> MusicDataTypes.NEWS
        "MOVIES_SHOW" -> MusicDataTypes.MOVIES_SHOW
        "AI_MUSIC" -> MusicDataTypes.AI_MUSIC
        "TEXT" -> MusicDataTypes.TEXT
        else -> MusicDataTypes.NONE
    }
}


