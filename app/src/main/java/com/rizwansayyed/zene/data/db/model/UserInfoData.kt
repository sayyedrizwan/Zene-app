package com.rizwansayyed.zene.data.db.model

import com.rizwansayyed.zene.utils.Utils.userAlphabetsImg

data class UserInfoData(
    var name: String?,
    var email: String?,
    var totalPlayTime: Int?,
    var phonenumber: String?,
    private var profilePhoto: String?,
    var isReviewDone: Boolean?
) {
    fun isLoggedIn(): Boolean {
        if (email?.contains("@") == false) return false
        if ((email?.length ?: 0) <= 3) return false

        return true
    }

    fun getProfilePicture(): String {
        if (profilePhoto == null) return userAlphabetsImg(name ?: "Zene User")
        if (profilePhoto!!.length <= 3) return userAlphabetsImg(name ?: "Zene User")
        return profilePhoto ?: userAlphabetsImg(name ?: "Zene User")
    }

    fun totalPlaytime(): String {
        val hours = (totalPlayTime ?: 0) / 60
        val remainingMinutes = (totalPlayTime ?: 0) % 60
        return "${hours}h ${remainingMinutes}m"
    }

}
