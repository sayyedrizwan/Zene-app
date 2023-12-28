package com.rizwansayyed.zene.data.utils.calender

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.provider.CalendarContract
import android.text.format.DateUtils
import android.util.Log
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.rizwansayyed.zene.domain.CalendarEvents
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.Calendar
import javax.inject.Inject


class CalenderEvents @Inject constructor(@ApplicationContext private val context: Context) :
    CalenderEventsInterface {
    override suspend fun todayCalenderEvent() = flow {
        val data = mutableListOf<CalendarEvents>()

        val projection = arrayOf(
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
        )


        val currentDate = Calendar.getInstance()
        val startOfDay = currentDate.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val endOfDay = currentDate.apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis

        val selection =
            "((${CalendarContract.Events.DTSTART} >= $startOfDay) AND (${CalendarContract.Events.DTEND} <= $endOfDay))"
        val sortOrder = "${CalendarContract.Events.DTSTART} ASC"

        val cursor = context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI, projection, selection, null, sortOrder
        )

        cursor?.use { c ->
            while (c.moveToNext()) {
                val title = c.getStringOrNull(c.getColumnIndex(CalendarContract.Events.TITLE))
                val startTime = c.getLongOrNull(c.getColumnIndex(CalendarContract.Events.DTSTART))
                val endTime = c.getLongOrNull(c.getColumnIndex(CalendarContract.Events.DTEND))

                data.add(CalendarEvents(title, startTime, endTime))
            }
        }

        emit(data)
    }.flowOn(Dispatchers.IO)


}