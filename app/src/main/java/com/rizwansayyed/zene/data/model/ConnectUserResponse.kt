package com.rizwansayyed.zene.data.model

data class ConnectUserResponse(
    val email: String?,
    val name: String?,
    val profile_photo: String?,
    val username: String?,
    val country: String?,
    val last_seen: String?,
    val status: String?,
    val connect_status: String?
)


data class ConnectUserInfoResponse(
    val user: ConnectUserResponse?, val status: ConnectUserStatusResponse?
)

data class ConnectUserStatusResponse(
    val email: String?, val to_email: String?, val timestamp: Long?, val is_connected: Int?
) {
    fun isConnected(): Boolean? {
        if (is_connected == null) return null
        return is_connected == 1
    }
}