package com.rizwansayyed.zene.roomdb.recentplayed

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_ARTISTS_DB
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB

@Entity(tableName = RECENT_PLAYED_ARTISTS_DB)
data class PlayedArtistsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val artists: String,
    val playTimes: Int,
    val timestamp: Long
)