package com.rizwansayyed.zene.data.roomdb.offlinesongs.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.utils.Utils.RoomDB.OFFLINE_SONGS_ROOM_DB

@Entity(tableName = OFFLINE_SONGS_ROOM_DB)
data class OfflineSongsData(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    val name: String?,
    val artists: String?,
    val thumbnail: String?
)