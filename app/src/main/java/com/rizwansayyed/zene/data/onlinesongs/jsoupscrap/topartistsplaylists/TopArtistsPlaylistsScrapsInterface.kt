package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists

import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.yt.YoutubeReleaseChannelResponse
import kotlinx.coroutines.flow.Flow

interface TopArtistsPlaylistsScrapsInterface {

    suspend fun topArtistsOfWeeks(): Flow<MutableList<MusicData>>
    suspend fun searchEngineData(name: String): Flow<Pair<String, String>>
    suspend fun ytChannelJson(path: String): Flow<YoutubeReleaseChannelResponse?>
    suspend fun ytPlaylistItems(path: String): Flow<YoutubeReleaseChannelResponse?>
}