package com.rizwansayyed.zene.data.model

data class ConnectChatMessageResponse(
    val _id: String?,
    val from: String?,
    val to: String?,
    val did_read: Boolean?,
    val message: String?,
    val timestamp: Long?,
    val candid_media: String? = null,
    val candid_thumbnail: String? = null,
    val gif: String? = null,
    val jam_name: String? = null,
    val jam_artists: String? = null,
    val jam_type: String? = null,
    val jam_thumbnail: String? = null,
    val jam_id: String? = null,
) {
    fun getMusicData(): ZeneMusicData? {
        if (jam_name == null || jam_id == null) return null
        return ZeneMusicData(jam_artists, jam_id, jam_name, "", jam_thumbnail, jam_type, "")
    }
}