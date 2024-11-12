package com.rizwansayyed.zene.data.roomdb.offlinesongs

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.rizwansayyed.zene.data.roomdb.updates.model.UpdateData
import com.rizwansayyed.zene.utils.Utils.RoomDB.OFFLINE_SONGS_ROOM_DB
import com.rizwansayyed.zene.utils.Utils.RoomDB.UPDATE_ROOM_DB

@Dao
interface OfflineSongsDao {
    @Query("SELECT * FROM $OFFLINE_SONGS_ROOM_DB ORDER BY ts DESC LIMIT 30 OFFSET :limit")
    fun get(limit: Int): List<UpdateData>

    @Query("DELETE FROM $UPDATE_ROOM_DB WHERE macAddress = :address")
    fun deleteAll(address: String)

    @Insert
    fun insert(users: UpdateData)

    @Delete
    fun delete(user: UpdateData)
}