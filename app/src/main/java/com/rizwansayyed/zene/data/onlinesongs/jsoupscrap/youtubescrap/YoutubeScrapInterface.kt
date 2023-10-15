package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.youtubescrap


import kotlinx.coroutines.flow.Flow

interface YoutubeScrapInterface {

    suspend fun ytReleaseItems(pId: String): Flow<MutableList<String>>
}