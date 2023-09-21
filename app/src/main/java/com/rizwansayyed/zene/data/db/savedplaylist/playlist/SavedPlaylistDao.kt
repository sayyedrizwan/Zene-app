package com.rizwansayyed.zene.data.db.savedplaylist.playlist

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.data.utils.DBNAME.SAVED_PLAYLIST_DB
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedPlaylistDao {
    @Query("SELECT * FROM $SAVED_PLAYLIST_DB ORDER BY timestamp DESC")
    fun list(): Flow<List<SavedPlaylistEntity>>

    @Upsert
    suspend fun insert(v: SavedPlaylistEntity)
}