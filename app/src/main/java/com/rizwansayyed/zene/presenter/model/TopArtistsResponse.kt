package com.rizwansayyed.zene.presenter.model

import com.squareup.moshi.JsonClass

typealias TopArtistsResponseApi = List<TopArtistsSongs>

@JsonClass(generateAdapter = true)
data class TopArtistsSongs(
    val name: String?,
    val img: String?,
    val artist: String?,
)


@JsonClass(generateAdapter = true)
data class TopArtistsSongsWithData(
    val title: String?,
    val details: List<TopArtistsSongs>?,
)