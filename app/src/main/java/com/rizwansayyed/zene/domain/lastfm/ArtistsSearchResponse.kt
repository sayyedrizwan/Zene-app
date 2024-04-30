package com.rizwansayyed.zene.domain.lastfm

import com.rizwansayyed.zene.domain.MusicData

data class ArtistsSearchResponse(
    val results: Results?
) {
    data class Results(
        val albummatches: Albummatches?,
        val artistmatches: Artistmatches?,
        val query: String?,
        val totalAlbums: String?,
        val totalArtists: String?,
        val totalTracks: String?,
        val trackmatches: Trackmatches?
    ) {
        data class Albummatches(
            val album: List<Album?>?
        ) {
            data class Album(
                val artist: String?,
                val image: String?,
                val listeners: String?,
                val name: String?,
                val sr: Sr?,
                val streamable: String?,
                val url: String?
            ) {
                data class Sr(
                    val artist_id: String?,
                    val fw: String?,
                    val md5: String?,
                    val qw: Double?,
                    val sw: String?,
                    val w: String?,
                    val w1: Int?,
                    val w2: Int?
                )
            }
        }

        data class Artistmatches(
            val artist: List<LastFMArtist?>?
        )

        data class Trackmatches(
            val track: List<Track?>?
        ) {
            data class Track(
                val artist: String?,
                val image: String?,
                val listeners: String?,
                val name: String?,
                val playlink: Playlink?,
                val sr: Sr?,
                val url: String?
            ) {
                data class Playlink(
                    val `class`: String?,
                    val `data-affiliate`: String?,
                    val `data-affiliate-id`: String?,
                    val `data-artist-name`: String?,
                    val `data-artist-url`: String?,
                    val `data-track-name`: String?,
                    val `data-track-url`: String?,
                    val `data-youtube-id`: String?,
                    val `data-youtube-url`: String?,
                    val href: String?,
                    val target: String?,
                    val title: String?
                )

                data class Sr(
                    val artist_id: String?,
                    val fw: String?,
                    val md5: String?,
                    val qw: Double?,
                    val sw: String?,
                    val w: String?,
                    val w1: Int?,
                    val w2: Int?
                )
            }
        }
    }
}

data class LastFMArtist(
    val image: String?,
    val listeners: String?,
    val name: String?,
    val sr: Sr?,
    val streamable: String?,
    val url: String?
) {
    data class Sr(
        val fw: String?,
        val md5: String?,
        val qw: Double?,
        val sw: String?,
        val w: String?,
        val w1: Int?,
        val w2: Int?
    )

    fun hdImage(): String {
        return "https://lastfm.freetls.fastly.net/i/u/${image?.substringAfterLast("/")}"
    }
}

data class ArtistsShortInfo(
    val name: String,
    val info: LastFMArtist,
    val desc: String
)