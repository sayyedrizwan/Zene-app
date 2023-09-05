package com.rizwansayyed.zene.domain.model

data class YTMusicResponse(
    val result: Result?
) {
    data class Result(
        val audios: List<Audio?>?,
        val author: String?,
        val duration: String?,
        val id: String?,
        val image: String?,
        val mp3_task_data: Mp3TaskData?,
        val source: String?,
        val title: String?,
        val videos: List<Video?>?
    ) {
        data class Audio(
            val cdn_url: String?,
            val filesize: String?,
            val type: String?,
            val url: String?
        )

        data class Mp3TaskData(
            val download_url: String?,
            val duration: String?,
            val gid: String?,
            val quality: Int?,
            val status: Int?,
            val tid: String?,
            val vid: String?
        )

        data class Video(
            val cdn_url: String?,
            val filesize: Any?,
            val quality: String?,
            val type: String?,
            val url: String?
        )
    }
}