package com.rizwansayyed.zene.domain.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class UrlResponse(val url: String?)