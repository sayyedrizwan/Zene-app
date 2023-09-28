package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.data.utils.moshi


fun List<MusicData>.toTxtCache(): String? {
    val r = MusicDataCache(System.currentTimeMillis(), this)
    return moshi.adapter(r.javaClass).toJson(r)
}


fun TopSuggestMusicData.toTxtCache(): String? {
    return moshi.adapter(TopSuggestMusicData::class.java).toJson(this)
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
)


data class TopSuggestMusicData(
    val cacheTime: Long,
    val pList: List<String>,
    val list: List<MusicData>
)