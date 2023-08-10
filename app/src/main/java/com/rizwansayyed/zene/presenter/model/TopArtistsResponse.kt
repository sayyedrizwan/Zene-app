package com.rizwansayyed.zene.presenter.model


typealias TopArtistsResponseApi = List<TopArtists>

data class TopArtists(
    val name: String?,
    val img: String?,
)