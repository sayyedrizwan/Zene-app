package com.rizwansayyed.zene.data.db.recentplay

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.utils.DBNAME.RECENT_PLAYED_DB
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicType


@Entity(tableName = RECENT_PLAYED_DB)
data class RecentPlayedEntity(
    @PrimaryKey val songId: String,
    val name: String?,
    val artists: String?,
    var playTimes: Int,
    val thumbnail: String?,
    var timestamp: Long,
    var playerDuration: Long,
    var lastListenDuration: Long
)

fun List<RecentPlayedEntity>.asMusicDataList(): List<MusicData> {
    return this.map {
        MusicData(
            it.thumbnail, it.name, it.artists, it.songId, MusicType.MUSIC, ""
        )
    }
}