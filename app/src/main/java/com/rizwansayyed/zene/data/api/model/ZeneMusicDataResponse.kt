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

data class ZeneLyricsData(
    val isSync: Boolean?, val lyrics: String?
)

data class ZeneVideosMusicData(
    val lyricsVideoID: String?, val officialVideoID: String?
)

data class ZenePlaylistAlbumsData(
    val info: ZeneMusicDataItems?,
    val songs: List<ZeneMusicDataItems>,
    val isAdded: Boolean?,
    val ownerEmail: String?,
)


data class ZeneMoodPlaylistData(
    val name: String?, val list: List<MoodLists>
)

data class MoodLists(
    val name: String?, val list: List<ZeneMusicDataItems>
)

typealias ZeneMusicImportPlaylistsDataResponse = List<ZeneMusicImportPlaylistsItems>


data class ZeneMusicImportPlaylistsItems(
    val name: String?,
    val artists: String?,
    val id: String?,
    val thumbnail: String?,
    val desc: String?,
    val path: String?,
    val next: String?,
    private val type: String?
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
            "STORE" -> MusicType.STORE
            "NEWS" -> MusicType.NEWS
            "RADIO" -> MusicType.RADIO
            "OFFLINE_SONGS" -> MusicType.OFFLINE_SONGS
            "RADIO_LANG" -> MusicType.RADIO_LANGUAGE
            else -> MusicType.NONE
        }
    }

    fun getDomain(): String {
        try {
            val regex = Regex("(?:https?://)?(?:www\\.)?([^/]+)")
            val matchResult = regex.find(id ?: "")
            return matchResult?.groups?.get(1)?.value ?: ""
        } catch (e: Exception) {
            return ""
        }
    }
}


enum class MusicType {
    SONGS, PLAYLIST, ALBUMS, ARTISTS, VIDEO, MOOD, STORE, NEWS, RADIO, OFFLINE_SONGS, RADIO_LANGUAGE, NONE
}