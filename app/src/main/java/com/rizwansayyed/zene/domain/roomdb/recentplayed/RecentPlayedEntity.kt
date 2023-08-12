package com.rizwansayyed.zene.domain.roomdb.recentplayed

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
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

fun RecentPlayedEntity.toTopArtistsSongs(): TopArtistsSongs {
    val artists = this.artists.trim().substringBefore("&").substringBefore(",")
    return TopArtistsSongs(artists, this.img, this.name)
}