package com.rizwansayyed.zene.data.roomdb.zeneconnectupdates

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rizwansayyed.zene.data.roomdb.zeneconnectupdates.model.ZeneConnectUpdatesModel
import com.rizwansayyed.zene.utils.Utils.RoomDB.ZENE_CONNECT_UPDATES_DB

@Dao
interface ZeneConnectUpdatesDao {

    @Query("SELECT * FROM $ZENE_CONNECT_UPDATES_DB ORDER BY timestamp DESC")
    fun getList(): List<ZeneConnectUpdatesModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg list: ZeneConnectUpdatesModel)
}