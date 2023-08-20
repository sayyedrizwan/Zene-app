package com.rizwansayyed.zene.presenter.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SongLyricsResponse(
    val lyrics: String?,
)

