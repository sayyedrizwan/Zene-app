package com.rizwansayyed.zene.data.model

data class LoveBuzzFullInfoResponse(
    val id: String?,
    val title: String?,
    val mainImage: String?,
    val relationshipStatus: String?,
    val relationshipDuration: RelationshipDuration?,
    val summary: String?,
    val children: List<Children>?,
    val timeline: List<Timeline>?,
    val comparison: Comparison?,
    val references: List<String?>?,
    val photoGallery: List<String?>?,
) {

    fun toMusicData(): ZeneMusicData {
        val name = "${comparison?.personA?.name} & ${comparison?.personB?.name}"
        return ZeneMusicData(summary, id, name, "", mainImage, MusicDataTypes.DATING.name)
    }

    data class RelationshipDuration(
        val relationshipDuration: Long?,
        val placeholder: String?,
    )

    data class Children(
        val name: String?,
        val gender: String?,
        val born: String?,
        val age: String?,
    )

    data class OtherRelationship(
        val image: String?,
        val name: String?
    )

    data class Timeline(
        val date: String?,
        val event: String?,
    )

    data class Comparison(val personA: PersonsInfo?, val personB: PersonsInfo?)

    data class PersonsInfo(
        val name: String?,
        val image: String?,
        val age: String?,
        val height: String?,
        val zodiac: String?,
        val occupation: String?,
        val hair_color: String?,
        val eye_color: String?,
        val nationality: String?,
        val about: String?,
        val otherRelationships: List<OtherRelationship>?,
        val religion: String?,
    )
}