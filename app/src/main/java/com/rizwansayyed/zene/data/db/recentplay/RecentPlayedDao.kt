package com.rizwansayyed.zene.data.db.recentplay

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.data.utils.DBNAME.RECENT_PLAYED_DB
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentPlayedDao {
    @Query("SELECT * FROM $RECENT_PLAYED_DB ORDER BY timestamp DESC")
    suspend fun list(): List<RecentPlayedEntity>


    @Query("SELECT * FROM $RECENT_PLAYED_DB ORDER BY timestamp DESC LIMIT 6")
    fun recentList(): Flow<List<RecentPlayedEntity>>


    @Upsert
    suspend fun insert(v: RecentPlayedEntity)
}