package com.rizwansayyed.zene.db.model

data class UserInfoData(
    var id: Int?,
    var name: String?,
    var email: String?,
    var totalPlayTime: Int?,
    var profilePhoto: Int?,
    var isReviewDone: Boolean?,
    var subscriptionStatus: Boolean?,
    var subscriptionExpiryData: Long?,
) {
    fun isLoggedIn(): Boolean {
        if (email?.contains("@") == false) return false
        if ((email?.length ?: 0) <= 3) return false

        return true
    }
}
