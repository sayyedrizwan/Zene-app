package com.rizwansayyed.zene.ui.musicplay.video

data class VideoPlayerResponse(
    val status: VideoPlayerStatus = VideoPlayerStatus.LOADING,
    val data: String? = null
)

enum class VideoPlayerStatus(val i: Int) {
    LOADING(0), ERROR(1), SUCCESS(1),
}

enum class VideoPlayerViewStatus(val i: Int) {
    MUSIC(0), LYRICS(1), INFO(2),
}