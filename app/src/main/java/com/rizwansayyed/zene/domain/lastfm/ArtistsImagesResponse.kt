package com.rizwansayyed.zene.domain.lastfm


typealias ArtistsImagesResponse = Map<String, ImageResponseItems>

data class ImageResponseItems(
    val details: String?,
    val heading: String?,
    val href: String?,
    val image_title: String?,
    val index: Int?,
    val is_preferred: Boolean?,
    val next: String?,
    val previous: String?,
    val shoutbox_url: String?,
    val src: String?,
    val thumbnails: String?,
    val title: String?
)