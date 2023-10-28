package com.rizwansayyed.zene.domain

data class SongDownloadResponse(
    val audioStreams: List<AudioStream?>?,
    val category: String?,
    val chapters: List<Any?>?,
    val dash: Any?,
    val description: String?,
    val dislikes: Int?,
    val duration: Int?,
    val hls: String?,
    val lbryId: Any?,
    val license: String?,
    val likes: Int?,
    val livestream: Boolean?,
    val metaInfo: List<Any?>?,
    val previewFrames: List<PreviewFrame?>?,
    val proxyUrl: String?,
    val relatedStreams: List<RelatedStream?>?,
    val subtitles: List<Any?>?,
    val tags: List<String?>?,
    val thumbnailUrl: String?,
    val title: String?,
    val uploadDate: String?,
    val uploader: String?,
    val uploaderAvatar: String?,
    val uploaderSubscriberCount: Int?,
    val uploaderUrl: String?,
    val uploaderVerified: Boolean?,
    val videoStreams: List<VideoStream?>?,
    val views: Int?,
    val visibility: String?
) {
    data class AudioStream(
        val audioTrackId: Any?,
        val audioTrackLocale: Any?,
        val audioTrackName: Any?,
        val audioTrackType: Any?,
        val bitrate: Int?,
        val codec: String?,
        val contentLength: Int?,
        val format: String?,
        val fps: Int?,
        val height: Int?,
        val indexEnd: Int?,
        val indexStart: Int?,
        val initEnd: Int?,
        val initStart: Int?,
        val itag: Int?,
        val mimeType: String?,
        val quality: String?,
        val url: String?,
        val videoOnly: Boolean?,
        val width: Int?
    )

    data class PreviewFrame(
        val durationPerFrame: Int?,
        val frameHeight: Int?,
        val frameWidth: Int?,
        val framesPerPageX: Int?,
        val framesPerPageY: Int?,
        val totalCount: Int?,
        val urls: List<String?>?
    )

    data class RelatedStream(
        val duration: Int?,
        val isShort: Boolean?,
        val shortDescription: Any?,
        val thumbnail: String?,
        val title: String?,
        val type: String?,
        val uploaded: Long?,
        val uploadedDate: String?,
        val uploaderAvatar: String?,
        val uploaderName: String?,
        val uploaderUrl: String?,
        val uploaderVerified: Boolean?,
        val url: String?,
        val views: Int?
    )

    data class VideoStream(
        val audioTrackId: Any?,
        val audioTrackLocale: Any?,
        val audioTrackName: Any?,
        val audioTrackType: Any?,
        val bitrate: Int?,
        val codec: String?,
        val contentLength: Int?,
        val format: String?,
        val fps: Int?,
        val height: Int?,
        val indexEnd: Int?,
        val indexStart: Int?,
        val initEnd: Int?,
        val initStart: Int?,
        val itag: Int?,
        val mimeType: String?,
        val quality: String?,
        val url: String?,
        val videoOnly: Boolean?,
        val width: Int?
    )
}