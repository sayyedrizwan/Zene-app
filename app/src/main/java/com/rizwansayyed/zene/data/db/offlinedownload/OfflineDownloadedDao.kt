package com.rizwansayyed.zene.data.db.offlinedownload

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.data.utils.DBNAME.OFFLINE_DOWNLOADED_SONGS_DB
import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineDownloadedDao {
    @Query("SELECT * FROM $OFFLINE_DOWNLOADED_SONGS_DB ORDER BY timestamp DESC")
    suspend fun list(): List<OfflineDownloadedEntity>


    @Query("SELECT * FROM $OFFLINE_DOWNLOADED_SONGS_DB ORDER BY timestamp DESC")
    fun recentList(): Flow<List<OfflineDownloadedEntity>>


    @Upsert
    suspend fun insert(v: OfflineDownloadedEntity)
}