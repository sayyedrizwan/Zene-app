package com.rizwansayyed.zene.data.model

data class UserInfoResponse(
    val email: String?,
    val name: String?,
    val photo: String?,
    val fcm_token: String?,
    val ip: String?,
    val device: String?,
    val country: String?,
)