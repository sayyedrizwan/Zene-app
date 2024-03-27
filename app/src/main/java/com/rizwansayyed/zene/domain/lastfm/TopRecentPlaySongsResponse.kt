package com.rizwansayyed.zene.domain.lastfm

import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataWithArtists
import com.rizwansayyed.zene.domain.MusicType


data class TopRecentPlaySongsResponse(
    val results: Results?,
)

data class Results(
    val artist: List<Artist>?,
)

data class Artist(
    val listeners: String?,
    val name: String?,
    val scrobbles: String?,
    val tracks: List<Track>?,
    val weight: Long?,
    val url: String?,
    val image: String?,
)

data class Track(
    val artist: String?,
    val name: String?,
    val weight: Long?,
    val url: String?,
)

fun MusicData.toMusicArtists(a: Artist): MusicDataWithArtists {
    return MusicDataWithArtists(
        this.thumbnail ?: "",
        this.name ?: "",
        this.artists ?: "",
        a.listeners ?: "",
        a.image?.replace("174s/", "770x0/")?.replace(".png", ".jpg"),
        a.name ?: "",
        this.songId ?: "",
        this.type ?: MusicType.MUSIC
    )
}