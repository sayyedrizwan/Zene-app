package com.rizwansayyed.zene.domain.download

data class SaveFromVideoResponse(
    val duration: String?,
    val medias: List<Media?>?,
    val sid: Any?,
    val source: String?,
    val thumbnail: String?,
    val title: String?,
    val url: String?
) {
    data class Media(
        val audioAvailable: Boolean?,
        val cached: Boolean?,
        val chunked: Boolean?,
        val extension: String?,
        val formattedSize: String?,
        val quality: String?,
        val size: Int?,
        val url: String?,
        val videoAvailable: Boolean?
    )
}