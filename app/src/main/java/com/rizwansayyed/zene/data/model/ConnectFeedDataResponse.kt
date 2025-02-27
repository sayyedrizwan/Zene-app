package com.rizwansayyed.zene.data.model

data class ConnectFeedDataResponse(
    var id: Int? = null,
    var email: String? = null,
    var media: String? = null,
    var caption: String? = null,
    var media_thubnail: String? = null,
    var is_vibing: Boolean? = null,
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
    var comments: Int? = null,
    var timestamp: Long? = null,
    var userDetails: ConnectUserResponse? = null,
) {
    fun getMusicData(): ZeneMusicData? {
        if (jazz_name == null || jazz_id == null) return null
        return ZeneMusicData(jazz_artists, jazz_id, jazz_name, "", jazz_thumbnail, jazz_type, "")
    }

    fun ts(): String {
        timestamp ?: return ""
        val currentTimestamp = System.currentTimeMillis()
        val diffMillis: Long = currentTimestamp - timestamp!!
        val seconds = diffMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return if (seconds < 60) seconds.toString() + "s ago"
        else if (minutes < 60) minutes.toString() + "m ago"
        else hours.toString() + "h ago"
    }

    fun isMediaVideo(): Boolean {
        val videoExtensions = listOf("mp4", "mkv", "avi", "mov", "wmv", "flv", "webm")
        return videoExtensions.any { media?.endsWith(".$it", ignoreCase = true) == true }
    }
}