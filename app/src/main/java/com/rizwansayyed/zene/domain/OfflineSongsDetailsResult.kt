package com.rizwansayyed.zene.domain

import android.net.Uri

data class OfflineSongsDetailsResult(
    val id: Long,
    val title: String,
    val artist: String,
    val albums: String,
    val duration: Long,
    val data: String,
    val date: Long,
    val art: Uri?
)