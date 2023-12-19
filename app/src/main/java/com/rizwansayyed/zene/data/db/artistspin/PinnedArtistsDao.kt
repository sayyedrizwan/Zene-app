package com.rizwansayyed.zene.data.db.artistspin

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.data.utils.DBNAME.ARTISTS_PIN_DB
import com.rizwansayyed.zene.data.utils.DBNAME.OFFLINE_DOWNLOADED_SONGS_DB
import kotlinx.coroutines.flow.Flow

@Dao
interface PinnedArtistsDao {
    @Query("SELECT * FROM $ARTISTS_PIN_DB ORDER BY addedTime DESC")
    fun flowList(): Flow<List<PinnedArtistsEntity>>

    @Query("SELECT * FROM $ARTISTS_PIN_DB ORDER BY addedTime DESC")
   suspend fun list(): List<PinnedArtistsEntity>

    @Query("SELECT COUNT(*) FROM $ARTISTS_PIN_DB WHERE LOWER(REPLACE(name, ' ', '')) = LOWER(REPLACE(:name, ' ', ''))")
    suspend fun doContain(name: String): Int

    @Query("SELECT * FROM $ARTISTS_PIN_DB WHERE LOWER(REPLACE(name, ' ', '')) = LOWER(REPLACE(:name, ' ', '')) LIMIT 1")
    suspend fun artistsData(name: String): PinnedArtistsEntity

    @Query("UPDATE $ARTISTS_PIN_DB SET thumbnail = :url WHERE LOWER(REPLACE(name, ' ', '')) = LOWER(REPLACE(:url, ' ', ''))")
    suspend fun artistsThumbnailUpdate(url: String): Int

    @Query("DELETE FROM $ARTISTS_PIN_DB WHERE LOWER(REPLACE(name, ' ', '')) = LOWER(REPLACE(:name, ' ', ''))")
    suspend fun delete(name: String): Int

    @Upsert
    suspend fun insertOrUpdate(v: PinnedArtistsEntity)
}