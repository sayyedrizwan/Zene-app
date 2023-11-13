package com.rizwansayyed.zene.domain

data class ArtistsEvents(
    val eventName: String?,
    val time: String?,
    val address: String?,
    val link: String?,
    val artists: List<ArtistsArtists>,
)


data class ArtistsArtists(val name: String, val img: String)

