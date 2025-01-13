package com.rizwansayyed.zene.data.model

data class UserInfoResponse(
    val email: String?,
    val name: String?,
    val photo: String?,
    val phoneNumber: String?,
    val authToken: String?,
) {
    fun isLoggedIn(): Boolean {
        if (email == null) return false
        if (email.contains("@") && email.length > 4) return true
        return false
    }
}