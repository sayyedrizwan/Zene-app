package com.rizwansayyed.zene.data.db.impl

import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import kotlinx.coroutines.flow.Flow

interface RoomDBInterface {

   suspend fun recentSixPlayed(): Flow<List<RecentPlayedEntity>>
}