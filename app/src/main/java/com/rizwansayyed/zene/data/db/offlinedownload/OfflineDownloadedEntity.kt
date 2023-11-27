package com.rizwansayyed.zene.data.db.offlinedownload

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.utils.DBNAME.OFFLINE_DOWNLOADED_SONGS_DB

@Entity(tableName = OFFLINE_DOWNLOADED_SONGS_DB)
data class OfflineDownloadedEntity(
    @PrimaryKey(autoGenerate = false) val songId: String,
    val songName: String,
    val songArtists: String,
    var thumbnail: String,
    var songPath: String,
    val timestamp: Long,
    var progress: Int,
)