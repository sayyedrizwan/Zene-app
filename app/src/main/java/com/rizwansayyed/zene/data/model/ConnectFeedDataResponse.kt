package com.rizwansayyed.zene.data.model

data class ConnectFeedDataResponse(
    var media: String? = null,
    var isVibing: Boolean? = null,
    var jazzName: String? = null,
    var jazzId: String? = null,
    var jazzThumbnail: String? = null,
    var emoji: String? = null
) {

    fun isMediaVideo(): Boolean {
        val videoExtensions = listOf("mp4", "mkv", "avi", "mov", "wmv", "flv", "webm")
        return videoExtensions.any { media?.endsWith(".$it", ignoreCase = true) == true }
    }
}