package com.rizwansayyed.zene.domain

data class InstagramDataResponse(
    val `data`: Data?,
    val status: String?
) {
    data class Data(
        val user: User?
    ) {
        data class User(
            val ai_agent_type: Any?,
            val bio_links: List<BioLink?>?,
            val biography: String?,
            val biography_with_entities: BiographyWithEntities?,
            val blocked_by_viewer: Boolean?,
            val business_address_json: Any?,
            val business_category_name: String?,
            val business_contact_method: String?,
            val business_email: Any?,
            val business_phone_number: Any?,
            val category_enum: Any?,
            val category_name: String?,
            val connected_fb_page: Any?,
            val country_block: Boolean?,
            val edge_felix_video_timeline: EdgeFelixVideoTimeline?,
            val edge_follow: EdgeFollow?,
            val edge_followed_by: EdgeFollowedBy?,
            val edge_media_collections: EdgeMediaCollections?,
            val edge_mutual_followed_by: EdgeMutualFollowedBy?,
            val edge_owner_to_timeline_media: EdgeOwnerToTimelineMedia?,
            val edge_related_profiles: EdgeRelatedProfiles?,
            val edge_saved_media: EdgeSavedMedia?,
            val eimu_id: String?,
            val external_url: String?,
            val external_url_linkshimmed: String?,
            val fb_profile_biolink: Any?,
            val fbid: String?,
            val followed_by_viewer: Boolean?,
            val follows_viewer: Boolean?,
            val full_name: String?,
            val group_metadata: Any?,
            val guardian_id: Any?,
            val has_ar_effects: Boolean?,
            val has_blocked_viewer: Boolean?,
            val has_channel: Boolean?,
            val has_clips: Boolean?,
            val has_guides: Boolean?,
            val has_requested_viewer: Boolean?,
            val hide_like_and_view_counts: Boolean?,
            val highlight_reel_count: Int?,
            val id: String?,
            val is_business_account: Boolean?,
            val is_embeds_disabled: Boolean?,
            val is_guardian_of_viewer: Boolean?,
            val is_joined_recently: Boolean?,
            val is_private: Boolean?,
            val is_professional_account: Boolean?,
            val is_regulated_c18: Boolean?,
            val is_supervised_by_viewer: Boolean?,
            val is_supervised_user: Boolean?,
            val is_supervision_enabled: Boolean?,
            val is_verified: Boolean?,
            val is_verified_by_mv4b: Boolean?,
            val overall_category_name: Any?,
            val pinned_channels_list_count: Int?,
            val profile_pic_url: String?,
            val profile_pic_url_hd: String?,
            val pronouns: List<Any?>?,
            val requested_by_viewer: Boolean?,
            val restricted_by_viewer: Any?,
            val should_show_category: Boolean?,
            val should_show_public_contacts: Boolean?,
            val show_account_transparency_details: Boolean?,
            val transparency_label: Any?,
            val transparency_product: String?,
            val username: String?
        ) {
            data class BioLink(
                val link_type: String?,
                val lynx_url: String?,
                val title: String?,
                val url: String?
            )

            data class BiographyWithEntities(
                val entities: List<Any?>?,
                val raw_text: String?
            )

            data class EdgeFelixVideoTimeline(
                val count: Int?,
                val edges: List<Edge?>?,
                val page_info: PageInfo?
            ) {
                data class Edge(
                    val node: Node?
                ) {
                    data class Node(
                        val __typename: String?,
                        val accessibility_caption: Any?,
                        val coauthor_producers: List<Any?>?,
                        val comments_disabled: Boolean?,
                        val dash_info: DashInfo?,
                        val dimensions: Dimensions?,
                        val display_url: String?,
                        val edge_liked_by: EdgeLikedBy?,
                        val edge_media_preview_like: EdgeMediaPreviewLike?,
                        val edge_media_to_caption: EdgeMediaToCaption?,
                        val edge_media_to_comment: EdgeMediaToComment?,
                        val edge_media_to_tagged_user: EdgeMediaToTaggedUser?,
                        val encoding_status: Any?,
                        val fact_check_information: Any?,
                        val fact_check_overall_rating: Any?,
                        val felix_profile_grid_crop: Any?,
                        val gating_info: Any?,
                        val has_audio: Boolean?,
                        val has_upcoming_event: Boolean?,
                        val id: String?,
                        val is_published: Boolean?,
                        val is_video: Boolean?,
                        val location: Any?,
                        val media_overlay_info: Any?,
                        val media_preview: String?,
                        val nft_asset_info: Any?,
                        val owner: Owner?,
                        val pinned_for_users: List<Any?>?,
                        val product_type: String?,
                        val sharing_friction_info: SharingFrictionInfo?,
                        val shortcode: String?,
                        val taken_at_timestamp: Int?,
                        val thumbnail_resources: List<ThumbnailResource?>?,
                        val thumbnail_src: String?,
                        val title: String?,
                        val tracking_token: String?,
                        val video_duration: Double?,
                        val video_url: String?,
                        val video_view_count: Int?,
                        val viewer_can_reshare: Boolean?
                    ) {
                        data class DashInfo(
                            val is_dash_eligible: Boolean?,
                            val number_of_qualities: Int?,
                            val video_dash_manifest: Any?
                        )

                        data class Dimensions(
                            val height: Int?,
                            val width: Int?
                        )

                        data class EdgeLikedBy(
                            val count: Int?
                        )

                        data class EdgeMediaPreviewLike(
                            val count: Int?
                        )

                        data class EdgeMediaToCaption(
                            val edges: List<Edge?>?
                        ) {
                            data class Edge(
                                val node: Node?
                            ) {
                                data class Node(
                                    val text: String?
                                )
                            }
                        }

                        data class EdgeMediaToComment(
                            val count: Int?
                        )

                        data class EdgeMediaToTaggedUser(
                            val edges: List<Edge?>?
                        ) {
                            data class Edge(
                                val node: Node?
                            ) {
                                data class Node(
                                    val user: User?,
                                    val x: Double?,
                                    val y: Double?
                                ) {
                                    data class User(
                                        val followed_by_viewer: Boolean?,
                                        val full_name: String?,
                                        val id: String?,
                                        val is_verified: Boolean?,
                                        val profile_pic_url: String?,
                                        val username: String?
                                    )
                                }
                            }
                        }

                        data class Owner(
                            val id: String?,
                            val username: String?
                        )

                        data class SharingFrictionInfo(
                            val bloks_app_url: Any?,
                            val should_have_sharing_friction: Boolean?
                        )

                        data class ThumbnailResource(
                            val config_height: Int?,
                            val config_width: Int?,
                            val src: String?
                        )
                    }
                }

                data class PageInfo(
                    val end_cursor: String?,
                    val has_next_page: Boolean?
                )
            }

            data class EdgeFollow(
                val count: Int?
            )

            data class EdgeFollowedBy(
                val count: Int?
            )

            data class EdgeMediaCollections(
                val count: Int?,
                val edges: List<Any?>?,
                val page_info: PageInfo?
            ) {
                data class PageInfo(
                    val end_cursor: Any?,
                    val has_next_page: Boolean?
                )
            }

            data class EdgeMutualFollowedBy(
                val count: Int?,
                val edges: List<Any?>?
            )

            data class EdgeOwnerToTimelineMedia(
                val count: Int?,
                val edges: List<Edge?>?,
                val page_info: PageInfo?
            ) {
                data class Edge(
                    val node: Node?
                ) {
                    data class Node(
                        val __typename: String?,
                        val accessibility_caption: Any?,
                        val clips_music_attribution_info: ClipsMusicAttributionInfo?,
                        val coauthor_producers: List<Any?>?,
                        val comments_disabled: Boolean?,
                        val dash_info: DashInfo?,
                        val dimensions: Dimensions?,
                        val display_url: String?,
                        val edge_liked_by: EdgeLikedBy?,
                        val edge_media_preview_like: EdgeMediaPreviewLike?,
                        val edge_media_to_caption: EdgeMediaToCaption?,
                        val edge_media_to_comment: EdgeMediaToComment?,
                        val edge_media_to_tagged_user: EdgeMediaToTaggedUser?,
                        val edge_sidecar_to_children: EdgeSidecarToChildren?,
                        val fact_check_information: Any?,
                        val fact_check_overall_rating: Any?,
                        val felix_profile_grid_crop: Any?,
                        val gating_info: Any?,
                        val has_audio: Boolean?,
                        val has_upcoming_event: Boolean?,
                        val id: String?,
                        val is_video: Boolean?,
                        val location: Any?,
                        val media_overlay_info: Any?,
                        val media_preview: String?,
                        val nft_asset_info: Any?,
                        val owner: Owner?,
                        val pinned_for_users: List<Any?>?,
                        val product_type: String?,
                        val sharing_friction_info: SharingFrictionInfo?,
                        val shortcode: String?,
                        val taken_at_timestamp: Int?,
                        val thumbnail_resources: List<ThumbnailResource?>?,
                        val thumbnail_src: String?,
                        val tracking_token: String?,
                        val video_url: String?,
                        val video_view_count: Int?,
                        val viewer_can_reshare: Boolean?
                    ) {
                        data class ClipsMusicAttributionInfo(
                            val artist_name: String?,
                            val audio_id: String?,
                            val should_mute_audio: Boolean?,
                            val should_mute_audio_reason: String?,
                            val song_name: String?,
                            val uses_original_audio: Boolean?
                        )

                        data class DashInfo(
                            val is_dash_eligible: Boolean?,
                            val number_of_qualities: Int?,
                            val video_dash_manifest: Any?
                        )

                        data class Dimensions(
                            val height: Int?,
                            val width: Int?
                        )

                        data class EdgeLikedBy(
                            val count: Int?
                        )

                        data class EdgeMediaPreviewLike(
                            val count: Int?
                        )

                        data class EdgeMediaToCaption(
                            val edges: List<Edge?>?
                        ) {
                            data class Edge(
                                val node: Node?
                            ) {
                                data class Node(
                                    val text: String?
                                )
                            }
                        }

                        data class EdgeMediaToComment(
                            val count: Int?
                        )

                        data class EdgeMediaToTaggedUser(
                            val edges: List<Edge?>?
                        ) {
                            data class Edge(
                                val node: Node?
                            ) {
                                data class Node(
                                    val user: User?,
                                    val x: Double?,
                                    val y: Double?
                                ) {
                                    data class User(
                                        val followed_by_viewer: Boolean?,
                                        val full_name: String?,
                                        val id: String?,
                                        val is_verified: Boolean?,
                                        val profile_pic_url: String?,
                                        val username: String?
                                    )
                                }
                            }
                        }

                        data class EdgeSidecarToChildren(
                            val edges: List<Edge?>?
                        ) {
                            data class Edge(
                                val node: Node?
                            ) {
                                data class Node(
                                    val __typename: String?,
                                    val accessibility_caption: Any?,
                                    val dash_info: DashInfo?,
                                    val dimensions: Dimensions?,
                                    val display_url: String?,
                                    val edge_media_to_tagged_user: EdgeMediaToTaggedUser?,
                                    val fact_check_information: Any?,
                                    val fact_check_overall_rating: Any?,
                                    val gating_info: Any?,
                                    val has_audio: Boolean?,
                                    val has_upcoming_event: Boolean?,
                                    val id: String?,
                                    val is_video: Boolean?,
                                    val media_overlay_info: Any?,
                                    val media_preview: String?,
                                    val owner: Owner?,
                                    val sharing_friction_info: SharingFrictionInfo?,
                                    val shortcode: String?,
                                    val tracking_token: String?,
                                    val video_url: String?,
                                    val video_view_count: Int?
                                ) {
                                    data class DashInfo(
                                        val is_dash_eligible: Boolean?,
                                        val number_of_qualities: Int?,
                                        val video_dash_manifest: Any?
                                    )

                                    data class Dimensions(
                                        val height: Int?,
                                        val width: Int?
                                    )

                                    data class EdgeMediaToTaggedUser(
                                        val edges: List<Edge?>?
                                    ) {
                                        data class Edge(
                                            val node: Node?
                                        ) {
                                            data class Node(
                                                val user: User?,
                                                val x: Double?,
                                                val y: Double?
                                            ) {
                                                data class User(
                                                    val followed_by_viewer: Boolean?,
                                                    val full_name: String?,
                                                    val id: String?,
                                                    val is_verified: Boolean?,
                                                    val profile_pic_url: String?,
                                                    val username: String?
                                                )
                                            }
                                        }
                                    }

                                    data class Owner(
                                        val id: String?,
                                        val username: String?
                                    )

                                    data class SharingFrictionInfo(
                                        val bloks_app_url: Any?,
                                        val should_have_sharing_friction: Boolean?
                                    )
                                }
                            }
                        }

                        data class Owner(
                            val id: String?,
                            val username: String?
                        )

                        data class SharingFrictionInfo(
                            val bloks_app_url: Any?,
                            val should_have_sharing_friction: Boolean?
                        )

                        data class ThumbnailResource(
                            val config_height: Int?,
                            val config_width: Int?,
                            val src: String?
                        )
                    }
                }

                data class PageInfo(
                    val end_cursor: String?,
                    val has_next_page: Boolean?
                )
            }

            data class EdgeRelatedProfiles(
                val edges: List<Any?>?
            )

            data class EdgeSavedMedia(
                val count: Int?,
                val edges: List<Any?>?,
                val page_info: PageInfo?
            ) {
                data class PageInfo(
                    val end_cursor: Any?,
                    val has_next_page: Boolean?
                )
            }
        }
    }
}