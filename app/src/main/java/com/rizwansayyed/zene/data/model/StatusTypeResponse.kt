package com.rizwansayyed.zene.data.model

data class StatusTypeResponse(val status: Boolean?, val message: String?)

data class MediaStatusTypeResponse(
    val status: Boolean?,
    val message: String?,
    val media: String?,
    val thumbnail: String?
)