package com.rizwansayyed.zene.presenter.model

data class MusicPlayerDetails(
    val thumbnail: String,
    val songName: String,
    val artists: String,
    val pId: String,
    val currentDuration: String,
    val duration: String,
)