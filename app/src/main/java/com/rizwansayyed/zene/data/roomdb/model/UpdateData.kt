package com.rizwansayyed.zene.data.roomdb.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.utils.Utils.RoomDB.UPDATE_ROOM_DB

@Entity(tableName = UPDATE_ROOM_DB)
data class UpdateData(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var macAddress: String = "",
    var lat: Double? = null,
    var long: Double? = null,
    var address: String? = null,
    var ts: Long? = null,
    var type: Int? = null,
)

const val UPDATES_TYPE_DISCONNECT = 0
const val UPDATES_TYPE_CONNECT = 1