package com.rizwansayyed.zene.data.model

data class VideoDataResponse(
    val recommended: List<ZeneMusicData?>?,
    val forYou: List<ZeneMusicData?>?
)