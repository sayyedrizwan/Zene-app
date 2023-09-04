package com.rizwansayyed.zene.domain.roomdb.collections.playlist

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.domain.roomdb.offlinesongs.OfflineStatusTypes
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.utils.Utils.DB.OFFLINE_SONGS_DB
import com.rizwansayyed.zene.utils.Utils.DB.PLAYLIST_DB
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB

@Entity(tableName = PLAYLIST_DB)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val name: String,
    val timestamp: Long = System.currentTimeMillis(),
    var image1: String = "",
    var image2: String = "",
    var image3: String = "",
    var image4: String = "",
    val items: Int = 0,
)
