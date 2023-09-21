package com.rizwansayyed.zene.data.db.offlinedownload

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.utils.DBNAME.OFFLINE_DOWNLOADED_SONGS_DB

@Entity(tableName = OFFLINE_DOWNLOADED_SONGS_DB)
data class OfflineDownloadedEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val songName: String,
    val songArtists: String,
    val pid: String,
    var thumbnail: String,
    var songPath: String,
    val timestamp: Long,
    var status: OfflineStatusTypes,
    val playerDuration: Long,
    val viewed: Int
)

enum class OfflineStatusTypes(val v: Int) {
    DOWNLOADING(0), SUCCESS(1), FAILED(1),
}
