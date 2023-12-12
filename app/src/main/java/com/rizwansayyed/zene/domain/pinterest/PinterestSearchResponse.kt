package com.rizwansayyed.zene.domain.pinterest

data class PinterestSearchResponse(
    val request_identifier: String?,
    val resource: Resource?,
    val resource_response: ResourceResponse?
) {

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
        val display_shoptab: Boolean?,
        val endpoint_name: String?,
        val http_status: Int?,
        val message: String?,
        val query_l1_vertical_ids: List<Long?>?,
        val status: String?,
        val x_pinterest_sli_endpoint_name: String?
    ) {
        data class Data(
            val results: List<Result?>?,
        ) {
            data class Result(
                val ad_match_reason: Int?,
                val alt_text: String?,
                val attribution: Any?,
                val background_colour: Any?,
                val bookmarks_for_objects: Any?,
                val button_text: Any?,
                val call_to_action_text: Any?,
                val campaign_id: Any?,
                val carousel_data: Any?,
                val closeup_id: String?,
                val container_type: Int?,
                val content_ids: List<String?>?,
                val created_at: String?,
                val debug_info_html: Any?,
                val description: String?,
                val did_its: List<Any?>?,
                val domain: String?,
                val dominant_color: String?,
                val embed: Any?,
                val expanded_viewport_objects: List<Any?>?,
                val experience: Any?,
                val grid_title: String?,
                val has_required_attribution_provider: Boolean?,
                val id: String?,
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
                val item_actions: List<Any?>?,
                val link: String?,
                val promoted_is_lead_ad: Boolean?,
                val promoted_is_removable: Boolean?,
                val promoted_lead_form: Any?,
                val promoter: Any?,
                val story_pin_data: StoryPinData?,
            ) {
                data class Images(
                    val `170x`: Orig?,
                    val `236x`: Orig?,
                    val `474x`: Orig?,
                    val `736x`: Orig?,
                    val orig: Orig?
                ) {
                    data class Orig(
                        val height: Int?,
                        val url: String?,
                        val width: Int?
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
        }

    }
}