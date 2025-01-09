package com.rizwansayyed.zene.data.model

data class EntertainmentDataResponse(
    val topNews: List<ZeneMusicData?>?,
    val latestNews: List<ZeneMusicData?>?,
    val newsArticles: EntertainmentNewsArticlesResponse?,
    val trailers: List<ZeneMusicData?>?
)

data class EntertainmentNewsArticlesResponse(
    val hollywood: List<ZeneMusicData?>?,
    val bollywood: List<ZeneMusicData?>?,
    val music: List<ZeneMusicData?>?
)