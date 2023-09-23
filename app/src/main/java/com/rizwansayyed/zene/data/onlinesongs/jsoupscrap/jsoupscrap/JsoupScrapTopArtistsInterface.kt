package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupscrap

import com.rizwansayyed.zene.domain.TopArtistsResult
import kotlinx.coroutines.flow.Flow

interface JsoupScrapTopArtistsInterface {

    suspend fun searchEngineData(name: String): Flow<Pair<String, String>>

    suspend fun topArtistsOfWeeks(): Flow<MutableList<TopArtistsResult>>
}