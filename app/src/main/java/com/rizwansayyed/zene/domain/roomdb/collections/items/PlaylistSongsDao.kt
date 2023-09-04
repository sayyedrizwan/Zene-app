package com.rizwansayyed.zene.domain.roomdb.collections.items

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.domain.roomdb.collections.playlist.PlaylistEntity
import com.rizwansayyed.zene.utils.Utils.DB.OFFLINE_SONGS_DB
import com.rizwansayyed.zene.utils.Utils.DB.PLAYLIST_DB
import com.rizwansayyed.zene.utils.Utils.DB.PLAYLIST_SONGS_DB
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistSongsDao {

    @Query("SELECT * FROM $PLAYLIST_SONGS_DB WHERE playlistId = :playlistID ORDER BY timestamp DESC")
    fun songs(playlistID: Int): Flow<List<PlaylistSongsEntity>>

    @Query("SELECT * FROM $PLAYLIST_SONGS_DB WHERE playlistId = :playlistID ORDER BY timestamp DESC LIMIT 4")
   suspend fun latest4playlists(playlistID: Int): List<PlaylistSongsEntity>

    @Query("SELECT COUNT(*) FROM $PLAYLIST_SONGS_DB WHERE pID = :pID")
   suspend fun isSongsAlreadyAvailable(pID: String): Int

    @Upsert
    suspend fun insert(playlist: PlaylistSongsEntity)
}