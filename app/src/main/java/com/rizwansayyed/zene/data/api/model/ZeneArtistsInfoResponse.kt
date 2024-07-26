package com.rizwansayyed.zene.data.api.model

data class ZeneArtistsInfoResponse(
    val desc: String?,
    val followers: Int?,
    val img: List<String?>?,
    val name: String?,
    val socialMedia: List<SocialMedia?>?,
    val topSongs: List<ZeneMusicDataItems?>?
) {
    data class SocialMedia(
        val network: String?,
        val title: String?,
        val url: String?,
        val username: String?
    )
}


data class ZeneArtistsDataResponse(
    val songs: List<ZeneMusicDataItems>,
    val albums: List<ZeneMusicDataItems>,
    val videos: List<ZeneMusicDataItems>,
    val playlists: List<ZeneMusicDataItems>,
    val artists: List<ZeneMusicDataItems>,
    val news: List<ZeneMusicDataItems?>?
)