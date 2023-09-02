package com.rizwansayyed.zene.presenter.model

import com.squareup.moshi.JsonClass

typealias TopArtistsResponseApi = List<TopArtistsSongs>

data class TopArtistsSongsResponse(
    val data: List<TopArtistsSongs>,
    val apiResponse: ApiResponse
)


enum class ApiResponse(val s: Int) {
    SUCCESS(0), LOADING(1), ERROR(2)
}

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