package com.rizwansayyed.zene.domain

data class LoginUserData(
    val name: String?, val email: String?, val image: String?,
) {
    fun isLogin(): Boolean {
        return ((email?.length ?: 0) > 3) && email?.contains("@") == true
    }
}