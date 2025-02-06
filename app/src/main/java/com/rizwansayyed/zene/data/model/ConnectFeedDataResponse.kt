package com.rizwansayyed.zene.data.model

data class ConnectFeedDataResponse(
    var media: String? = null,
    var caption: String? = null,
    var isVibing: Boolean? = null,
    var jazz_name: String? = null,
    var jazz_artists: String? = null,
    var jazz_id: String? = null,
    var jazz_thumbnail: String? = null,
    var jazz_type: String? = null,
    var location_name: String? = null,
    var location_address: String? = null,
    var longitude: String? = null,
    var latitude: String? = null,
    var emoji: String? = null,
    var userDetails: ConnectUserResponse? = null,
) {
    fun getMusicData(): ZeneMusicData {
        return ZeneMusicData(jazz_artists, jazz_id, jazz_name, "", jazz_thumbnail, jazz_type, "")
    }

    fun isMediaVideo(): Boolean {
        val videoExtensions = listOf("mp4", "mkv", "avi", "mov", "wmv", "flv", "webm")
        return videoExtensions.any { media?.endsWith(".$it", ignoreCase = true) == true }
    }
}