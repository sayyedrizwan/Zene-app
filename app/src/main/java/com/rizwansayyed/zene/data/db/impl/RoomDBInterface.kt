package com.rizwansayyed.zene.data.db.impl

import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import kotlinx.coroutines.flow.Flow

interface RoomDBInterface {

   suspend fun recentSixPlayed(): Flow<Flow<List<RecentPlayedEntity>>>

   suspend fun insert(v: RecentPlayedEntity): Flow<Unit>
    suspend fun offlineDownloadedSongs(): Flow<Flow<List<OfflineDownloadedEntity>>>
    suspend fun insert(v: OfflineDownloadedEntity): Flow<Unit>
}