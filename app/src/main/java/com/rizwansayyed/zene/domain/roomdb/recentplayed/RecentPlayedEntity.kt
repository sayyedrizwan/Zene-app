package com.rizwansayyed.zene.domain.roomdb.recentplayed

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineSongsEntity
import com.rizwansayyed.zene.domain.roomdb.songsdetails.SongDetailsEntity
import com.rizwansayyed.zene.presenter.jsoup.model.YTSearchData
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB

@Entity(tableName = RECENT_PLAYED_DB)
data class RecentPlayedEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String,
    val artists: String,
    var playTimes: Int,
    val pid: String,
    val img: String,
    var timestamp: Long,
    var playerDuration: Long,
    var lastListenDuration: Long
)

fun RecentPlayedEntity.toTopArtistsSongs(): TopArtistsSongs {
    val artists = this.artists.trim().substringBefore("&").substringBefore(",")
    return TopArtistsSongs(artists, this.img, this.name)
}

fun toRecentPlay(s: OfflineSongsEntity): RecentPlayedEntity {
    return RecentPlayedEntity(
        null, s.songName, s.songArtists, 1, s.pid, s.img, System.currentTimeMillis(), 0, 0
    )
}

fun toRecentPlay(s: YTSearchData): RecentPlayedEntity {
    return RecentPlayedEntity(
        null, s.songName, s.artistName, 1, s.songID, s.thumbnail, System.currentTimeMillis(), 0, 0
    )
}

fun toRecentPlay(s: SongDetailsEntity): RecentPlayedEntity {
    return RecentPlayedEntity(
        null, s.name, s.artists, 1, s.songID, s.thumbnail, System.currentTimeMillis(), 0, 0
    )
}
