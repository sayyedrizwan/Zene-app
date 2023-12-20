package com.rizwansayyed.zene.data.db.artistsfeed

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rizwansayyed.zene.data.utils.DBNAME.ARTISTS_FEED_DB
import com.rizwansayyed.zene.data.utils.DBNAME.ARTISTS_PIN_DB
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.daysOldTimestamp

@Entity(tableName = ARTISTS_FEED_DB)
data class ArtistsFeedEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    var artistsName: String,
    var username: String,
    var timeAdded: Long,
    var feedType: FeedPostType,
    var media: String?,
    var isVideo: Boolean,
    var title: String,
    var desc: String?,
    var postId: String?,
)

enum class FeedPostType {
    INSTAGRAM, FACEBOOK, TIKTOK, YOUTUBE, X, XFans, NEWS
}