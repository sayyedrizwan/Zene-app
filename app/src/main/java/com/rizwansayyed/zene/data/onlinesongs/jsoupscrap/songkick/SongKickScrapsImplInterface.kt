package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.songkick

import kotlinx.coroutines.flow.Flow


interface SongKickScrapsImplInterface {
    suspend fun artistsEvents(a: String): Flow<String>
}