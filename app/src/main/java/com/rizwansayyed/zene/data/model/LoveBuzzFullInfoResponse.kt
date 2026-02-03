package com.rizwansayyed.zene.data.model

data class LoveBuzzFullInfoResponse(
    val title: String?,
    val mainImage: String?,
    val relationshipStatus: String?,
    val relationshipDuration: RelationshipDuration?,
    val summary: String?,
    val children: List<Children>?,
    val otherRelationship: List<OtherRelationship>?,
    val about: List<String>?,
    val timeline: List<Timeline>?,
    val comparison: Comparison?,
    val references: Any?,
    val photoGallery: List<PhotoGallery>?,
) {

    data class RelationshipDuration(
        val relationshipDuration: Long?,
        val placeholder: String?,
    )

    data class Children(
        val name: String?,
        val age: Long?,
    )

    data class OtherRelationship(
        val id: String?,
        val name: String?,
        val image: String?,
    )

    data class Timeline(
        val date: String?,
        val event: String?,
    )

    data class Comparison(val personA: PersonsInfo?, val personB: PersonsInfo?)

    data class PersonsInfo(
        val name: String?,
        val age: String?,
        val height: String?,
        val zodiac: String?,
        val occupation: String?,
        val hair_color: String?,
        val eye_color: String?,
        val nationality: String?,
    )

    data class PhotoGallery(
        val url: String?,
        val alt: String?,
    )
}