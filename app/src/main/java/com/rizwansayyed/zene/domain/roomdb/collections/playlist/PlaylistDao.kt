package com.rizwansayyed.zene.domain.roomdb.collections.playlist

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.utils.Utils.DB.OFFLINE_SONGS_DB
import com.rizwansayyed.zene.utils.Utils.DB.PLAYLIST_DB
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM $PLAYLIST_DB ORDER BY timestamp DESC")
    fun playlists(): Flow<List<PlaylistEntity>>

    @Upsert
    suspend fun insert(playlist: PlaylistEntity)
}