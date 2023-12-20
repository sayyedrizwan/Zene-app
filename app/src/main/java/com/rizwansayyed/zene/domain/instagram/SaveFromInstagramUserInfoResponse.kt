package com.rizwansayyed.zene.domain.instagram

data class SaveFromInstagramUserInfoResponse(
    val result: Result?
) {
    data class Result(
        val status: String?,
        val user: User?
    ) {
        data class User(
            val account_badges: List<Any?>?,
            val account_category: String?,
            val account_type: Int?,
            val address_street: String?,
            val ads_incentive_expiration_date: Any?,
            val ads_page_id: String?,
            val ads_page_name: String?,
            val auto_expand_chaining: Any?,
            val bio_links: List<Any?>?,
            val biography: String?,
            val biography_with_entities: BiographyWithEntities?,
            val birthday_today_visibility_for_viewer: String?,
            val broadcast_chat_preference_status: BroadcastChatPreferenceStatus?,
            val business_contact_method: String?,
            val can_add_fb_group_link_on_profile: Boolean?,
            val can_hide_category: Boolean?,
            val can_hide_public_contacts: Boolean?,
            val can_use_affiliate_partnership_messaging_as_brand: Boolean?,
            val can_use_affiliate_partnership_messaging_as_creator: Boolean?,
            val can_use_branded_content_discovery_as_brand: Boolean?,
            val can_use_branded_content_discovery_as_creator: Boolean?,
            val category: String?,
            val category_id: String?,
            val city_id: String?,
            val city_name: String?,
            val contact_phone_number: String?,
            val creator_shopping_info: CreatorShoppingInfo?,
            val current_catalog_id: Any?,
            val direct_messaging: String?,
            val displayed_action_button_partner: Any?,
            val displayed_action_button_type: String?,
            val existing_user_age_collection_enabled: Boolean?,
            val external_url: String?,
            val fan_club_info: FanClubInfo?,
            val fbid_v2: String?,
            val feed_post_reshare_disabled: Boolean?,
            val follow_friction_type: Int?,
            val follower_count: Int?,
            val following_count: Int?,
            val full_name: String?,
            val has_anonymous_profile_picture: Boolean?,
            val has_biography_translation: Boolean?,
            val has_collab_collections: Boolean?,
            val has_exclusive_feed_content: Boolean?,
            val has_fan_club_subscriptions: Boolean?,
            val has_guides: Boolean?,
            val has_highlight_reels: Boolean?,
            val has_igtv_series: Boolean?,
            val has_music_on_profile: Boolean?,
            val has_private_collections: Boolean?,
            val has_public_tab_threads: Boolean?,
            val has_videos: Boolean?,
            val hd_profile_pic_url_info: HdProfilePicUrlInfo?,
            val hd_profile_pic_versions: List<HdProfilePicVersion?>?,
            val highlight_reshare_disabled: Boolean?,
            val include_direct_blacklist_status: Boolean?,
            val instagram_location_id: String?,
            val interop_messaging_user_fbid: Long?,
            val is_bestie: Boolean?,
            val is_business: Boolean?,
            val is_call_to_action_enabled: Boolean?,
            val is_category_tappable: Boolean?,
            val is_direct_roll_call_enabled: Boolean?,
            val is_eligible_for_lead_center: Boolean?,
            val is_eligible_for_smb_support_flow: Boolean?,
            val is_favorite: Boolean?,
            val is_in_canada: Boolean?,
            val is_interest_account: Boolean?,
            val is_memorialized: Boolean?,
            val is_new_to_instagram: Boolean?,
            val is_new_to_instagram_30d: Boolean?,
            val is_opal_enabled: Boolean?,
            val is_potential_business: Boolean?,
            val is_private: Boolean?,
            val is_profile_audio_call_enabled: Boolean?,
            val is_profile_broadcast_sharing_enabled: Boolean?,
            val is_profile_picture_expansion_enabled: Boolean?,
            val is_regulated_c18: Boolean?,
            val is_remix_setting_enabled_for_posts: Boolean?,
            val is_remix_setting_enabled_for_reels: Boolean?,
            val is_secondary_account_creation: Boolean?,
            val is_supervision_features_enabled: Boolean?,
            val is_verified: Boolean?,
            val is_whatsapp_linked: Boolean?,
            val latest_besties_reel_media: Int?,
            val latest_reel_media: Int?,
            val latitude: Int?,
            val lead_details_app_id: String?,
            val longitude: Int?,
            val media_count: Int?,
            val merchant_checkout_style: String?,
            val mini_shop_seller_onboarding_status: Any?,
            val mutual_followers_count: Int?,
            val nametag: Any?,
            val num_of_admined_pages: Any?,
            val open_external_url_with_in_app_browser: Boolean?,
            val page_id: String?,
            val page_name: String?,
            val pinned_channels_info: PinnedChannelsInfo?,
            val pk: String?,
            val pk_id: String?,
            val primary_profile_link_type: Int?,
            val professional_conversion_suggested_account_type: Int?,
            val profile_context: String?,
            val profile_context_facepile_users: List<Any?>?,
            val profile_context_links_with_user_ids: List<Any?>?,
            val profile_pic_id: String?,
            val profile_pic_url: String?,
            val profile_pic_url_signature: ProfilePicUrlSignature?,
            val profile_type: Int?,
            val pronouns: List<Any?>?,
            val public_email: String?,
            val public_phone_country_code: String?,
            val public_phone_number: String?,
            val recs_from_friends: RecsFromFriends?,
            val relevant_news_regulation_locations: List<Any?>?,
            val remove_message_entrypoint: Boolean?,
            val seller_shoppable_feed_type: String?,
            val shopping_post_onboard_nux_type: Any?,
            val should_show_category: Boolean?,
            val should_show_public_contacts: Boolean?,
            val show_account_transparency_details: Boolean?,
            val show_fb_link_on_profile: Boolean?,
            val show_fb_page_link_on_profile: Boolean?,
            val show_ig_app_switcher_badge: Boolean?,
            val show_post_insights_entry_point: Boolean?,
            val show_shoppable_feed: Boolean?,
            val show_text_post_app_badge: Boolean?,
            val show_text_post_app_switcher_badge: Boolean?,
            val smb_delivery_partner: Any?,
            val smb_support_delivery_partner: Any?,
            val smb_support_partner: Any?,
            val strong_id__: String?,
            val text_post_app_badge_label: String?,
            val text_post_app_joiner_number: Int?,
            val text_post_app_joiner_number_label: String?,
            val text_post_new_post_count: Int?,
            val third_party_downloads_enabled: Int?,
            val total_ar_effects: Int?,
            val total_clips_count: Int?,
            val total_igtv_videos: Int?,
            val transparency_product_enabled: Boolean?,
            val username: String?,
            val whatsapp_number: String?,
            val zip: String?
        ) {
            data class BiographyWithEntities(
                val entities: List<Entity?>?,
                val raw_text: String?
            ) {
                data class Entity(
                    val hashtag: Hashtag?
                ) {
                    data class Hashtag(
                        val id: String?,
                        val name: String?
                    )
                }
            }

            data class BroadcastChatPreferenceStatus(
                val json_response: String?
            )

            data class CreatorShoppingInfo(
                val linked_merchant_accounts: List<Any?>?
            )

            data class FanClubInfo(
                val autosave_to_exclusive_highlight: Any?,
                val connected_member_count: Any?,
                val fan_club_id: Any?,
                val fan_club_name: Any?,
                val fan_consideration_page_revamp_eligiblity: Any?,
                val has_enough_subscribers_for_ssc: Any?,
                val is_fan_club_gifting_eligible: Any?,
                val is_fan_club_referral_eligible: Any?,
                val subscriber_count: Any?
            )

            data class HdProfilePicUrlInfo(
                val height: Int?,
                val url: String?,
                val url_signature: UrlSignature?,
                val width: Int?
            ) {
                data class UrlSignature(
                    val expires: Int?,
                    val signature: String?
                )
            }

            data class HdProfilePicVersion(
                val height: Int?,
                val url: String?,
                val url_signature: UrlSignature?,
                val width: Int?
            ) {
                data class UrlSignature(
                    val expires: Int?,
                    val signature: String?
                )
            }

            data class PinnedChannelsInfo(
                val has_public_channels: Boolean?,
                val pinned_channels_list: List<Any?>?
            )

            data class ProfilePicUrlSignature(
                val expires: Int?,
                val signature: String?
            )

            data class RecsFromFriends(
                val enable_recs_from_friends: Boolean?,
                val recs_from_friends_entry_point_type: String?
            )
        }
    }
}