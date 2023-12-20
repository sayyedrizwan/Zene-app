package com.rizwansayyed.zene.data.db.artistsfeed

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.utils.DBNAME.ARTISTS_FEED_DB
import com.rizwansayyed.zene.data.utils.DBNAME.ARTISTS_PIN_DB
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.daysOldTimestamp
import java.util.Calendar

@Entity(tableName = ARTISTS_FEED_DB)
data class ArtistsFeedEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var artistsName: String?,
    var username: String?,
    var timeAdded: Long?,
    var feedType: FeedPostType?,
    var media: String?,
    var isVideo: Boolean?,
    var title: String?,
    var desc: String?,
    var postId: String?,
)


fun youtubeToTimestamp(ts: String): Long {
    return try {
        val t = Regex("\\d+").find(ts)?.value?.toIntOrNull() ?: 0
        val duration = ts.substringAfter("$t").trim().lowercase()

        val field = if (duration.contains("second")) Calendar.SECOND
        else if (duration.contains("minute")) Calendar.MINUTE
        else if (duration.contains("hour")) Calendar.HOUR_OF_DAY
        else if (duration.contains("year")) Calendar.DAY_OF_YEAR
        else if (duration.contains("week")) Calendar.WEEK_OF_YEAR
        else if (duration.contains("month")) Calendar.MONTH
        else Calendar.DAY_OF_YEAR


        val calendar = Calendar.getInstance()
        calendar.add(field, -t)
        calendar.timeInMillis

    } catch (e: Exception) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        calendar.timeInMillis
    }
}

enum class FeedPostType {
    INSTAGRAM, INSTAGRAM_STORIES, FACEBOOK, YOUTUBE, SHORTS, NEWS
}