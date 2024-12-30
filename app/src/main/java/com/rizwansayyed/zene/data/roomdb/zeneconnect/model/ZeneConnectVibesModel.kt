package com.rizwansayyed.zene.data.roomdb.zeneconnect.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.utils.Utils.RoomDB.ZENE_CONNECT_VIBES_DB
import java.util.concurrent.TimeUnit

@Entity(tableName = ZENE_CONNECT_VIBES_DB)
data class ZeneConnectVibesModel(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    var number: String = "",
    var timestamp: Long? = null,
    var imagePath: String? = "",
    var songId: String? = null,
    var songArtists: String? = null,
    var songThumbnail: String? = null,
    var songName: String? = null,
    var type: String? = null,
    var isNew: Boolean = true
) {
    fun getExtraDetails(): ZeneMusicDataItems? {
        return try {
            if (songName == null || songArtists == null || songId == null) null
            else ZeneMusicDataItems(songName, songArtists, songId, songThumbnail, songId, type)
        } catch (e: Exception) {
            null
        }
    }

    fun timeAgo(): String {
        timestamp ?: return ""
        val currentMillis = System.currentTimeMillis()
        val differenceMillis = currentMillis - timestamp!!

        if (differenceMillis < 0) return ""
        val seconds = TimeUnit.MILLISECONDS.toSeconds(differenceMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(differenceMillis)
        val hours = TimeUnit.MILLISECONDS.toHours(differenceMillis)
        val days = TimeUnit.MILLISECONDS.toDays(differenceMillis)

        return when {
            seconds < 60 -> "${seconds}s ago"
            minutes < 60 -> "${minutes}m ago"
            hours < 24 -> "${hours}h ago"
            else -> "${days}d ago"
        }
    }
}