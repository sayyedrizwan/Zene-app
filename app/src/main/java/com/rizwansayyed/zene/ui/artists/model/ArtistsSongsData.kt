package com.rizwansayyed.zene.ui.artists.model

data class ArtistsSongsData(
    val name: String,
    val image: String,
    val listeners: String
)

data class ArtistsAlbumsData(
    val name: String,
    val image: String,
    val listeners: String,
    val songsSize: String,
    val date: String
)