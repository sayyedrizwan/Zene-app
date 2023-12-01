package com.rizwansayyed.zene.domain.pinterest

data class PinterestSearchResponse(
    val client_context: ClientContext?,
    val request_identifier: String?,
    val resource: Resource?,
    val resource_response: ResourceResponse?
) {
    data class ClientContext(
        val analysis_ua: AnalysisUa?,
        val app_type_detailed: Int?,
        val app_version: String?,
        val batch_exp: Boolean?,
        val browser_locale: String?,
        val browser_name: String?,
        val browser_type: Int?,
        val browser_version: String?,
        val country: String?,
        val country_from_hostname: String?,
        val country_from_ip: String?,
        val csp_nonce: String?,
        val current_url: String?,
        val debug: Boolean?,
        val deep_link: String?,
        val enabled_advertiser_countries: List<String?>?,
        val facebook_token: Any?,
        val full_path: String?,
        val http_referrer: String?,
        val impersonator_user_id: Any?,
        val invite_code: String?,
        val invite_sender_id: String?,
        val is_authenticated: Boolean?,
        val is_bot: String?,
        val is_full_page: Boolean?,
        val is_internal_ip: Boolean?,
        val is_mobile_agent: Boolean?,
        val is_sterling_on_steroids: Boolean?,
        val is_tablet_agent: Boolean?,
        val language: String?,
        val locale: String?,
        val origin: String?,
        val path: String?,
        val placed_experiences: Any?,
        val referrer: Any?,
        val region_from_ip: String?,
        val request_host: String?,
        val request_identifier: String?,
        val seo_debug: Boolean?,
        val social_bot: String?,
        val stage: String?,
        val sterling_on_steroids_ldap: Any?,
        val sterling_on_steroids_user_type: Any?,
        val unauth_id: String?,
        val user: User?,
        val user_agent: String?,
        val user_agent_can_use_native_app: Boolean?,
        val user_agent_platform: String?,
        val user_agent_platform_version: Any?,
        val utm_campaign: Any?,
        val visible_url: String?
    ) {
        data class AnalysisUa(
            val app_type: Int?,
            val browser_name: String?,
            val browser_version: String?,
            val device: String?,
            val device_type: Any?,
            val os_name: String?,
            val os_version: String?
        )

        data class User(
            val ip_country: String?,
            val ip_region: String?,
            val unauth_id: String?
        )
    }

    data class Resource(
        val name: String?,
        val options: Options?
    ) {
        data class Options(
            val appliedProductFilters: String?,
            val article: String?,
            val auto_correction_disabled: String?,
            val bookmarks: List<String?>?,
            val filters: String?,
            val price_max: Any?,
            val price_min: Any?,
            val query: String?,
            val scope: String?,
            val top_pin_id: String?
        )
    }

    data class ResourceResponse(
        val bookmark: String?,
        val code: Int?,
        val `data`: Data?,
        val endpoint_name: String?,
        val http_status: Int?,
        val message: String?,
        val metadata: Metadata?,
        val query_l1_vertical_ids: List<Long?>?,
        val search_nag: SearchNag?,
        val searchfeed_tabs: SearchfeedTabs?,
        val status: String?,
        val x_pinterest_sli_endpoint_name: String?
    ) {
        data class Data(
            val nag: Nag?,
            val no_gift_wrap: Boolean?,
            val query_l1_vertical_ids: List<Long?>?,
            val results: List<Result?>?,
            val sensitivity: Sensitivity?,
            val should_append_global_search: Boolean?,
            val tabs: List<Tab?>?
        ) {
            class Nag

            data class Result(
                val access: List<Any?>?,
                val ad_match_reason: Int?,
                val aggregated_pin_data: AggregatedPinData?,
                val alt_text: Any?,
                val attribution: Any?,
                val board: Board?,
                val call_to_action_text: Any?,
                val campaign_id: Any?,
                val carousel_data: Any?,
                val created_at: String?,
                val debug_info_html: Any?,
                val description: String?,
                val did_its: List<Any?>?,
                val domain: String?,
                val dominant_color: String?,
                val embed: Any?,
                val grid_title: String?,
                val has_required_attribution_provider: Boolean?,
                val id: String?,
                val image_crop: ImageCrop?,
                val image_signature: String?,
                val images: Images?,
                val insertion_id: Any?,
                val is_downstream_promotion: Boolean?,
                val is_eligible_for_filters: Boolean?,
                val is_eligible_for_pdp: Boolean?,
                val is_eligible_for_related_products: Boolean?,
                val is_eligible_for_web_closeup: Boolean?,
                val is_oos_product: Boolean?,
                val is_prefetch_enabled: Boolean?,
                val is_promoted: Boolean?,
                val is_stale_product: Boolean?,
                val is_uploaded: Boolean?,
                val link: String?,
                val link_domain: LinkDomain?,
                val pinner: Pinner?,
                val promoted_is_lead_ad: Boolean?,
                val promoted_is_removable: Boolean?,
                val promoted_lead_form: Any?,
                val promoter: Any?,
                val reaction_counts: ReactionCounts?,
                val rich_summary: RichSummary?,
                val shopping_flags: List<Int?>?,
                val sponsorship: Any?,
                val story_pin_data: StoryPinData?,
                val story_pin_data_id: String?,
                val title: String?,
                val tracking_params: String?,
                val type: String?,
                val videos: Any?
            ) {
                data class AggregatedPinData(
                    val has_xy_tags: Boolean?
                )

                data class Board(
                    val name: String?,
                    val url: String?
                )

                data class ImageCrop(
                    val max_y: Double?,
                    val min_y: Int?
                )

                data class Images(
                    val `170x`: ImageX?,
                    val `236x`: ImageX?,
                    val `474x`: ImageX?,
                    val `736x`: ImageX?,
                    val orig: ImageX?
                ) {
                    data class ImageX(
                        val height: Int?,
                        val url: String?,
                        val width: Int?
                    )
                }

                data class LinkDomain(
                    val official_user: Any?
                )

                data class Pinner(
                    val ads_only_profile_site: Any?,
                    val follower_count: Int?,
                    val full_name: String?,
                    val id: String?,
                    val image_large_url: String?,
                    val image_medium_url: String?,
                    val image_small_url: String?,
                    val is_ads_only_profile: Boolean?,
                    val is_verified_merchant: Boolean?,
                    val username: String?,
                    val verified_identity: VerifiedIdentity?
                ) {
                    class VerifiedIdentity
                }

                data class ReactionCounts(
                    val `1`: Int?
                )

                data class RichSummary(
                    val actions: List<Any?>?,
                    val apple_touch_icon_images: Any?,
                    val apple_touch_icon_link: Any?,
                    val display_description: String?,
                    val display_name: String?,
                    val favicon_images: FaviconImages?,
                    val favicon_link: String?,
                    val id: String?,
                    val products: List<Any?>?,
                    val site_name: String?,
                    val type: String?,
                    val type_name: String?,
                    val url: String?
                ) {
                    data class FaviconImages(
                        val orig: String?
                    )
                }

                data class StoryPinData(
                    val has_affiliate_products: Boolean?,
                    val has_product_pins: Boolean?,
                    val id: String?,
                    val last_edited: Any?,
                    val metadata: Metadata?,
                    val page_count: Int?,
                    val pages: List<Page?>?,
                    val pages_preview: List<PagesPreview?>?,
                    val static_page_count: Int?,
                    val total_video_duration: Int?,
                    val type: String?
                ) {
                    data class Metadata(
                        val basics: Any?,
                        val canvas_aspect_ratio: Double?,
                        val compatible_version: String?,
                        val diy_data: Any?,
                        val is_compatible: Boolean?,
                        val is_editable: Boolean?,
                        val is_promotable: Boolean?,
                        val pin_image_signature: String?,
                        val pin_title: String?,
                        val recipe_data: Any?,
                        val root_pin_id: String?,
                        val root_user_id: String?,
                        val showreel_data: Any?,
                        val template_type: Any?,
                        val version: String?
                    )

                    data class Page(
                        val blocks: List<Block?>?,
                        val id: String?,
                        val image: Any?,
                        val image_adjusted: Any?,
                        val image_signature: String?,
                        val image_signature_adjusted: String?,
                        val layout: Int?,
                        val music_attributions: List<Any?>?,
                        val should_mute: Boolean?,
                        val style: Style?,
                        val type: String?,
                        val video: Any?,
                        val video_signature: Any?
                    ) {
                        data class Block(
                            val block_style: BlockStyle?,
                            val block_type: Int?,
                            val image: Any?,
                            val image_signature: String?,
                            val text: String?,
                            val tracking_id: String?,
                            val type: String?
                        ) {
                            data class BlockStyle(
                                val corner_radius: Int?,
                                val height: Int?,
                                val rotation: Int?,
                                val width: Int?,
                                val x_coord: Int?,
                                val y_coord: Int?
                            )
                        }

                        data class Style(
                            val background_color: String?,
                            val media_fit: Any?
                        )
                    }

                    data class PagesPreview(
                        val blocks: List<Block?>?,
                        val id: String?,
                        val image: Any?,
                        val image_adjusted: Any?,
                        val image_signature: String?,
                        val image_signature_adjusted: String?,
                        val layout: Int?,
                        val music_attributions: List<Any?>?,
                        val should_mute: Boolean?,
                        val style: Style?,
                        val type: String?,
                        val video: Any?,
                        val video_signature: Any?
                    ) {
                        data class Block(
                            val block_style: BlockStyle?,
                            val block_type: Int?,
                            val image: Any?,
                            val image_signature: String?,
                            val text: String?,
                            val tracking_id: String?,
                            val type: String?
                        ) {
                            data class BlockStyle(
                                val corner_radius: Int?,
                                val height: Int?,
                                val rotation: Int?,
                                val width: Int?,
                                val x_coord: Int?,
                                val y_coord: Int?
                            )
                        }

                        data class Style(
                            val background_color: String?,
                            val media_fit: Any?
                        )
                    }
                }
            }

            class Sensitivity

            data class Tab(
                val id: String?,
                val name: String?,
                val tab_type: String?,
                val type: String?
            )
        }

        data class Metadata(
            val query_l1_vertical_ids: List<Long?>?,
            val searchfeed_tabs: SearchfeedTabs?
        ) {
            data class SearchfeedTabs(
                val id: String?,
                val tabs: List<Tab?>?,
                val type: String?
            ) {
                data class Tab(
                    val id: String?,
                    val name: String?,
                    val tab_type: String?,
                    val type: String?
                )
            }
        }

        data class SearchNag(
            val nag: Nag?,
            val theme: Any?
        ) {
            class Nag
        }

        data class SearchfeedTabs(
            val id: String?,
            val tabs: List<Tab?>?,
            val type: String?
        ) {
            data class Tab(
                val id: String?,
                val name: String?,
                val tab_type: String?,
                val type: String?
            )
        }
    }
}