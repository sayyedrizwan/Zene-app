package com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.utils.DBNAME
import com.rizwansayyed.zene.data.utils.DBNAME.PLAYLIST_SONGS_DB

const val DEFAULT_PLAYLIST_ITEMS = "-1,"

@Entity(tableName = PLAYLIST_SONGS_DB)
data class PlaylistSongsEntity(
    @PrimaryKey var songId: String,
    var addedPlaylistIds: String,
    val name: String?,
    val artists: String?,
    val thumbnail: String?,
    val timestamp: Long,
)