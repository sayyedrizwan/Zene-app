package com.rizwansayyed.zene.data.roomdb.zeneconnect.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.utils.Utils.RoomDB.ZENE_CONNECT_CONTACT_DB

@Entity(tableName = ZENE_CONNECT_CONTACT_DB)
data class ZeneConnectContactsModel(
    @PrimaryKey var number: String = "",
    var email: String? = "",
    var profilePhoto: String? = "",
    var contactName: String? = "",
    var currentPlayingSongName: String? = null,
    var currentPlayingSongArtists: String? = null,
    var currentPlayingSongID: String? = null,
    var currentPlayingSongThumbnail: String? = null,
    var numberOfPosts: Int? = null,
    var isNew: Boolean? = false,
    var ts: Long? = null,
)