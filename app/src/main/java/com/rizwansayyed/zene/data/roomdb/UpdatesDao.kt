package com.rizwansayyed.zene.data.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.rizwansayyed.zene.data.roomdb.model.UpdateData
import com.rizwansayyed.zene.utils.Utils.RoomDB.UPDATE_ROOM_DB
import okhttp3.Address

@Dao
interface UpdatesDao {
    @Query("SELECT * FROM $UPDATE_ROOM_DB WHERE macAddress = :address ORDER BY ts DESC LIMIT 30 OFFSET :limit")
    fun get(address: String, limit: Int): List<UpdateData>

    @Query("DELETE FROM $UPDATE_ROOM_DB WHERE macAddress = :address")
    fun deleteAll(address: String)

    @Insert
    fun insert(users: UpdateData)

    @Delete
    fun delete(user: UpdateData)
}