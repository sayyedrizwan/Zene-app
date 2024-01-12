package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.youtubescrap


import kotlinx.coroutines.flow.Flow

interface YoutubeScrapInterface {

    suspend fun ytReleaseItems(songId: String): Flow<MutableList<String>>
    suspend fun getChannelId(username: String): Flow<String>
}