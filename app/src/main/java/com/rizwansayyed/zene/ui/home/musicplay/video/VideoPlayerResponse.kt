package com.rizwansayyed.zene.ui.home.musicplay.video

import com.rizwansayyed.zene.presenter.model.VideoDataDownloader

data class VideoPlayerResponse(
    val status: VideoPlayerStatus = VideoPlayerStatus.LOADING,
    val data: VideoDataDownloader? = null
)

enum class VideoPlayerStatus(val i: Int) {
    LOADING(0), ERROR(1), SUCCESS(1),
}