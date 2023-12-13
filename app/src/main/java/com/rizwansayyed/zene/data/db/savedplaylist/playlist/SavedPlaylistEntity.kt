package com.rizwansayyed.zene.data.db.savedplaylist.playlist

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.utils.DBNAME.SAVED_PLAYLIST_DB
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicType


@Entity(tableName = SAVED_PLAYLIST_DB)
data class SavedPlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    var name: String,
    val timestamp: Long = System.currentTimeMillis(),
    var thumbnail: String? = null,
    var items: Int = 0,
    var playlistId: String? = null
)


fun SavedPlaylistEntity.asMusicData(): MusicData {
    return MusicData(thumbnail, name, name, playlistId, MusicType.ALBUMS, "")
}