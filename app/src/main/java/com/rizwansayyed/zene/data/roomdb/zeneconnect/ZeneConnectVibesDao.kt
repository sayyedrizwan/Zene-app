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

    @Query("SELECT * FROM $ZENE_CONNECT_VIBES_DB WHERE id = :id LIMIT 1")
    suspend fun getPosts(id: Int): ZeneConnectVibesModel?

    @Query("SELECT COUNT(id) FROM $ZENE_CONNECT_VIBES_DB WHERE number = :number")
    suspend fun allPostsNumber(number: String): Int

    @Query("SELECT * FROM $ZENE_CONNECT_VIBES_DB WHERE number = :number ORDER BY isNew DESC, timestamp DESC")
    suspend fun allVibesNumberSortNew(number: String): List<ZeneConnectVibesModel>

    @Query("UPDATE $ZENE_CONNECT_VIBES_DB SET isNew = 0 WHERE id = :id")
    suspend fun resetNewVibes(id: Int)

    @Query("UPDATE $ZENE_CONNECT_VIBES_DB SET isSeen = 1 WHERE number = :number AND imagePath = :photo")
    suspend fun updateSeenVibes(number: String, photo: String)

    @Query("UPDATE $ZENE_CONNECT_VIBES_DB SET emoji = :emoji WHERE number = :number AND imagePath = :photo")
    suspend fun updateEmojiVibes(number: String, photo: String, emoji: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg contacts: ZeneConnectVibesModel)
}