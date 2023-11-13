package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.songkick

import com.rizwansayyed.zene.domain.ArtistsEvents
import kotlinx.coroutines.flow.Flow


interface SongKickScrapsImplInterface {
    suspend fun artistsEvents(a: String): Flow<ArrayList<ArtistsEvents>>
}