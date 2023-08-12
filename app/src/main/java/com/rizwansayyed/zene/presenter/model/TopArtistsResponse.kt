package com.rizwansayyed.zene.presenter.model


typealias TopArtistsResponseApi = List<TopArtistsSongs>

data class TopArtistsSongs(
    val name: String?,
    val img: String?,
    val artist: String?,
)


data class TopArtistsSongsWithData(
    val title: String?,
    val details: List<TopArtistsSongs>?,
)