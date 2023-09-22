package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap

import com.rizwansayyed.zene.domain.TopArtistsResult
import kotlinx.coroutines.flow.Flow

interface JsoupScrapInterface {

    suspend fun searchEngineData(name: String): Flow<Pair<String, String>>
    suspend fun topArtistsOfWeeks(): Flow<MutableList<TopArtistsResult>>
}