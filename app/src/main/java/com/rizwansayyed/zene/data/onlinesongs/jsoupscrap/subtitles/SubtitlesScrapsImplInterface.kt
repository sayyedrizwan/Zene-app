package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.subtitles

import com.rizwansayyed.zene.domain.ArtistsEvents
import com.rizwansayyed.zene.domain.subtitles.GeniusLyricsWithInfo
import kotlinx.coroutines.flow.Flow


interface SubtitlesScrapsImplInterface {

    suspend fun searchSubtitles(songName: String, artistName: String): Flow<GeniusLyricsWithInfo>

    suspend fun searchSubtitlesOnGenius(
        songName: String, artistName: String
    ): Flow<GeniusLyricsWithInfo>


}