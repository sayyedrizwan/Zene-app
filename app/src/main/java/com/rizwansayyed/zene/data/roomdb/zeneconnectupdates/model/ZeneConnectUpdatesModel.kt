package com.rizwansayyed.zene.data.roomdb.zeneconnectupdates.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.utils.Utils.RoomDB.ZENE_CONNECT_UPDATES_DB
import com.rizwansayyed.zene.utils.Utils.RoomDB.ZENE_CONNECT_VIBES_DB
import java.util.concurrent.TimeUnit

@Entity(tableName = ZENE_CONNECT_UPDATES_DB)
data class ZeneConnectUpdatesModel(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var number: String = "",
    var timestamp: Long? = null,
    var imagePath: String? = "",
    var songId: String? = null,
    var isSeen: Boolean = false,
    var emoji: String? = "",
)