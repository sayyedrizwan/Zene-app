package com.rizwansayyed.zene.domain.model

import com.rizwansayyed.zene.domain.roomdb.songsdetails.SongDetailsEntity

data class SongDetailsResponse(
    var thumbnail: String?,
    var songName: String?,
    var artistName: String?,
    var songID: String?,
    var videoID: String?,
)

fun SongDetailsResponse.toLocal(): SongDetailsEntity? {
    if (this.thumbnail == null) return null
    if (this.songName == null) return null
    if (this.artistName == null) return null
    if (this.songID == null) return null

    return SongDetailsEntity(
        this.songName!!,
        this.artistName!!,
        this.songID!!,
        this.videoID ?: "",
        this.thumbnail!!,
        System.currentTimeMillis(),
        null
    )
}