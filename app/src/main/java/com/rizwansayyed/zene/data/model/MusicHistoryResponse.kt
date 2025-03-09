package com.rizwansayyed.zene.data.model

data class MusicHistoryResponse(
    val id: String?,
    val name: String?,
    val artists: String?,
    val deviceInfo: String?,
    val thumbnail: String?,
    val type: String?
) {
    fun asMusicData() = ZeneMusicData(artists, id, name, "", thumbnail, type)
    fun type() = musicMediaType(type)
}