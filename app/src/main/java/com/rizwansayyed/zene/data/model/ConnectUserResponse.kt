package com.rizwansayyed.zene.data.model


data class ConnectUserInfoResponse(
    val songDetails: ZeneMusicData?,
    var didRequestToYou: Boolean?,
    val status: Status?,
    val topSongs: List<ZeneMusicData>?,
    val user: ConnectUserResponse?,
    val message: UserMessage?,
) {
    data class Status(
        var isConnected: Boolean?,
        var lastListeningSong: Boolean?,
        var locationSharing: Boolean?,
        var silentNotification: Boolean?
    ) {
        fun isConnected(): ConnectedUserStatus {
            if (isConnected == null) return ConnectedUserStatus.NONE
            return if (isConnected == true) ConnectedUserStatus.FRIENDS else ConnectedUserStatus.REQUESTED
        }
    }
}

data class UserMessage(var fromCurrentUser: Boolean?, var message: String?)


enum class ConnectedUserStatus {
    FRIENDS, REQUESTED, NONE
}

data class ConnectUserResponse(
    val country: String?,
    val email: String?,
    val last_seen: Long?,
    val location: String?,
    val name: String?,
    val profile_photo: String?,
    val username: String?
) {
    fun isUserLocation(): Boolean {
        try {
            val lat = location?.substringBefore(",")?.trim()?.toDouble() ?: 0.0
            val lon = location?.substringAfter(",")?.trim()?.toDouble() ?: 0.0
            return lat > 0.0 && lon > 0.0
        } catch (e: Exception) {
            return false
        }
    }
}