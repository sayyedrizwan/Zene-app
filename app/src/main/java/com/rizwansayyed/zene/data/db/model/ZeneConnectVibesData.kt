package com.rizwansayyed.zene.data.db.model


typealias ZeneConnectVibesResponse = List<ZeneConnectVibesItems>

data class ZeneConnectVibesItems(
    val to_number: String?,
    val from_number: String?,
    val timestamp: Long?,
    val image_path: String?,
    val songid: String?,
    val atists: String?,
    val name: String?,
    val type: String?,
    val thumbnail: String?,
)