package com.rizwansayyed.zene.data.model

data class StatusTypeResponse(val status: Boolean?, val message: String?)
data class StatusCouponResponse(val isAvailable: Boolean?, val error: Boolean?)

data class MediaStatusTypeResponse(
    val status: Boolean?,
    val message: String?,
    val ts: Long?,
    val media: String?,
    val thumbnail: String?,
    val expire: Long?
)