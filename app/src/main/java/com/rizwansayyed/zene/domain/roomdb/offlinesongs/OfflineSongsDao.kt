package com.rizwansayyed.zene.domain.roomdb.offlinesongs

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.utils.Utils.DB.OFFLINE_SONGS_DB
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB
import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineSongsDao {

    @Query("SELECT * FROM $OFFLINE_SONGS_DB ORDER BY timestamp DESC")
    fun allOfflineSongs(): Flow<List<OfflineSongsEntity>>

    @Query("SELECT * FROM $OFFLINE_SONGS_DB ORDER BY timestamp DESC")
    suspend fun offlineSongs(): List<OfflineSongsEntity>

    @Upsert
    suspend fun insert(recentPlay: OfflineSongsEntity)
}