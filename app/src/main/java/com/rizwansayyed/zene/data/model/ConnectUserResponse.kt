package com.rizwansayyed.zene.data.model

data class ConnectUserResponse(
    val email: String?,
    val name: String?,
    val profile_photo: String?,
    val last_seen: String?,
    val connect_status: String?
)