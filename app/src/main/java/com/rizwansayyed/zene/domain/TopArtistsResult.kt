package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.data.utils.moshi

data class TopArtistsCacheResponse(
    val cacheTime: Long,
    val list: List<TopArtistsResult>
)

fun MutableList<TopArtistsResult>.toTxtCache(): String? {
    val r = TopArtistsCacheResponse(System.currentTimeMillis(), this)
    return moshi.adapter(r.javaClass).toJson(r)
}

data class TopArtistsResult(
    val thumbnail: String,
    val name: String,
    val instagram: String,
    val twitter: String,
)
