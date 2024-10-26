package com.rizwansayyed.zene.data.api.model

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class ZeneCacheTopSongsArtistsPostsResponse(
    val artists: List<String?>?,
    val songs: List<String?>?
)

data class ZeneArtistsPostsResponse(
    val artists: ZeneMusicDataResponse?,
    val posts: List<ZeneArtistsPostItems?>?
)

data class ZeneArtistsPostItems(
    val caption: String?,
    val media: List<String?>?,
    val name: String?,
    val profileImg: String?,
    val thumbnail: String?,
    val timestamp: Long?,
    val type: String?,
    val url: String?,
    val username: String?
) {
    fun timestampToDate(): String {
        if (timestamp == null) {
            return ""
        }
        val now = LocalDateTime.now()
        val then = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())

        val secondsAgo = ChronoUnit.SECONDS.between(then, now)
        val minutesAgo = ChronoUnit.MINUTES.between(then, now)
        val hoursAgo = ChronoUnit.HOURS.between(then, now)
        val daysAgo = ChronoUnit.DAYS.between(then, now)
        val monthsAgo = ChronoUnit.MONTHS.between(then, now)
        val yearsAgo = ChronoUnit.YEARS.between(then, now)

        return when {
            yearsAgo > 0 -> {
                val formatter = DateTimeFormatter.ofPattern("dd:MMM:yyyy")
                val dateTime =
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
                dateTime.format(formatter)
            }

            monthsAgo > 0 -> "$monthsAgo months ago"
            daysAgo > 0 -> "$daysAgo days ago"
            hoursAgo > 0 -> "$hoursAgo hours ago"
            minutesAgo > 0 -> "$minutesAgo minutes ago"
            secondsAgo > 0 -> "$secondsAgo seconds ago"
            else -> "just now"
        }
    }
}