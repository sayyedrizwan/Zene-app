package com.rizwansayyed.zene.domain.roomdb

import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.domain.roomdb.recentplayed.RecentPlayedEntity
import com.rizwansayyed.zene.presenter.model.TopArtistsSongsWithData
import kotlinx.coroutines.flow.Flow

interface RoomDBImplInterface {

    suspend fun recentPlayed(): Flow<Flow<List<RecentPlayedEntity>>>

    suspend fun insert(recentPlay: RecentPlayedEntity): Flow<Unit>
    suspend fun songsSuggestionsUsingSongsHistory(): Flow<ArrayList<TopArtistsSongs>>

    suspend fun songSuggestionsForYouUsingHistory(): Flow<ArrayList<TopArtistsSongs>>

    suspend fun artistsSuggestionsForYouUsingHistory(): Flow<ArrayList<TopArtistsSongs>>

    suspend fun topArtistsSuggestions(): Flow<List<RecentPlayedEntity>>

    suspend fun topArtistsSongs(): Flow<ArrayList<TopArtistsSongsWithData>>
}