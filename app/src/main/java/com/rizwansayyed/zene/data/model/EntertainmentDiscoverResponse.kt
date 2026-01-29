package com.rizwansayyed.zene.data.model

import androidx.compose.ui.graphics.Color
import com.rizwansayyed.zene.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


data class EntertainmentDiscoverResponse(
    val isExpire: Boolean?,
    val featuredTrailer: ZeneMusicData?,
    val trailers: ZeneMusicDataList?,
    val news: ZeneMusicDataList?,
    val trends: ZeneMusicDataList?,
    val movies: ZeneMusicDataList?,
    val dated: List<WhoDatedWhoData>?,
    val events: EventsResponses?,
    val eventsNews: ZeneMusicDataList?,
)

data class EventsResponses(
    val thisWeek: List<EventsResponsesItems>?,
    val city: List<EventsResponsesItems>?,
    val all: List<EventsResponsesItems>?,
)

data class EventsResponsesItems(
    val name: String?,
    val date: String?,
    val timezone: String?,
    val duration: Double?,
    val dateWithTime: String?,
    val ticket: String?,
    val id: String?,
    val thumbnail: String?,
    val address: String?,
    val geo: EventsGeo?,
) {

    data class EventsGeo(
        val lat: Double, val lng: Double
    )

    fun getMonth(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        val parsedDate = sdf.parse(date ?: "") ?: return ""
        val calendar = Calendar.getInstance()
        calendar.time = parsedDate
        return calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH) ?: ""
    }

    fun getDay(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH)
        val parsedDate = sdf.parse(date ?: "") ?: return ""
        val calendar = Calendar.getInstance()
        calendar.time = parsedDate
        return calendar.get(Calendar.DAY_OF_MONTH).toString()
    }
}

data class WhoDatedWhoData(
    val bannerImage: String?,
    val relationshipSummary: String?,
    val about: List<String>?,
    val references: List<Reference>?,
    val coupleComparison: CoupleComparison?,
    val gallery: List<Gallery>?,
    val meta: Meta?
) {

    data class RelationshipBadge(
        val color: Color,
        val icon: Int
    )

    fun relationshipBadge(): RelationshipBadge? = when {
        meta?.event?.lowercase()?.contains("hookup")
            ?: false -> RelationshipBadge(Color(0xFFFF8243), R.drawable.ic_bed_love_couple)

        meta?.event?.lowercase()?.contains("dating")
            ?: false -> RelationshipBadge(Color(0xFFE65C9C), R.drawable.ic_romance_couple)

        meta?.event?.lowercase()?.contains("breakup")
            ?: false -> RelationshipBadge(Color(0xFFD32F2F), R.drawable.ic_black_broken)

        meta?.event?.lowercase()?.contains("engagement") ?: false ->
            RelationshipBadge(Color(0xFFE51A5B), R.drawable.ic_diamond_ring)

        meta?.event?.lowercase()?.contains("marriage") ?: false ->
            RelationshipBadge(Color(0xFFFFC107), R.drawable.ic_bride)

        meta?.event?.lowercase()?.contains("child") ?: false ->
            RelationshipBadge(Color(0xFF64B5F6), R.drawable.ic_child_boy)

        meta?.event?.lowercase()?.contains("divorce") ?: false ->
            RelationshipBadge(Color(0xFF757575), R.drawable.ic_divorce_person_separate)

        else -> null
    }

    data class Reference(
        val title: String?,
        val url: String?,
    )

    data class CoupleComparison(
        val personA: PersonA?,
        val personB: PersonB?,
    )

    data class PersonA(
        val name: String?,
        val image: String?,
        val ageAtStart: String?,
        val zodiac: String?,
        val occupation: String?,
        val hairColor: String?,
        val eyeColor: String?,
        val nationality: String?,
        val height: String?,
        val religion: String?,
    )

    data class PersonB(
        val name: String?,
        val image: String?,
        val ageAtStart: String?,
        val zodiac: String?,
        val occupation: String?,
        val hairColor: String?,
        val eyeColor: String?,
        val nationality: String?,
        val height: String?,
        val religion: String?,
    )

    data class Gallery(
        val image: String?,
        val caption: String?,
    )

    data class Meta(
        val url: String?,
        val title: String?,
        val event: String?,
        val image: String?,
    ) {
        fun getStatus(): String {
            return event?.split("-")
                ?.firstOrNull()
                ?.trim()
                ?: ""
        }

        fun getDate(): String {
            return event?.substringAfter(" -")?.trim() ?: ""
        }
    }
}