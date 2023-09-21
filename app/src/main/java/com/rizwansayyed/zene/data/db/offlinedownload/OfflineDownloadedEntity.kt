package com.rizwansayyed.zene.data.db.offlinedownload

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.db.utils.DbName
import com.rizwansayyed.zene.data.db.utils.DbName.offline_downloaded_songs_db

@Entity(tableName = offline_downloaded_songs_db)
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
