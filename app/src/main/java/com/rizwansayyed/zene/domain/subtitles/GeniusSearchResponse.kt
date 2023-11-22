package com.rizwansayyed.zene.domain.subtitles

data class GeniusSearchResponse(
    val meta: Meta?,
    val response: Response?
) {
    data class Meta(
        val status: Int?
    )

    data class Response(
        val sections: List<Section?>?
    ) {
        data class Section(
            val hits: List<Hit?>?,
            val type: String?
        ) {
            data class Hit(
                val highlights: List<Highlight?>?,
                val index: String?,
                val result: Result?,
                val type: String?
            ) {
                data class Highlight(
                    val `property`: String?,
                    val ranges: List<Range?>?,
                    val snippet: Boolean?,
                    val value: String?
                ) {
                    data class Range(
                        val end: Int?,
                        val start: Int?
                    )
                }

                data class Result(
                    val _type: String?,
                    val annotation_count: Int?,
                    val api_path: String?,
                    val article_type: String?,
                    val article_url: String?,
                    val artist: Artist?,
                    val artist_names: String?,
                    val author: Author?,
                    val author_list_for_display: String?,
                    val cover_art_thumbnail_url: String?,
                    val cover_art_url: String?,
                    val current_user_metadata: CurrentUserMetadata?,
                    val dek: String?,
                    val description: String?,
                    val dfp_kv: List<DfpKv?>?,
                    val draft: Boolean?,
                    val duration: Int?,
                    val featured_artists: List<Any?>?,
                    val featured_slot: Any?,
                    val for_homepage: Boolean?,
                    val for_mobile: Boolean?,
                    val full_title: String?,
                    val generic_sponsorship: Boolean?,
                    val header_image_thumbnail_url: String?,
                    val header_image_url: String?,
                    val id: Int?,
                    val instrumental: Boolean?,
                    val lyrics_owner_id: Int?,
                    val lyrics_state: String?,
                    val lyrics_updated_at: Int?,
                    val name: String?,
                    val name_with_artist: String?,
                    val path: String?,
                    val poster_attributes: PosterAttributes?,
                    val poster_url: String?,
                    val preview_image: String?,
                    val primary_artist: PrimaryArtist?,
                    val provider: String?,
                    val provider_id: String?,
                    val provider_params: List<ProviderParam?>?,
                    val published_at: Int?,
                    val pyongs_count: Int?,
                    val relationships_index_url: String?,
                    val release_date_components: ReleaseDateComponents?,
                    val release_date_for_display: String?,
                    val release_date_with_abbreviated_month_for_display: String?,
                    val short_title: String?,
                    val song_art_image_thumbnail_url: String?,
                    val song_art_image_url: String?,
                    val sponsor_image: Any?,
                    val sponsor_image_style: String?,
                    val sponsor_link: String?,
                    val sponsor_phrase: String?,
                    val sponsored: Boolean?,
                    val sponsorship: Sponsorship?,
                    val stats: Stats?,
                    val title: String?,
                    val title_with_featured: String?,
                    val type: String?,
                    val updated_by_human_at: Int?,
                    val url: String?,
                    val video_attributes: VideoAttributes?,
                    val view_count: Int?,
                    val votes_total: Int?
                ) {
                    data class Artist(
                        val _type: String?,
                        val api_path: String?,
                        val header_image_url: String?,
                        val id: Int?,
                        val image_url: String?,
                        val index_character: String?,
                        val iq: Int?,
                        val is_meme_verified: Boolean?,
                        val is_verified: Boolean?,
                        val name: String?,
                        val slug: String?,
                        val url: String?
                    )

                    data class Author(
                        val _type: String?,
                        val about_me_summary: String?,
                        val api_path: String?,
                        val avatar: Avatar?,
                        val current_user_metadata: CurrentUserMetadata?,
                        val header_image_url: String?,
                        val human_readable_role_for_display: String?,
                        val id: Int?,
                        val iq: Int?,
                        val is_meme_verified: Boolean?,
                        val is_verified: Boolean?,
                        val login: String?,
                        val name: String?,
                        val role_for_display: String?,
                        val url: String?
                    ) {
                        data class Avatar(
                            val medium: Medium?,
                            val small: Small?,
                            val thumb: Thumb?,
                            val tiny: Tiny?
                        ) {
                            data class Medium(
                                val bounding_box: BoundingBox?,
                                val url: String?
                            ) {
                                data class BoundingBox(
                                    val height: Int?,
                                    val width: Int?
                                )
                            }

                            data class Small(
                                val bounding_box: BoundingBox?,
                                val url: String?
                            ) {
                                data class BoundingBox(
                                    val height: Int?,
                                    val width: Int?
                                )
                            }

                            data class Thumb(
                                val bounding_box: BoundingBox?,
                                val url: String?
                            ) {
                                data class BoundingBox(
                                    val height: Int?,
                                    val width: Int?
                                )
                            }

                            data class Tiny(
                                val bounding_box: BoundingBox?,
                                val url: String?
                            ) {
                                data class BoundingBox(
                                    val height: Int?,
                                    val width: Int?
                                )
                            }
                        }

                        data class CurrentUserMetadata(
                            val excluded_permissions: List<String?>?,
                            val interactions: Interactions?,
                            val permissions: List<Any?>?
                        ) {
                            data class Interactions(
                                val following: Boolean?
                            )
                        }
                    }

                    data class CurrentUserMetadata(
                        val excluded_permissions: List<String?>?,
                        val permissions: List<Any?>?
                    )

                    data class DfpKv(
                        val name: String?,
                        val values: List<String?>?
                    )

                    data class PosterAttributes(
                        val height: Int?,
                        val width: Int?
                    )

                    data class PrimaryArtist(
                        val _type: String?,
                        val api_path: String?,
                        val header_image_url: String?,
                        val id: Int?,
                        val image_url: String?,
                        val index_character: String?,
                        val iq: Int?,
                        val is_meme_verified: Boolean?,
                        val is_verified: Boolean?,
                        val name: String?,
                        val slug: String?,
                        val url: String?
                    )

                    data class ProviderParam(
                        val name: String?,
                        val value: String?
                    )

                    data class ReleaseDateComponents(
                        val day: Int?,
                        val month: Int?,
                        val year: Int?
                    )

                    data class Sponsorship(
                        val _type: String?,
                        val api_path: String?,
                        val sponsor_image: Any?,
                        val sponsor_image_style: String?,
                        val sponsor_link: String?,
                        val sponsor_phrase: String?,
                        val sponsored: Boolean?
                    )

                    data class Stats(
                        val concurrents: Int?,
                        val hot: Boolean?,
                        val pageviews: Int?,
                        val unreviewed_annotations: Int?
                    )

                    data class VideoAttributes(
                        val height: Int?,
                        val width: Int?
                    )
                }
            }
        }
    }
}

data class GeniusLyricsWithInfo(val lyrics: String, val info: String, val subtitles: Boolean)