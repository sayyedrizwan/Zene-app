package com.rizwansayyed.zene.data.db.model

data class UserInfoData(
    var name: String?,
    var email: String?,
    var totalPlayTime: Int?,
    var profilePhoto: String?,
    var isReviewDone: Boolean?,
    var subscriptionStatus: String?,
    var subscriptionExpiryData: String?,
) {
    fun isLoggedIn(): Boolean {
        if (email?.contains("@") == false) return false
        if ((email?.length ?: 0) <= 3) return false

        return true
    }

    fun totalPlaytime(): String {
        val hours = (totalPlayTime ?: 0) / 60
        val remainingMinutes = (totalPlayTime ?: 0) % 60
        return "${hours}h ${remainingMinutes}m"
    }

}
