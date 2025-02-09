package com.rizwansayyed.zene.data.model

data class VibesCommentsResponse(
    val id: Int?,
    val gif: String?,
    val vibes_id: String?,
    val email: String?,
    val timestamp: Long?,
    val name: String?,
    val profile_photo: String?
) {
    fun ts(): String {
        timestamp ?: return ""
        val currentTimestamp = System.currentTimeMillis()
        val diffMillis: Long = currentTimestamp - timestamp
        val seconds = diffMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60

        return if (seconds < 60) seconds.toString() + "s ago"
        else if (minutes < 60) minutes.toString() + "m ago"
        else hours.toString() + "h ago"
    }

}