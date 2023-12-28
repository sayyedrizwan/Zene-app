package com.rizwansayyed.zene.data.utils.calender

import android.content.ContentResolver
import android.content.Context
import android.provider.CalendarContract
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import com.rizwansayyed.zene.domain.CalendarEvents
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class CalenderEvents @Inject constructor(@ApplicationContext private val context: Context) :
    CalenderEventsInterface {
    override suspend fun todayCalenderEvent() = flow {
        val data = mutableListOf<CalendarEvents>()

        val projection = arrayOf(
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND
        )

        val now = System.currentTimeMillis()
        val uri = CalendarContract.Events.CONTENT_URI
        val selection =
            CalendarContract.Events.DTSTART + " >= ? AND " + CalendarContract.Events.DTEND + " <= ?"
        val selectionArgs = arrayOf(now.toString(), (now + 86400000).toString())

        val contentResolver: ContentResolver = context.getContentResolver()
        val cursor = contentResolver.query(uri, projection, selection, selectionArgs, null)


        if (cursor != null && cursor.moveToFirst()) {
            do {
                val eventId = cursor
                    .getLongOrNull(cursor.getColumnIndex(CalendarContract.Events._ID))
                val title = cursor
                    .getStringOrNull(cursor.getColumnIndex(CalendarContract.Events.TITLE))
                val startTime = cursor
                    .getLongOrNull(cursor.getColumnIndex(CalendarContract.Events.DTSTART))
                val endTime = cursor
                    .getLongOrNull(cursor.getColumnIndex(CalendarContract.Events.DTEND))

                data.add(CalendarEvents(eventId, title, startTime, endTime))

            } while (cursor.moveToNext())

            cursor.close()
        }

        emit(data)
    }.flowOn(Dispatchers.IO)


}