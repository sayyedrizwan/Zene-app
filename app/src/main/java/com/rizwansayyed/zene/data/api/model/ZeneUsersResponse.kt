package com.rizwansayyed.zene.data.api.model

import com.rizwansayyed.zene.data.db.model.UserInfoData

data class ZeneUsersResponse(
    val country: String?,
    val device_info: String?,
    val email: String?,
    val fcm_token: String?,
    val ip: String?,
    val is_review_done: Int?,
    val last_seen: Long?,
    val latest_songs: List<String?>?,
    val name: String?,
    val pinned_artists: List<String?>?,
    val profile_photo: String?,
    val sign_up_date: Long?,
    val subscription_expiry_date: Long?,
    val subscription_status: String?,
    val total_playtime: Int?
) {
    private fun isReviewDone(): Boolean {
        return is_review_done == 1
    }


    fun toUserInfo(email: String?): UserInfoData {
        return UserInfoData(
            name, email, total_playtime, profile_photo,
            isReviewDone(), subscription_status, subscription_status
        )
    }
}