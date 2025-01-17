package com.rizwansayyed.zene.data.model

data class VideoDataResponse(
    val trendingMusic: List<ZeneMusicData?>?,
    val trendingGaming: List<ZeneMusicData?>?,
    val trendingFilm: List<ZeneMusicData?>?,
    val suggestions: List<VideoDataRecommendedResponse>?,
    val forYou: List<ZeneMusicData?>?,
    val recommended: List<ZeneMusicData?>?
)

data class VideoDataRecommendedResponse(
    val name: String?,
    val videos: List<ZeneMusicData?>?,
)