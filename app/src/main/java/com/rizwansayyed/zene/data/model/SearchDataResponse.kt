package com.rizwansayyed.zene.data.model

data class SearchDataResponse(
    val song: List<ZeneMusicData?>?,
    val videos: List<ZeneMusicData?>?,
    val news: List<ZeneMusicData?>?,
    val artists: List<ZeneMusicData?>?,
    val albums: List<ZeneMusicData?>?,
    val playlists: List<ZeneMusicData?>?,
    val podcast: List<ZeneMusicData?>?,
    val movies: List<ZeneMusicData?>?,
)

data class SearchPlacesDataResponse(
    val address: String?,
    val name: String?,
    val lat: String?,
    val lon: String?
)