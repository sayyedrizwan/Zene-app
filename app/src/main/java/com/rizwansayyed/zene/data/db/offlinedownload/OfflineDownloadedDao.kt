package com.rizwansayyed.zene.data.db.offlinedownload

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.data.db.utils.DbName.offline_downloaded_songs_db
import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineDownloadedDao {
    @Query("SELECT * FROM $offline_downloaded_songs_db ORDER BY timestamp DESC")
    suspend fun list(): List<OfflineDownloadedEntity>


    @Query("SELECT * FROM $offline_downloaded_songs_db ORDER BY timestamp DESC")
    fun recentList(): Flow<List<OfflineDownloadedEntity>>


    @Upsert
    suspend fun insert(v: OfflineDownloadedEntity)
}