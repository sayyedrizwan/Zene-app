package com.rizwansayyed.zene.data.roomdb.zeneconnect

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
import com.rizwansayyed.zene.utils.Utils.RoomDB.ZENE_CONNECT_CONTACT_DB

@Dao
interface ZeneConnectContactsDao {

    @Query("SELECT * FROM $ZENE_CONNECT_CONTACT_DB ORDER BY ts DESC")
    fun get(): LiveData<List<ZeneConnectContactsModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg contacts: ZeneConnectContactsModel)
}