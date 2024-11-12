package com.rizwansayyed.zene.data.roomdb.offlinesongs

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rizwansayyed.zene.data.roomdb.offlinesongs.model.OfflineSongsData
import com.rizwansayyed.zene.utils.Utils.RoomDB.OFFLINE_SONGS_ROOM_DB

@Dao
interface OfflineSongsDao {
    @Query("SELECT * FROM $OFFLINE_SONGS_ROOM_DB ORDER BY ts DESC LIMIT 30 OFFSET :limit")
    fun get(limit: Int): List<OfflineSongsData>

    @Query("SELECT * FROM $OFFLINE_SONGS_ROOM_DB WHERE id = :songID")
    fun isSaved(songID: String): List<OfflineSongsData>

    @Query("DELETE FROM $OFFLINE_SONGS_ROOM_DB WHERE id = :id")
    fun deleteAll(id: String)

    @Insert
    fun insert(data: OfflineSongsData)
}