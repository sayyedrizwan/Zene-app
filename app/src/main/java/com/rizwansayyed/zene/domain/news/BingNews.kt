package com.rizwansayyed.zene.domain.news

import java.time.Duration
import java.util.Calendar

data class BingNews(
    val url: String?,
    val img: String?,
    val title: String?,
    val desc: String?,
    val time: String?,
) {
    fun timeStamp(): Long {
        return try {
            val t = Regex("\\d+").find(time ?: "")?.value?.toIntOrNull() ?: 0

            val field = when (time?.substringAfter("$t")!!.lowercase()) {
                "s" -> Calendar.SECOND
                "m" -> Calendar.MINUTE
                "h" -> Calendar.HOUR_OF_DAY
                "d" -> Calendar.DAY_OF_YEAR
                "w" -> Calendar.WEEK_OF_YEAR
                "mon" -> Calendar.MONTH
                else -> Calendar.DAY_OF_YEAR
            }

            val calendar = Calendar.getInstance()
            calendar.add(field, -t)
            calendar.timeInMillis

        } catch (e: Exception) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            calendar.timeInMillis
        }
    }
}