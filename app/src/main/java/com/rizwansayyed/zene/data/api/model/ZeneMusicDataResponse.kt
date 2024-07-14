package com.rizwansayyed.zene.data.api.model


typealias ZeneMusicDataResponse = List<ZeneMusicDataItems>

data class ZeneMusicDataItems(
    val name: String?,
    val artists: String?,
    val id: String?,
    val thumbnail: String?,
    val extra: String?,
    val type: String?,
)