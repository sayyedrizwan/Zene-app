package com.rizwansayyed.zene.data.api.model

import android.util.Log
import com.rizwansayyed.zene.data.db.DataStoreManager.isZeneConnectUsedOnOtherDeviceDB
import com.rizwansayyed.zene.data.db.model.UserInfoData
import com.rizwansayyed.zene.utils.Utils.uniqueDeviceUID
import kotlinx.coroutines.flow.flowOf

data class ZeneUsersPremiumResponse(val isPremium: Boolean?)

data class ZeneUsersResponse(
    val country: String?,
    val device_info: String?,
    val email: String?,
    val fcm_token: String?,
    val ip: String?,
    val is_review_done: Int?,
    val phone_number: String?,
    val country_code: String?,
    val last_seen: Long?,
    val latest_songs: List<String?>?,
    val name: String?,
    val zene_connect_device: String?,
    val pinned_artists: List<String?>?,
    val profile_photo: String?,
    val sign_up_date: Long?,
    val total_playtime: Int?,
    val song_name: String,
    val song_artists: String,
    val song_id: String,
    val song_thumbnail: String,
) {
    private fun isReviewDone(): Boolean {
        return is_review_done == 1
    }


    fun toUserInfo(email: String?): UserInfoData {
        Log.d("TAG", "toUserInfo: get Users ${uniqueDeviceUID()} $zene_connect_device")
        val number = if (uniqueDeviceUID() == zene_connect_device) phone_number
        else null

        isZeneConnectUsedOnOtherDeviceDB = flowOf((zene_connect_device?.length ?: 0) >= 6)

        return UserInfoData(
            name, email, total_playtime, number, profile_photo, isReviewDone()
        )
    }
}