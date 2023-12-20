package com.rizwansayyed.zene.data.db.artistspin

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.utils.DBNAME.ARTISTS_PIN_DB
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.daysOldTimestamp

@Entity(tableName = ARTISTS_PIN_DB)
data class PinnedArtistsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var name: String,
    var thumbnail: String = "",
    var instagramUsername: String = "",
    var facebookPage: String = "",
    var tiktokPage: String = "",
    var youtubeChannel: String = "",
    var xChannel: String = "",
    var addedTime: Long = System.currentTimeMillis(),
    var lastProfilePicSync: Long = daysOldTimestamp(10),
    var lastInfoSync: Long = daysOldTimestamp(10),
)