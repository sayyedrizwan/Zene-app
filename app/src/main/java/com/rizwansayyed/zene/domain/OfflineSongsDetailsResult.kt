package com.rizwansayyed.zene.domain

data class OfflineSongsDetailsResult(
    val id: Long,
    val title: String,
    val artist: String,
    val albums: String,
    val duration: Long,
    val data: String
)