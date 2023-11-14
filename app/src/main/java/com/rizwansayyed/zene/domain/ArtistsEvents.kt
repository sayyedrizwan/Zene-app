package com.rizwansayyed.zene.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

data class ArtistsEvents(
    val eventName: String?,
    val time: String?,
    val address: String?,
    val link: String?,
    val artists: List<ArtistsArtists>,
) {
    private fun convertTimeToMs(): Long {
        val formatter = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.ENGLISH)
        val calendar = Calendar.getInstance().apply {
            time = formatter.parse(this@ArtistsEvents.time!!)!!
        }
        return calendar.timeInMillis
    }

    fun date(): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = convertTimeToMs()
        }
        val formatter = SimpleDateFormat("dd", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(calendar.time)
    }

    fun month(): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = convertTimeToMs()
        }
        val formatter = SimpleDateFormat("MMM", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(calendar.time)
    }

    fun year(): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = convertTimeToMs()
        }
        val formatter = SimpleDateFormat("yyy", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
        return formatter.format(calendar.time)
    }
}


data class ArtistsArtists(val name: String, val img: String)

