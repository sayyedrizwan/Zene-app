package com.rizwansayyed.zene.data.model

import java.util.Locale

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
    val file_path: String? = null,
    val file_name: String? = null,
    val file_size: String? = null,
) {
    fun getMusicData(): ZeneMusicData? {
        if (jam_name == null || jam_id == null) return null
        return ZeneMusicData(jam_artists, jam_id, jam_name, "", jam_thumbnail, jam_type, "")
    }

    fun fileSize(): String {
        val fileSize = file_size?.toLongOrNull() ?: return ""
        val kb = 1024.0
        val mb = kb * 1024
        val gb = mb * 1024

        return when {
            fileSize >= gb -> String.format(Locale.getDefault(), "%.2f GB", fileSize / gb)
            fileSize >= mb -> String.format(Locale.getDefault(), "%.2f MB", fileSize / mb)
            fileSize >= kb -> String.format(Locale.getDefault(), "%.2f KB", fileSize / kb)
            else -> "$fileSize Bytes"
        }
    }
}