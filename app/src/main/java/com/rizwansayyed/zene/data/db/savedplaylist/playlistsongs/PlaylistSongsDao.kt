package com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rizwansayyed.zene.data.utils.DBNAME.PLAYLIST_SONGS_DB
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistSongsDao {
    @Query("SELECT * FROM $PLAYLIST_SONGS_DB ORDER BY timestamp DESC")
    fun list(): Flow<List<PlaylistSongsEntity>>

    @Query("SELECT * FROM $PLAYLIST_SONGS_DB WHERE songId = :songId LIMIT 1")
    fun info(songId: String): Flow<PlaylistSongsEntity?>

    @Query("SELECT * FROM $PLAYLIST_SONGS_DB WHERE songId = :songId LIMIT 1")
    suspend fun songInfo(songId: String): PlaylistSongsEntity?

    @Query("SELECT COUNT (*) FROM $PLAYLIST_SONGS_DB WHERE addedPlaylistIds LIKE '%$DEFAULT_PLAYLIST_ITEMS%'")
    suspend fun defaultPlaylistSongsCount(): Int

    @Query("DELETE FROM $PLAYLIST_SONGS_DB WHERE songId = :songId")
    suspend fun rmSongs(songId: String): Int

    @Upsert
    suspend fun insert(v: PlaylistSongsEntity)
}