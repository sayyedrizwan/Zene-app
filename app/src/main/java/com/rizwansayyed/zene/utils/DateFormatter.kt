package com.rizwansayyed.zene.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateFormatter {

    object DateStyle {
        const val SIMPLE_TIME = "HH:mm"
        const val SIMPLE_TIME_SINGLE = "H : m"


    }


    fun toDate(format: String, timestamp: Long = System.currentTimeMillis()): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val date = Date(timestamp)

        return sdf.format(date).trim()
    }
}