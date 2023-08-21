package com.rizwansayyed.zene.domain.roomdb.offlinesongs

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.utils.Utils.DB.OFFLINE_SONGS_DB
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB

@Entity(tableName = OFFLINE_SONGS_DB)
data class OfflineSongsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val songName: String,
    val songArtists: String,
    val pid: String,
    val img: String,
    val timestamp: Long,
    val playerDuration: Long
)