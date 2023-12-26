package com.rizwansayyed.zene.domain.auddSongRecognition

data class AuddSongRecognitionResponse(
    val result: Result?,
    val status: String?
) {
    data class Result(
        val album: String?,
        val apple_music: AppleMusic?,
        val artist: String?,
        val label: String?,
        val release_date: String?,
        val song_link: String?,
        val spotify: Spotify?,
        val timecode: String?,
        val title: String?
    ) {
        data class AppleMusic(
            val albumName: String?,
            val artistName: String?,
            val artwork: Artwork?,
            val composerName: String?,
            val discNumber: Int?,
            val durationInMillis: Int?,
            val genreNames: List<String?>?,
            val isrc: String?,
            val name: String?,
            val playParams: PlayParams?,
            val previews: List<Preview?>?,
            val releaseDate: String?,
            val trackNumber: Int?,
            val url: String?
        ) {
            data class Artwork(
                val bgColor: String?,
                val height: Int?,
                val textColor1: String?,
                val textColor2: String?,
                val textColor3: String?,
                val textColor4: String?,
                val url: String?,
                val width: Int?
            )

            data class PlayParams(
                val id: String?,
                val kind: String?
            )

            data class Preview(
                val url: String?
            )
        }

        data class Spotify(
            val album: Album?,
            val artists: List<Artist?>?,
            val available_markets: List<String?>?,
            val disc_number: Int?,
            val duration_ms: Int?,
            val explicit: Boolean?,
            val external_ids: ExternalIds?,
            val external_urls: ExternalUrls?,
            val href: String?,
            val id: String?,
            val is_playable: Any?,
            val linked_from: Any?,
            val name: String?,
            val popularity: Int?,
            val preview_url: String?,
            val track_number: Int?,
            val uri: String?
        ) {
            data class Album(
                val album_group: String?,
                val album_type: String?,
                val artists: List<Artist?>?,
                val available_markets: List<String?>?,
                val external_urls: ExternalUrls?,
                val href: String?,
                val id: String?,
                val images: List<Image?>?,
                val name: String?,
                val release_date: String?,
                val release_date_precision: String?,
                val uri: String?
            ) {
                data class Artist(
                    val external_urls: ExternalUrls?,
                    val href: String?,
                    val id: String?,
                    val name: String?,
                    val uri: String?
                ) {
                    data class ExternalUrls(
                        val spotify: String?
                    )
                }

                data class ExternalUrls(
                    val spotify: String?
                )

                data class Image(
                    val height: Int?,
                    val url: String?,
                    val width: Int?
                )
            }

            data class Artist(
                val external_urls: ExternalUrls?,
                val href: String?,
                val id: String?,
                val name: String?,
                val uri: String?
            ) {
                data class ExternalUrls(
                    val spotify: String?
                )
            }

            data class ExternalIds(
                val isrc: String?
            )

            data class ExternalUrls(
                val spotify: String?
            )
        }
    }
}