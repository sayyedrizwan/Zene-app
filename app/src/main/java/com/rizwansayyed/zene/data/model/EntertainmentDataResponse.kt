package com.rizwansayyed.zene.data.model

data class EntertainmentDataResponse(
    val topNews: List<ZeneMusicData?>?,
    val latestNews: List<ZeneMusicData?>?,
    val newsArticles: List<ZeneMusicData?>?,
    val trailers: List<ZeneMusicData?>?
)