package com.rizwansayyed.zene.data.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.rizwansayyed.zene.data.roomdb.model.UpdateData
import com.rizwansayyed.zene.utils.Utils.RoomDB.UPDATE_ROOM_DB

@Dao
interface UpdatesDao {
    @Query("SELECT * FROM $UPDATE_ROOM_DB")
    fun getAll(): List<UpdateData>

    @Insert
    fun insert(users: UpdateData)

    @Delete
    fun delete(user: UpdateData)
}