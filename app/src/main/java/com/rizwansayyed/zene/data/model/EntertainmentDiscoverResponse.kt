package com.rizwansayyed.zene.data.model


data class EntertainmentDiscoverResponse(
    val isExpire: Boolean?,
    val news: List<ZeneMusicData>?,
    val trends: List<ZeneMusicData>?,
    val dated: List<WhoDatedWhoData>?,
)

data class WhoDatedWhoData(
    val bannerImage: String?,
    val relationshipSummary: String?,
    val about: List<String>?,
    val references: List<Reference>?,
    val coupleComparison: CoupleComparison?,
    val gallery: List<Gallery>?,
    val meta: Meta?
) {

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
    )
}