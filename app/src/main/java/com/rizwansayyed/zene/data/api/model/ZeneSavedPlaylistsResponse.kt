package com.rizwansayyed.zene.data.api.model


typealias ZeneSavedPlaylistsResponse = List<ZeneSavedPlaylistsResponseItem>

data class ZeneSavedPlaylistsResponseItem(
    val _id: String?,
    val email: String?,
    val id: String?,
    val img: String?,
    val name: String?,
    val isSaved: Boolean?,
    val timestamp: Long?
)
