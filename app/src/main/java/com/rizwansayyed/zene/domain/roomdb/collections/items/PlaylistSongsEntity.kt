package com.rizwansayyed.zene.domain.roomdb.collections.items

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineStatusTypes
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.utils.Utils.DB.OFFLINE_SONGS_DB
import com.rizwansayyed.zene.utils.Utils.DB.PLAYLIST_DB
import com.rizwansayyed.zene.utils.Utils.DB.PLAYLIST_SONGS_DB
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB

@Entity(tableName = PLAYLIST_SONGS_DB)
data class PlaylistSongsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String,
    val timestamp: Long,
    val thumbnail: Long,
    val artists: Long,
    val pID: Long,
)
