package com.rizwansayyed.zene.data.roomdb.zeneconnect

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectVibesModel
import com.rizwansayyed.zene.utils.Utils.RoomDB.ZENE_CONNECT_VIBES_DB

@Dao
interface ZeneConnectVibesDao {

    @Query("SELECT * FROM $ZENE_CONNECT_VIBES_DB ORDER BY timestamp DESC")
    fun get(): List<ZeneConnectVibesModel>

    @Query("SELECT * FROM $ZENE_CONNECT_VIBES_DB WHERE number = :number")
    suspend fun get(number: String): ZeneConnectVibesModel?

    @Query("SELECT COUNT(id) FROM $ZENE_CONNECT_VIBES_DB WHERE number = :number AND isNew = 1")
    suspend fun newPosts(number: String): Int

    @Query("SELECT COUNT(id) FROM $ZENE_CONNECT_VIBES_DB WHERE number = :number")
    suspend fun allPostsNumber(number: String): Int

    @Query("SELECT * FROM $ZENE_CONNECT_VIBES_DB WHERE number = :number ORDER BY isNew DESC")
    suspend fun allVibesNumberSortNew(number: String): List<ZeneConnectVibesModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg contacts: ZeneConnectVibesModel)
}