package com.rizwansayyed.zene.data.db.impl

import com.rizwansayyed.zene.data.db.offlinedownload.OfflineDownloadedEntity
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.data.db.savedplaylist.playlist.SavedPlaylistEntity
import kotlinx.coroutines.flow.Flow

interface RoomDBInterface {

    suspend fun recentSixPlayed(): Flow<Flow<List<RecentPlayedEntity>>>

    suspend fun insert(v: RecentPlayedEntity): Flow<Unit>

    suspend fun offlineDownloadedSongs(): Flow<Flow<List<OfflineDownloadedEntity>>>

    suspend fun insert(v: OfflineDownloadedEntity): Flow<Unit>

    suspend fun savedPlaylists(): Flow<Flow<List<SavedPlaylistEntity>>>

    suspend fun insert(v: SavedPlaylistEntity): Flow<Unit>

    suspend fun topTenList(): Flow<List<RecentPlayedEntity>>
}