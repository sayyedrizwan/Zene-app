package com.rizwansayyed.zene.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.provider.CalendarContract
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun getEventTimeToMS(dateString: String?): Long {
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.parse(dateString!!)?.time ?: 0L
    } catch (_: Exception) {
        return 0L
    }
}

fun addEventToCalendar(
    context: Context, title: String, location: String?,
    startMillis: Long, endMillis: Long, description: String? = null
) {

    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, title)
        if (description != null)
            putExtra(CalendarContract.Events.DESCRIPTION, description)

        if (location != null)
            putExtra(CalendarContract.Events.EVENT_LOCATION, location)

        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
    }

    intent.flags = FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}