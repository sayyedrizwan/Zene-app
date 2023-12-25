package com.rizwansayyed.zene.data.db.savedplaylist.playlistsongs

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.utils.DBNAME
import com.rizwansayyed.zene.data.utils.DBNAME.PLAYLIST_SONGS_DB
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicType

const val DEFAULT_PLAYLIST_ITEMS = "-0,"

@Entity(tableName = PLAYLIST_SONGS_DB)
data class PlaylistSongsEntity(
    @PrimaryKey var songId: String,
    var addedPlaylistIds: String,
    val name: String?,
    val artists: String?,
    var thumbnail: String?,
    val timestamp: Long,
)

fun PlaylistSongsEntity.toMusicData(): MusicData {
    return MusicData(thumbnail, name, artists, songId, MusicType.MUSIC, "")
}

fun List<PlaylistSongsEntity>.toMusicDataList(): List<MusicData> {
    return this.map { MusicData(it.thumbnail, it.name, it.artists, it.songId, MusicType.MUSIC, "") }
}