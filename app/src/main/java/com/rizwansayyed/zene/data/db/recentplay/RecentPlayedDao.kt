package com.rizwansayyed.zene.data.db.recentplay

import androidx.room.Dao
import androidx.room.Query
import com.rizwansayyed.zene.data.db.utils.DbName.recent_played_db

@Dao
interface RecentPlayedDao {
    @Query("SELECT * FROM $recent_played_db ORDER BY timestamp DESC")
    suspend fun list(): List<RecentPlayedEntity>


    @Query("SELECT * FROM $recent_played_db ORDER BY timestamp DESC LIMIT 6")
    suspend fun recentList(): List<RecentPlayedEntity>
}