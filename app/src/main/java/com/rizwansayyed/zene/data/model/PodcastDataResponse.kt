package com.rizwansayyed.zene.data.model

data class PodcastDataResponse(
    val history: List<ZeneMusicData?>?,
    val latest: List<ZeneMusicData?>?,
    val trending: List<ZeneMusicData?>?,
    val categories: List<ZeneMusicData?>?,
    val podcastYouMayLike: List<ZeneMusicData?>?,
    val explore: List<ZeneMusicData?>?
)