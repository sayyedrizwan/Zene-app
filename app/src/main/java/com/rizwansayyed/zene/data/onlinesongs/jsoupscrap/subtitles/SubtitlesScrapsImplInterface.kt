package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.subtitles

import com.rizwansayyed.zene.domain.MusicPlayerList
import com.rizwansayyed.zene.domain.subtitles.GeniusLyricsWithInfo
import kotlinx.coroutines.flow.Flow


interface SubtitlesScrapsImplInterface {

    suspend fun searchSubtitles(data: MusicPlayerList): Flow<GeniusLyricsWithInfo>
    suspend fun searchSubtitlesOnGenius(data: MusicPlayerList): Flow<GeniusLyricsWithInfo>
}