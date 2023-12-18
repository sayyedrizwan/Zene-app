package com.rizwansayyed.zene.data.db.artistsfeed

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.data.utils.DBNAME.ARTISTS_FEED_DB
import com.rizwansayyed.zene.data.utils.DBNAME.ARTISTS_PIN_DB
import com.rizwansayyed.zene.data.utils.DBNAME.OFFLINE_DOWNLOADED_SONGS_DB
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistsFeedDao {
    @Query("SELECT * FROM $ARTISTS_FEED_DB ORDER BY timeAdded DESC")
    fun flowList(): Flow<List<ArtistsFeedEntity>>

    @Query("DELETE FROM $ARTISTS_FEED_DB WHERE LOWER(REPLACE(artistsName, ' ', '')) = LOWER(REPLACE(:name, ' ', ''))")
    suspend fun delete(name: String): Int

    @Upsert
    suspend fun insertOrUpdate(v: ArtistsFeedEntity)
}