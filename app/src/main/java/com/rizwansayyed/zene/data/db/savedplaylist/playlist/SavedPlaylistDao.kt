package com.rizwansayyed.zene.data.db.savedplaylist.playlist

import androidx.paging.DataSource
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.data.utils.DBNAME.SAVED_PLAYLIST_DB
import com.rizwansayyed.zene.data.utils.PAGINATION_PAGE_SIZE
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedPlaylistDao {
    @Query("SELECT * FROM $SAVED_PLAYLIST_DB ORDER BY timestamp DESC")
    fun list(): Flow<List<SavedPlaylistEntity>>


    @Query("SELECT * FROM $SAVED_PLAYLIST_DB ORDER BY timestamp DESC LIMIT :limit, $PAGINATION_PAGE_SIZE")
    suspend fun pagingPlaylist(limit: Int): List<SavedPlaylistEntity>

    @Query("SELECT * FROM $SAVED_PLAYLIST_DB WHERE LOWER(name) = :n")
    suspend fun searchWithName(n: String): List<SavedPlaylistEntity>

    @Upsert
    suspend fun insert(v: SavedPlaylistEntity)
}