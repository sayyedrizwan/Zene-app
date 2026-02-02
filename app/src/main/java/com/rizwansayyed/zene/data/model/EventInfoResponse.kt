package com.rizwansayyed.zene.data.model

import java.text.SimpleDateFormat
import java.util.Locale

data class EventInfoResponse(
    val artistName: String?,
    val eventImage: String?,
    val date: String?,
    val endDate: String?,
    val happeningIn: String?,
    val venue: String?,
    val city: String?,
    val state: String?,
    val country: String?,
    val postalCode: String?,
    val streetAddress: String?,
    val latitude: Double?,
    val longitude: Double?,
    val ticketUrl: String?,
    val upcomingConcerts: List<UpcomingConcerts>?,
) {

    fun toMusicData(id: String) =
        ZeneMusicData("${venue}, $city", id, artistName, "", eventImage, MusicDataTypes.EVENTS.name)

    data class UpcomingConcerts(
        val eventId: String?,
        val date: String?,
        val venue: String?,
        val location: String?,
        val month: String?,
        val day: String?,
        val title: String?,
    ) {
        fun toMusicData(img: String?) =
            ZeneMusicData(location, eventId, title, "", img, MusicDataTypes.EVENTS.name)
    }


    fun formatDateTime(): Pair<String, String> {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
            val dateFormat = inputFormat.parse(date!!)
            val dateOutput = SimpleDateFormat("dd MMM", Locale.ENGLISH)
            val timeOutput = SimpleDateFormat("hh:mm a", Locale.ENGLISH)

            return dateOutput.format(dateFormat!!) to timeOutput.format(dateFormat)
        } catch (_: Exception) {
            return Pair(date ?: "0", date ?: "0")
        }
    }
}