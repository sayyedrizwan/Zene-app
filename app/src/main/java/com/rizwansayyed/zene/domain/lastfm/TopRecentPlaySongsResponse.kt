package com.rizwansayyed.zene.domain.lastfm

data class TopRecentPlaySongsResponse(
    val results: Results?
) {
    data class Results(
        val track: List<Track?>?
    ) {
        data class Track(
            val artist: String?,
            val artist_url: String?,
            val image: String?,
            val listeners: String?,
            val name: String?,
            val scrobbles: String?,
            val url: String?,
            val weight: Int?
        )
    }
}