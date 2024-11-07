package com.rizwansayyed.zene.data.roomdb.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.utils.Utils.RoomDB.UPDATE_ROOM_DB

@Entity(tableName = UPDATE_ROOM_DB)
data class UpdateData(
    @PrimaryKey val id: Int,
    @ColumnInfo val macAddress: String,
    @ColumnInfo val lat: Double?,
    @ColumnInfo val long: Double?,
    @ColumnInfo val address: String?,
    @ColumnInfo val ts: Long?,
    @ColumnInfo val type: Long?,
)