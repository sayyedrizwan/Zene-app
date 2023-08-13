package com.rizwansayyed.zene.domain.roomdb.songsdetails

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.presenter.model.Thumbnail
import com.rizwansayyed.zene.presenter.model.TopArtistsSongs
import com.rizwansayyed.zene.utils.Utils.DB.RECENT_PLAYED_DB
import com.rizwansayyed.zene.utils.Utils.DB.SONG_DETAILS_DB

@Entity(tableName = SONG_DETAILS_DB)
data class SongDetailsEntity(
    val name: String,
    val artists: String,
    val songID: String,
    val videoID: String,
    val thumbnail: String,
    val timestamp: Long,
    @PrimaryKey(autoGenerate = true) val id: Int?
)