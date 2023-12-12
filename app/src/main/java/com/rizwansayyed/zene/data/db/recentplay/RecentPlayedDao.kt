package com.rizwansayyed.zene.data.db.recentplay

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.data.utils.DBNAME.RECENT_PLAYED_DB
import com.rizwansayyed.zene.utils.Utils.OFFSET_LIMIT
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentPlayedDao {
    @Query("SELECT * FROM $RECENT_PLAYED_DB WHERE songId = :songId ORDER BY timestamp DESC LIMIT 1")
    suspend fun search(songId: String): RecentPlayedEntity?

    @Query("SELECT * FROM $RECENT_PLAYED_DB ORDER BY timestamp DESC LIMIT :offset")
    fun recentListLive(offset: Int): Flow<List<RecentPlayedEntity>>

    @Query("SELECT * FROM $RECENT_PLAYED_DB ORDER BY timestamp DESC LIMIT :offset, $OFFSET_LIMIT")
    suspend fun recentList(offset: Int): List<RecentPlayedEntity>

    @Query("SELECT * FROM $RECENT_PLAYED_DB ORDER BY playTimes DESC LIMIT :offset")
    suspend fun read(offset: Int): List<RecentPlayedEntity>

    @Query("SELECT * FROM $RECENT_PLAYED_DB WHERE timestamp >= :timestamp ORDER BY playTimes DESC LIMIT :offset")
    suspend fun readWithTimestamp(offset: Int, timestamp: Long): List<RecentPlayedEntity>

    @Query("UPDATE $RECENT_PLAYED_DB SET lastListenDuration = :duration WHERE songId = :songId")
    suspend fun updateTime(songId: String, duration: Long): Int


    @Upsert
    suspend fun insert(v: RecentPlayedEntity)
}