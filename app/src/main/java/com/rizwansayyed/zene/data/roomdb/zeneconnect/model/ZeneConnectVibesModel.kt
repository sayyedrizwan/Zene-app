package com.rizwansayyed.zene.data.roomdb.zeneconnect.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.utils.Utils.RoomDB.ZENE_CONNECT_VIBES_DB

@Entity(tableName = ZENE_CONNECT_VIBES_DB)
data class ZeneConnectVibesModel(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var number: String = "",
    var timestamp: Long? = null,
    var imagePath: String? = "",
    var songId: String? = null,
    var songArtists: String? = null,
    var songName: String? = null,
    var type: String? = null,
    var isNew: Boolean = true,
)