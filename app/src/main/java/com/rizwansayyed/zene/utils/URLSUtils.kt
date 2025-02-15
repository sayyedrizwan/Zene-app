package com.rizwansayyed.zene.utils

object URLSUtils {

    private const val ZENE_URL = "https://www.zenemusic.co/"

    fun connectShareURL(username: String): String {
        return "$ZENE_URL/connect/$username"
    }

    const val SHAZAM_BASE_URL = "https://www.shazam.com/"

    enum class SongRecognitionType {
        NONE, LOADING, LISTENING, ERROR, NO_SONG
    }


    const val YT_VIDEO_BASE_URL = "https://www.youtube.com/"


    const val FB_GRAPH_ID = "https://graph.facebook.com/me"


    const val ZENE_BASE_URL_API = "http://192.168.0.101:3000"
    const val ZENE_RECENT_HOME_MUSIC_API = "recent/home-music"
    const val ZENE_RECENT_HOME_PODCAST_API = "recent/home-podcast"
    const val ZENE_RECENT_HOME_RADIO_API = "recent/home-radio"
    const val ZENE_RECENT_HOME_VIDEOS_API = "recent/home-video"
    const val ZENE_RECENT_HOME_ENTERTAINMENT_API = "recent/home-entertainment"
    const val ZENE_RECENT_HOME_ENTERTAINMENT_MOVIES_API = "recent/home-entertainment-movies"

    const val ZENE_SEARCH_ALL_API = "search/all"
    const val ZENE_SEARCH_A_SONG_API = "search/a-song"
    const val ZENE_SEARCH_PLACES_API = "search/places"
    const val ZENE_SEARCH_TRENDING_API = "search/trending"
    const val ZENE_SEARCH_KEYWORDS_API = "search/keywords"
    const val ZENE_SEARCH_TRENDING_GIF_API = "search/trending-gif"
    const val ZENE_SEARCH_GIF_API = "search/search-gif"

    const val ZENE_CONNECT_USERS_SEARCH_VIA_PHONE_NUMBER_API = "connect/users-search-via-phonenumber"
    const val ZENE_CONNECT_SEARCH_API = "connect/search"
    const val ZENE_CONNECT_USER_INFO_API = "connect/user-info"
    const val ZENE_CONNECT_FRIENDS_API = "connect/friends"
    const val ZENE_CONNECT_FRIENDS_REQUEST_API = "connect/friends-request"
    const val ZENE_CONNECT_SEND_API = "connect/send"
    const val ZENE_CONNECT_ACCEPT_API = "connect/accept"
    const val ZENE_CONNECT_USER_SETTINGS_API = "connect/user-settings"
    const val ZENE_CONNECT_SEND_MESSAGE_API = "connect/send-message"
    const val ZENE_CONNECT_SEND_LOCATION_API = "connect/send-location"
    const val ZENE_CONNECT_FRIENDS_VIBES_API = "connect/friends-vibes"
    const val ZENE_CONNECT_SHARE_VIBE_API = "connect/share-vibe"
    const val ZENE_CONNECT_ADD_A_COMMENT_API = "connect/add-a-comment"
    const val ZENE_CONNECT_GET_COMMENT_API = "connect/get-comment"


    const val ZENE_PLAYER_SIMILAR_VIDEOS_API = "player/similar-videos"


    const val ZENE_AI_MUSIC_LIST_API = "ai/list"

    const val ZENE_USER_UPDATE_API = "users/user-info"
    const val ZENE_USER_UPDATE_CONNECT_STATUS_API = "users/update-connect-status"
    const val ZENE_USER_UPDATE_TRUE_CALLER_API = "users/update-truecaller"
    const val ZENE_USER_SEND_NUMBER_OTP_API = "users/send-number-otp"
    const val ZENE_USER_VERIFY_NUMBER_OTP_API = "users/verify-number-otp"
    const val ZENE_USER_ADD_HISTORY_API = "users/add-history"

    const val ZENE_CONTACT_CACHE = "zene_contact_cache"

    const val IP_BASE_URL = "http://ip-api.com/"
    const val IP_JSON_API = "json"
}