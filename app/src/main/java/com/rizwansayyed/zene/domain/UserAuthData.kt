package com.rizwansayyed.zene.domain

data class UserAuthData(
    val name: String?,
    val email: String = "",
    val photo: String?,
    val linkId: String?,
    val currentLinkedWeb: CurrentLinkedWeb?,
    val socketUsername: String?,
) {

    fun isLoggedIn(): Boolean {
        return email.trim().contains("@") && email.trim().length > 3
    }

    data class CurrentLinkedWeb(
        val browser: String,
        val os: String,
        val lastAccessTime: String,
        val ipAddress: String,
        val desktopFCM: String
    )
}