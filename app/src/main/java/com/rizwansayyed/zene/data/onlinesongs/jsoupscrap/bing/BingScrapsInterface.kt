package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing

import kotlinx.coroutines.flow.Flow

interface BingScrapsInterface {
    suspend fun bingOfficialVideo(a: String): Flow<String>
}