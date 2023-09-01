package com.rizwansayyed.zene.presenter.model

data class AlbumsHeadersResponse(
    val albums: List<MusicsAlbum>?,
    val header: List<MusicsHeader>?
)


data class MusicsHeader(
    val name: String?,
    val thumbnail: String?,
    val artists: String?
)


data class MusicsAlbum(
    val headline: String?,
    val item: List<MusicAlbumsItem?>?
)


data class MusicAlbumsItem(
    val name: String?,
    val desc: String?,
    val id: String?,
    val thumbnail: String?
)