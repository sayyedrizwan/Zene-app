package com.rizwansayyed.zene.data.model

data class StatusTypeResponse(val status: Boolean?, val message: String?)
data class StatusCouponResponse(val isAvailable: Boolean?, val error: Boolean?)
data class RecommendationNotificationResponse(
    val type: String?,
    val title: String?,
    val body: String?,
    val ts: Long?
)

data class MediaStatusTypeResponse(
    val status: Boolean?,
    val message: String?,
    val ts: Long?,
    val media: String?,
    val thumbnail: String?,
    val expire: Long?
)