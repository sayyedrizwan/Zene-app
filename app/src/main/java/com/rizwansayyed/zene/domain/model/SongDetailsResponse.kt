package com.rizwansayyed.zene.domain.model

import com.rizwansayyed.zene.domain.roomdb.songsdetails.SongDetailsEntity

data class SongDetailsResponse(
    val thumbnail: String?,
    val songName: String?,
    val artistName: String?,
    val songID: String?,
    val videoID: String?,
)

fun SongDetailsResponse.toLocal(): SongDetailsEntity? {
    if (this.thumbnail == null) return null
    if (this.songName == null) return null
    if (this.artistName == null) return null
    if (this.songID == null) return null
    if (this.videoID == null) return null
    return SongDetailsEntity(
        this.songName,
        this.artistName,
        this.songID,
        this.videoID,
        this.thumbnail,
        System.currentTimeMillis(),
        null
    )
}