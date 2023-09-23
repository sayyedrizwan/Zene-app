package com.rizwansayyed.zene.domain.spotify

import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.domain.OnlineRadioCacheResponse
import com.rizwansayyed.zene.domain.OnlineRadioResponseItem


fun List<SpotifyPlaylistSongsResponse.Tracks.SpotifyItem>.toTxtCache(): String? {
    val r = SpotifyTracksCacheResponse(System.currentTimeMillis(), this)
    return moshi.adapter(r.javaClass).toJson(r)
}

data class SpotifyTracksCacheResponse(
    val cacheTime: Long,
    val list: List<SpotifyPlaylistSongsResponse.Tracks.SpotifyItem>
)


data class SpotifyPlaylistSongsResponse(
    val collaborative: Boolean?,
    val description: String?,
    val external_urls: ExternalUrls?,
    val followers: Followers?,
    val href: String?,
    val id: String?,
    val images: List<Image?>?,
    val name: String?,
    val owner: Owner?,
    val primary_color: Any?,
    val `public`: Boolean?,
    val snapshot_id: String?,
    val tracks: Tracks?,
    val type: String?,
    val uri: String?
) {
    data class ExternalUrls(
        val spotify: String?
    )

    data class Followers(
        val href: Any?,
        val total: Int?
    )

    data class Image(
        val height: Any?,
        val url: String?,
        val width: Any?
    )

    data class Owner(
        val display_name: String?,
        val external_urls: ExternalUrls?,
        val href: String?,
        val id: String?,
        val type: String?,
        val uri: String?
    ) {
        data class ExternalUrls(
            val spotify: String?
        )
    }

    data class Tracks(
        val href: String?,
        val items: List<SpotifyItem>?,
        val limit: Int?,
        val next: Any?,
        val offset: Int?,
        val previous: Any?,
        val total: Int?
    ) {
        data class SpotifyItem(
            val added_at: String?,
            val added_by: AddedBy?,
            val is_local: Boolean?,
            val primary_color: Any?,
            val track: Track?,
            val video_thumbnail: VideoThumbnail?
        ) {
            data class AddedBy(
                val external_urls: ExternalUrls?,
                val href: String?,
                val id: String?,
                val type: String?,
                val uri: String?
            ) {
                data class ExternalUrls(
                    val spotify: String?
                )
            }

            data class Track(
                val album: Album?,
                val artists: List<Artist?>?,
                val available_markets: List<String?>?,
                val disc_number: Int?,
                val duration_ms: Int?,
                val episode: Boolean?,
                val explicit: Boolean?,
                val external_ids: ExternalIds?,
                val external_urls: ExternalUrls?,
                val href: String?,
                val id: String?,
                val is_local: Boolean?,
                val name: String?,
                val popularity: Int?,
                val preview_url: String?,
                val track: Boolean?,
                val track_number: Int?,
                val type: String?,
                val uri: String?
            ) {
                data class Album(
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
                    val total_tracks: Int?,
                    val type: String?,
                    val uri: String?
                ) {
                    data class Artist(
                        val external_urls: ExternalUrls?,
                        val href: String?,
                        val id: String?,
                        val name: String?,
                        val type: String?,
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
                    val type: String?,
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

            data class VideoThumbnail(
                val url: Any?
            )
        }
    }
}