package com.rizwansayyed.zene.domain.soundcloud

data class SoundCloudSearchResponse(
    val collection: List<Collection?>?,
    val next_href: String?,
    val query_urn: String?,
    val total_results: Int?
) {
    data class Collection(
        val artwork_url: String?,
        val avatar_url: String?,
        val badges: Badges?,
        val caption: Any?,
        val city: Any?,
        val comment_count: Int?,
        val commentable: Boolean?,
        val comments_count: Int?,
        val country_code: Any?,
        val created_at: String?,
        val creator_subscription: CreatorSubscription?,
        val creator_subscriptions: List<CreatorSubscription?>?,
        val description: String?,
        val display_date: String?,
        val download_count: Int?,
        val downloadable: Boolean?,
        val duration: Int?,
        val embeddable_by: String?,
        val first_name: String?,
        val followers_count: Int?,
        val followings_count: Int?,
        val full_duration: Int?,
        val full_name: String?,
        val genre: String?,
        val groups_count: Int?,
        val has_downloads_left: Boolean?,
        val id: Int?,
        val kind: String?,
        val label_name: String?,
        val last_modified: String?,
        val last_name: String?,
        val license: String?,
        val likes_count: Int?,
        val media: Media?,
        val monetization_model: String?,
        val permalink: String?,
        val permalink_url: String?,
        val playback_count: Int?,
        val playlist_count: Int?,
        val playlist_likes_count: Int?,
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
        val track_count: Int?,
        val track_format: String?,
        val uri: String?,
        val urn: String?,
        val user: User?,
        val user_id: Int?,
        val username: String?,
        val verified: Boolean?,
        val visuals: Visuals?,
        val waveform_url: String?
    ) {
        data class Badges(
            val pro: Boolean?,
            val pro_unlimited: Boolean?,
            val verified: Boolean?
        )

        data class CreatorSubscription(
            val product: Product?
        ) {
            data class Product(
                val id: String?
            )
        }

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
            val urn: String?,
            val writer_composer: String?
        )

        data class User(
            val avatar_url: String?,
            val badges: Badges?,
            val city: String?,
            val comments_count: Int?,
            val country_code: String?,
            val created_at: String?,
            val creator_subscription: CreatorSubscription?,
            val creator_subscriptions: List<CreatorSubscription?>?,
            val description: String?,
            val first_name: String?,
            val followers_count: Int?,
            val followings_count: Int?,
            val full_name: String?,
            val groups_count: Int?,
            val id: Int?,
            val kind: String?,
            val last_modified: String?,
            val last_name: String?,
            val likes_count: Int?,
            val permalink: String?,
            val permalink_url: String?,
            val playlist_count: Int?,
            val playlist_likes_count: Int?,
            val reposts_count: Any?,
            val station_permalink: String?,
            val station_urn: String?,
            val track_count: Int?,
            val uri: String?,
            val urn: String?,
            val username: String?,
            val verified: Boolean?,
            val visuals: Visuals?
        ) {
            data class Badges(
                val pro: Boolean?,
                val pro_unlimited: Boolean?,
                val verified: Boolean?
            )

            data class CreatorSubscription(
                val product: Product?
            ) {
                data class Product(
                    val id: String?
                )
            }

            data class Visuals(
                val enabled: Boolean?,
                val tracking: Any?,
                val urn: String?,
                val visuals: List<Visual?>?
            ) {
                data class Visual(
                    val entry_time: Int?,
                    val urn: String?,
                    val visual_url: String?
                )
            }
        }

        data class Visuals(
            val enabled: Boolean?,
            val tracking: Any?,
            val urn: String?,
            val visuals: List<Visual?>?
        ) {
            data class Visual(
                val entry_time: Int?,
                val urn: String?,
                val visual_url: String?
            )
        }
    }
}