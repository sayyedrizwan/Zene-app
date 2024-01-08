package com.rizwansayyed.zene.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateFormatter {

    object DateStyle {
        const val SIMPLE_TIME = "HH:mm"
        const val SIMPLE_TIME_SINGLE = "H : m"
        const val MONTH_YEAR_TIME = "MMM yyy"
        const val YEAR_TIME = "yyy"
        const val MONTH_DAY_TIME = "EEE, dd MMM"

        const val HOUR_24 = "HH"
        const val HOUR_12 = "hh"
        const val SIMPLE_MINUTES = "mm"
        const val SIMPLE_SECONDS = "ss"

    }


    fun toDate(format: String, timestamp: Long? = System.currentTimeMillis()): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val date = Date(timestamp ?: System.currentTimeMillis())

        return sdf.format(date).trim()
    }
}