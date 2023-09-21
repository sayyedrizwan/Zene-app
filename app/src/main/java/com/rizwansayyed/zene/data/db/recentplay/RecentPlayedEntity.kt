package com.rizwansayyed.zene.data.db.recentplay

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.utils.DBNAME.RECENT_PLAYED_DB


@Entity(tableName = RECENT_PLAYED_DB)
data class RecentPlayedEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String,
    val artists: String,
    var playTimes: Int,
    val pid: String,
    val thumbnail: String,
    var timestamp: Long,
    var playerDuration: Long,
    var lastListenDuration: Long
)