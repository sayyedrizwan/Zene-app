package com.rizwansayyed.zene.data.api.model


typealias ZeneMusicHistoryResponse = List<ZeneMusicHistoryItem>

data class ZeneMusicHistoryItem(
    val _id: String?,
    val email: String?,
    val name: String?,
    val artists: String?,
    val id: String?,
    val thumbnail: String?,
    val deviceInfo: String?,
    val deviceType: String?,
    val timestamp: Long?,
    val timesItsPlayed: Long?,
    val type: String?,
) {
    fun asMusicData(): ZeneMusicDataItems {
        return ZeneMusicDataItems(name, artists, id, thumbnail, "", type)
    }
}