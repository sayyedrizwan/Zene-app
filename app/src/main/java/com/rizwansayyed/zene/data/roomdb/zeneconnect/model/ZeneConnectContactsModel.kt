package com.rizwansayyed.zene.data.roomdb.zeneconnect.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.utils.Utils.RoomDB.ZENE_CONNECT_CONTACT_DB

@Entity(
    tableName = ZENE_CONNECT_CONTACT_DB,
    indices = [Index(value = ["number"], unique = true)]
)
data class ZeneConnectContactsModel(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var number: String = "",
    var email: String? = "",
    var profilePhoto: String? = "",
    var contactName: String? = "",
    var currentPlayingSong: String? = "",
    var currentPlayingSongTime: Long? = null,
    var numberOfPosts: Int? = null,
    var ts: Long? = null,
)