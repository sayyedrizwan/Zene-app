package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.topartistsplaylists

import com.rizwansayyed.zene.domain.MusicData
import kotlinx.coroutines.flow.Flow

interface TopArtistsPlaylistsScrapsInterface {

    suspend fun topArtistsOfWeeks(): Flow<MutableList<MusicData>>
}