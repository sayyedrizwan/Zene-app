package com.rizwansayyed.zene.roomdb

import com.rizwansayyed.zene.roomdb.recentplayed.RecentPlayedEntity
import kotlinx.coroutines.flow.Flow

interface RoomDBImplInterface {
    suspend fun recentPlayed(): Flow<Flow<List<RecentPlayedEntity>>>
    suspend fun insert(recentPlay: RecentPlayedEntity): Flow<Unit>
}