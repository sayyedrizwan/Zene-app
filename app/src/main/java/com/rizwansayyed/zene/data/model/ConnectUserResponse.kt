package com.rizwansayyed.zene.data.model

import com.rizwansayyed.zene.datastore.DataStorageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

data class ConnectUserInfoResponse(
    val songDetails: ZeneMusicData?,
    var didRequestToYou: Boolean?,
    val myStatus: Status?,
    val otherStatus: Status?,
    val topSongs: List<ZeneMusicData>?,
    val user: ConnectUserResponse?,
    val unReadMessages: Int?,
    val vibes: List<ConnectFeedDataResponse?>?,
) {
    data class Status(
        var isConnected: Boolean?,
        var lastListeningSong: Boolean?,
        var locationSharing: Boolean?,
        var silentNotification: Boolean?
    )

    fun isConnected(): ConnectedUserStatus = runBlocking(Dispatchers.IO) {
        val userEmail = DataStorageManager.userInfo.firstOrNull()
        if (user?.email == userEmail?.email) return@runBlocking ConnectedUserStatus.ME
        if (myStatus?.isConnected == null) return@runBlocking ConnectedUserStatus.NONE
        return@runBlocking if (myStatus.isConnected == true) ConnectedUserStatus.FRIENDS else ConnectedUserStatus.REQUESTED
    }
}

enum class ConnectedUserStatus {
    FRIENDS, REQUESTED, NONE, ME
}

data class ConnectUserResponse(
    val country: String?,
    val email: String?,
    val connect_status: String?,
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