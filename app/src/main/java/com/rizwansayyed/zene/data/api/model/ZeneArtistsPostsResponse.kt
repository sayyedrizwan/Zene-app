package com.rizwansayyed.zene.data.api.model

data class ZeneArtistsPostsResponse(
    val artists: ZeneMusicDataItems?,
    val posts: List<ZeneArtistsPostItems?>?
)

data class ZeneArtistsPostItems(
    val caption: String?,
    val media: List<String?>?,
    val name: String?,
    val profileImg: String?,
    val thumbnail: String?,
    val timestamp: Long?,
    val type: String?,
    val url: String?,
    val username: String?
)