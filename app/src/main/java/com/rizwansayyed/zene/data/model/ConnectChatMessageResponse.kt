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
)