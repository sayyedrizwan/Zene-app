package com.rizwansayyed.zene.domain.roomdb.recentplayed

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentPlayedDao {

    @Query("SELECT * FROM $RECENT_PLAYED_DB ORDER BY playTimes DESC LIMIT 6")
    fun recentPlayedHome(): Flow<List<RecentPlayedEntity>>

    @Query("SELECT * FROM $RECENT_PLAYED_DB ORDER BY playTimes DESC LIMIT 7")
    suspend fun topListenSongs(): List<RecentPlayedEntity>

    @Query("SELECT * FROM $RECENT_PLAYED_DB GROUP BY artists ORDER BY playTimes DESC LIMIT :limit")
    suspend fun artistsUnique(limit : Int): List<RecentPlayedEntity>

    @Upsert
    suspend fun insert(recentPlay: RecentPlayedEntity)
}