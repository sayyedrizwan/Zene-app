package com.rizwansayyed.zene.data.db.offlinedownload

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.data.utils.DBNAME.OFFLINE_DOWNLOADED_SONGS_DB
import com.rizwansayyed.zene.utils.Utils.OFFSET_LIMIT
import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineDownloadedDao {
    @Query("SELECT * FROM $OFFLINE_DOWNLOADED_SONGS_DB ORDER BY timestamp DESC")
    suspend fun list(): List<OfflineDownloadedEntity>

    @Query("SELECT * FROM $OFFLINE_DOWNLOADED_SONGS_DB ORDER BY timestamp DESC LIMIT $OFFSET_LIMIT")
    fun recentList(): Flow<List<OfflineDownloadedEntity>>

    @Query("SELECT * FROM $OFFLINE_DOWNLOADED_SONGS_DB ORDER BY timestamp DESC LIMIT :offset, $OFFSET_LIMIT")
    suspend fun recentList(offset: Int): List<OfflineDownloadedEntity>

    @Query("SELECT * FROM $OFFLINE_DOWNLOADED_SONGS_DB WHERE progress < 100 OR progress = -1 ORDER BY timestamp DESC")
    suspend fun nonDownloadedSongs(): List<OfflineDownloadedEntity>

    @Query("SELECT * FROM $OFFLINE_DOWNLOADED_SONGS_DB WHERE songId = :songId LIMIT 1")
    suspend fun songDetails(songId: String): OfflineDownloadedEntity?

    @Query("SELECT * FROM $OFFLINE_DOWNLOADED_SONGS_DB WHERE songId = :songId LIMIT 1")
    suspend fun readLyrics(songId: String): OfflineDownloadedEntity?

    @Query("DELETE FROM $OFFLINE_DOWNLOADED_SONGS_DB WHERE songId = :songId")
    suspend fun removeSong(songId: String): Int

    @Query("SELECT * FROM $OFFLINE_DOWNLOADED_SONGS_DB WHERE songId = :songId LIMIT 1")
    fun songDetailsFlow(songId: String): Flow<OfflineDownloadedEntity?>

    @Upsert
    suspend fun insertOrUpdate(v: OfflineDownloadedEntity)
}