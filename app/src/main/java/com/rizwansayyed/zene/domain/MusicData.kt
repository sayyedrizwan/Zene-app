package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.data.utils.moshi


fun List<MusicData>.toTxtCache(): String? {
    val r = MusicDataCache(System.currentTimeMillis(), this)
    return moshi.adapter(r.javaClass).toJson(r)
}

data class MusicDataCache(
    val cacheTime: Long,
    val list: List<MusicData>
)

data class MusicData(
    val thumbnail: String?,
    val name: String?,
    val artists: String?,
    val pId: String?,
)
