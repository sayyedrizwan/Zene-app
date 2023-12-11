package com.rizwansayyed.zene.data.db.savedplaylist.playlist

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.data.utils.DBNAME.SAVED_PLAYLIST_DB
import com.rizwansayyed.zene.utils.Utils.OFFSET_LIMIT
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedPlaylistDao {

    @Query("SELECT * FROM $SAVED_PLAYLIST_DB ORDER BY timestamp DESC")
    fun list(): Flow<List<SavedPlaylistEntity>>

    @Query("SELECT * FROM $SAVED_PLAYLIST_DB WHERE playlistId is NULL ORDER BY timestamp DESC LIMIT :limit, $OFFSET_LIMIT")
    suspend fun pagingCreatedPlaylist(limit: Int): List<SavedPlaylistEntity>

    @Query("SELECT * FROM $SAVED_PLAYLIST_DB WHERE playlistId is NULL ORDER BY timestamp DESC LIMIT :limit, $OFFSET_LIMIT")
    suspend fun pagingPlaylist(limit: Int): List<SavedPlaylistEntity>

    @Query("SELECT * FROM $SAVED_PLAYLIST_DB WHERE LOWER(name) = :n")
    suspend fun searchWithName(n: String): List<SavedPlaylistEntity>

    @Upsert
    suspend fun insert(v: SavedPlaylistEntity)
}