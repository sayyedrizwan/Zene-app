package com.rizwansayyed.zene.data.db.model

import org.json.JSONObject


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
}
