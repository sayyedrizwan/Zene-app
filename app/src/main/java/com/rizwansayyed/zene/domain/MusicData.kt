package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.data.utils.moshi


fun List<MusicData>.toTxtCache(): String? {
    val r = MusicDataCache(System.currentTimeMillis(), this)
    return moshi.adapter(r.javaClass).toJson(r)
}


fun List<MusicDataWithArtists>.toCache(): String? {
    val r = MusicDataWithArtistsCache(System.currentTimeMillis(), this)
    return moshi.adapter(r.javaClass).toJson(r)
}


fun TopSuggestMusicData.toTxtCache(): String? {
    return moshi.adapter(TopSuggestMusicData::class.java).toJson(this)
}

fun ArtistsFanDataCache.toTxtCache(): String? {
    return moshi.adapter(ArtistsFanDataCache::class.java).toJson(this)
}

fun SongsSuggestionsData.toTxtCache(): String? {
    return moshi.adapter(SongsSuggestionsData::class.java).toJson(this)
}

data class MusicDataCache(
    val cacheTime: Long,
    val list: List<MusicData>
)

data class MusicData(
    val thumbnail: String?,
    var name: String?,
    var artists: String?,
    var pId: String?,
    var type: MusicType?,
)

data class MusicDataWithArtistsCache(
    val cacheTime: Long,
    val list: List<MusicDataWithArtists>
)


data class MusicDataWithArtists(
    val thumbnail: String?,
    var name: String?,
    var artists: String?,
    var listeners: String?,
    var artistsImg: String?,
    var artistsName: String?,
    var pId: String?,
    var type: MusicType?,
)


data class TopSuggestMusicData(
    val cacheTime: Long,
    val pList: List<String>,
    val list: List<MusicData>
)

data class ArtistsFanDataCache(
    val aList: List<String>,
    val list: List<ArtistsFanData>
)

data class ArtistsFanData(
    val artistsName: String,
    val artistsImg: String,
    val list: List<MusicData>
)

data class SearchData(
    val songs: List<MusicData>,
    val albums: List<MusicData>,
    val artists: List<MusicData>
)

data class SongsSuggestionsData(
    val cacheSId: List<String>,
    val next: List<MusicData>,
    val related: List<MusicData>,
    val artists: List<MusicData>,
)

enum class MusicType {
    MUSIC, ALBUMS, ARTISTS, PLAYLIST, TEXT, RADIO
}