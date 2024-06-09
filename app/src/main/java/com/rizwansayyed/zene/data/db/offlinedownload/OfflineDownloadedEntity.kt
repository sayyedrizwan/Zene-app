package com.rizwansayyed.zene.data.db.offlinedownload

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.data.utils.DBNAME.OFFLINE_DOWNLOADED_SONGS_DB
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicType

@Entity(tableName = OFFLINE_DOWNLOADED_SONGS_DB)
data class OfflineDownloadedEntity(
    @PrimaryKey(autoGenerate = false) val songId: String,
    var songName: String,
    var songArtists: String,
    var thumbnail: String,
    var songPath: String,
    var timestamp: Long,
    var progress: Int,
    var lyrics: String?,
    var subtitles: Boolean?,
) {
    fun asMusicData(): MusicData {
        return MusicData(thumbnail, songName, songArtists, songId, MusicType.MUSIC)
    }
}


fun List<OfflineDownloadedEntity>.asMusicDataList(): List<MusicData> {
    return this.map {
        MusicData(
            it.thumbnail, it.songName, it.songArtists, it.songId, MusicType.MUSIC, ""
        )
    }
}