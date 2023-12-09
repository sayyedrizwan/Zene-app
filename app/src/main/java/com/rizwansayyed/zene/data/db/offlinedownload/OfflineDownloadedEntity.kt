package com.rizwansayyed.zene.data.db.offlinedownload

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.utils.DBNAME.OFFLINE_DOWNLOADED_SONGS_DB

@Entity(tableName = OFFLINE_DOWNLOADED_SONGS_DB)
data class OfflineDownloadedEntity(
    @PrimaryKey(autoGenerate = false) val songId: String,
    var songName: String,
    var songArtists: String,
    var thumbnail: String,
    var songPath: String,
    var timestamp: Long,
    var progress: Int,
)