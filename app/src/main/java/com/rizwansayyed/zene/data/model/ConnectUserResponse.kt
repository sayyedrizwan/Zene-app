package com.rizwansayyed.zene.data.model

import com.rizwansayyed.zene.datastore.DataStorageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

data class ConnectUserInfoResponse(
    val songDetails: ZeneMusicData?,
    var didRequestToYou: Boolean?,
    val myStatus: Status?,
    val otherStatus: Status?,
    val topSongs: List<ZeneMusicData>?,
    val user: ConnectUserResponse?,
    val unReadMessages: Int?,
    val vibes: List<ConnectFeedDataResponse?>?,
) {

    data class Status(
        var isConnected: Boolean?,
        var lastListeningSong: Boolean?,
        var locationSharing: Boolean?,
        var silentNotification: Boolean?
    )

    fun isConnected(): ConnectedUserStatus = runBlocking(Dispatchers.IO) {
        val userEmail = DataStorageManager.userInfo.firstOrNull()
        if (user?.email == userEmail?.email) return@runBlocking ConnectedUserStatus.ME
        if (myStatus?.isConnected == null) return@runBlocking ConnectedUserStatus.NONE
        return@runBlocking if (myStatus.isConnected == true) ConnectedUserStatus.FRIENDS else ConnectedUserStatus.REQUESTED
    }
}

enum class ConnectedUserStatus {
    FRIENDS, REQUESTED, NONE, ME
}

data class ConnectUserResponse(
    val country: String?,
    val email: String?,
    val connect_status: String?,
    val last_seen: Long?,
    val location: String?,
    val name: String?,
    val profile_photo: String?,
    val username: String?
) {
    fun isUserLocation(): Boolean {
        try {
            val lat = location?.substringBefore(",")?.trim()?.toDouble() ?: 0.0
            val lon = location?.substringAfter(",")?.trim()?.toDouble() ?: 0.0
            return lat > 0.0 && lon > 0.0
        } catch (e: Exception) {
            return false
        }
    }

    fun getLastSeen(): String? {
        last_seen ?: return null

        val kolkataTimeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val sdfKolkata = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        sdfKolkata.timeZone = kolkataTimeZone
        val kolkataDateStr = sdfKolkata.format(Date(last_seen))
        val parsedDate = sdfKolkata.parse(kolkataDateStr)!!

        val calendar = Calendar.getInstance()
        calendar.time = parsedDate

        val localOffset = TimeZone.getDefault().getOffset(calendar.timeInMillis)
        val utcOffset = kolkataTimeZone.getOffset(calendar.timeInMillis)

        val offsetDifference = localOffset - utcOffset
        val timestamp = calendar.timeInMillis + offsetDifference


        val now = System.currentTimeMillis()
        val diff = now - timestamp

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)


        return when {
            seconds < 60 -> if (seconds < 10) "just now" else "$seconds seconds ago"
            minutes < 60 -> "$minutes minute${if (minutes == 1L) "" else "s"} ago"
            hours < 24 -> "$hours hour${if (hours == 1L) "" else "s"} ago"
            days in 1..6 -> "$days days ago"
            else -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timestamp

                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                val eventYear = calendar.get(Calendar.YEAR)

                return if (currentYear == eventYear) {
                    val sdf = SimpleDateFormat("dd MMM hh:mm a", Locale.getDefault())
                    sdf.format(Date(timestamp))
                } else {
                    val sdf = SimpleDateFormat("dd MMM yyyy hh:mm a", Locale.getDefault())
                    sdf.format(Date(timestamp))
                }
            }
        }
    }
}