package com.rizwansayyed.zene.domain.lastfm

data class TopRecentPlaySongsResponse(
    val results: Results?
) {
    data class Results(
        val artist: List<Artist?>?
    ) {
        data class Artist(
            val image: String?,
            val listeners: String?,
            val name: String?,
            val scrobbles: String?,
            val tracks: List<Track?>?,
            val url: String?,
            val weight: Int?
        ) {
            data class Track(
                val artist: String?,
                val name: String?,
                val url: String?,
                val weight: Int?
            )
        }
    }
}