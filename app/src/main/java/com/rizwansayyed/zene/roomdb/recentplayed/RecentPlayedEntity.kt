package com.rizwansayyed.zene.roomdb.recentplayed

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB

@Entity(tableName = RECENT_PLAYED_DB)
data class RecentPlayedEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String,
    val artists: String,
    val playTimes: Int,
    val pid: String,
    val img: String,
    val timestamp: Long,
    val playerDuration: Long
)