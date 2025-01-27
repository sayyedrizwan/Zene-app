package com.rizwansayyed.zene.utils

object URLSUtils {

    private const val ZENE_URL = "https://www.zenemusic.co/"

    fun connectShareURL(username: String): String {
        return "$ZENE_URL/connect/$username"
    }


    const val FB_GRAPH_ID = "https://graph.facebook.com/me"


    const val ZENE_BASE_URL_API = "http://192.168.0.110:3000"
    const val ZENE_RECENT_HOME_MUSIC_API = "recent/home-music"
    const val ZENE_RECENT_HOME_PODCAST_API = "recent/home-podcast"
    const val ZENE_RECENT_HOME_RADIO_API = "recent/home-radio"
    const val ZENE_RECENT_HOME_VIDEOS_API = "recent/home-video"
    const val ZENE_RECENT_HOME_ENTERTAINMENT_API = "recent/home-entertainment"
    const val ZENE_RECENT_HOME_ENTERTAINMENT_MOVIES_API = "recent/home-entertainment-movies"

    const val ZENE_CONNECT_NEAR_MUSIC_API = "connect/near-music"
    const val ZENE_CONNECT_USERS_SEARCH_API = "connect/connect-users-search"
    const val ZENE_CONNECT_SEARCH_API = "connect/connect-search"
    const val ZENE_CONNECT_USER_INFO_API = "connect/connect-user-info"
    const val ZENE_CONNECT_SEND_API = "connect/connect-send"
    const val ZENE_CONNECT_ACCEPT_API = "connect/connect-accept"
    const val ZENE_CONNECT_USER_SETTINGS_API = "connect/connect-user-settings"
    const val ZENE_CONNECT_SEND_MESSAGE_API = "connect/connect-send-message"

    const val ZENE_USER_UPDATE_API = "users/user-info"
    const val ZENE_USER_UPDATE_TRUE_CALLER_API = "users/update-truecaller"
    const val ZENE_USER_SEND_NUMBER_OTP_API = "users/send-number-otp"
    const val ZENE_USER_VERIFY_NUMBER_OTP_API = "users/verify-number-otp"

    const val ZENE_CONTACT_CACHE = "zene_contact_cache"

    const val IP_BASE_URL = "http://ip-api.com/"
    const val IP_JSON_API = "json"
}