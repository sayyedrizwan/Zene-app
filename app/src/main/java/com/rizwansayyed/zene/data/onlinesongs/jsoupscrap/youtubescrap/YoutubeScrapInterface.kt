package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.youtubescrap


import com.rizwansayyed.zene.domain.yt.YoutubeLatestSearchVideoResponse
import com.rizwansayyed.zene.domain.yt.YoutubeReleaseChannelResponse
import kotlinx.coroutines.flow.Flow

interface YoutubeScrapInterface {

    suspend fun ytReleaseItems(pId: String): Flow<MutableList<String>>
    suspend fun ytThisYearArtistOfficialVideos(name: String): Flow<String>
}