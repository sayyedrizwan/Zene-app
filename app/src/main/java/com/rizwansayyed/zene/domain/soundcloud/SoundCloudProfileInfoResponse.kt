package com.rizwansayyed.zene.domain.soundcloud

data class SoundCloudProfileInfoResponse(
    val collection: List<Collection?>?,
    val next_href: String?,
    val query_urn: Any?
) {
    data class Collection(
        val caption: Any?,
        val created_at: String?,
        val track: Track?,
        val type: String?,
        val user: User?,
        val uuid: String?
    ) {
        data class Track(
            val artwork_url: String?,
            val caption: Any?,
            val comment_count: Int?,
            val commentable: Boolean?,
            val created_at: String?,
            val description: Any?,
            val display_date: String?,
            val download_count: Int?,
            val downloadable: Boolean?,
            val duration: Int?,
            val embeddable_by: String?,
            val full_duration: Int?,
            val genre: String?,
            val has_downloads_left: Boolean?,
            val id: Int?,
            val kind: String?,
            val label_name: String?,
            val last_modified: String?,
            val license: String?,
            val likes_count: Int?,
            val media: Media?,
            val monetization_model: String?,
            val permalink: String?,
            val permalink_url: String?,
            val playback_count: Int?,
            val policy: String?,
            val `public`: Boolean?,
            val publisher_metadata: PublisherMetadata?,
            val purchase_title: Any?,
            val purchase_url: Any?,
            val release_date: String?,
            val reposts_count: Int?,
            val secret_token: Any?,
            val sharing: String?,
            val state: String?,
            val station_permalink: String?,
            val station_urn: String?,
            val streamable: Boolean?,
            val tag_list: String?,
            val title: String?,
            val track_authorization: String?,
            val track_format: String?,
            val uri: String?,
            val urn: String?,
            val user: User?,
            val user_id: Int?,
            val visuals: Any?,
            val waveform_url: String?
        ) {
            data class Media(
                val transcodings: List<Transcoding?>?
            ) {
                data class Transcoding(
                    val duration: Int?,
                    val format: Format?,
                    val preset: String?,
                    val quality: String?,
                    val snipped: Boolean?,
                    val url: String?
                ) {
                    data class Format(
                        val mime_type: String?,
                        val protocol: String?
                    )
                }
            }

            data class PublisherMetadata(
                val album_title: String?,
                val artist: String?,
                val c_line: String?,
                val c_line_for_display: String?,
                val contains_music: Boolean?,
                val explicit: Boolean?,
                val id: Int?,
                val isrc: String?,
                val p_line: String?,
                val p_line_for_display: String?,
                val release_title: String?,
                val upc_or_ean: String?,
                val urn: String?
            )

            data class User(
                val avatar_url: String?,
                val badges: Badges?,
                val city: Any?,
                val country_code: Any?,
                val first_name: String?,
                val followers_count: Int?,
                val full_name: String?,
                val id: Int?,
                val kind: String?,
                val last_modified: String?,
                val last_name: String?,
                val permalink: String?,
                val permalink_url: String?,
                val station_permalink: String?,
                val station_urn: String?,
                val uri: String?,
                val urn: String?,
                val username: String?,
                val verified: Boolean?
            ) {
                data class Badges(
                    val pro: Boolean?,
                    val pro_unlimited: Boolean?,
                    val verified: Boolean?
                )
            }
        }

        data class User(
            val avatar_url: String?,
            val badges: Badges?,
            val city: Any?,
            val country_code: Any?,
            val first_name: String?,
            val followers_count: Int?,
            val full_name: String?,
            val id: Int?,
            val kind: String?,
            val last_modified: String?,
            val last_name: String?,
            val permalink: String?,
            val permalink_url: String?,
            val station_permalink: String?,
            val station_urn: String?,
            val uri: String?,
            val urn: String?,
            val username: String?,
            val verified: Boolean?
        ) {
            data class Badges(
                val pro: Boolean?,
                val pro_unlimited: Boolean?,
                val verified: Boolean?
            )
        }
    }
}