package com.rizwansayyed.zene.data.model

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
    val ticketUrl: Double?,
    val upcomingConcerts: Double?,
) {
    data class UpcomingConcerts(
        val eventId: String?,
        val date: String?,
        val venue: String?,
        val location: String?,
        val month: String?,
        val day: String?,
        val title: String?,
    )
}