package com.rizwansayyed.zene.data.roomdb.offlinesongs.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.utils.Utils.RoomDB.OFFLINE_SONGS_ROOM_DB

@Entity(tableName = OFFLINE_SONGS_ROOM_DB)
data class OfflineSongsData(
    @PrimaryKey var id: String,
    val name: String?,
    val artists: String?,
    val thumbnail: String?,
    val ts: Long?
) {
    fun asMusicData(): ZeneMusicDataItems {
        return ZeneMusicDataItems(name, artists, id, thumbnail, "", "OFFLINE_SONGS")
    }
}