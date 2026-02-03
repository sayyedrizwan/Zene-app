package com.rizwansayyed.zene.utils

import com.rizwansayyed.zene.BuildConfig

object URLSUtils {

    const val ZENE_MAIL = "knocknock@zenemusic.co"
    const val ZENE_FAQ_URL = "https://www.zenemusic.co/faq"
    const val ZENE_PRIVACY_TERMS_URL = "https://www.zenemusic.co/legal/terms"
    const val ZENE_PRIVACY_POLICY_URL = "https://www.zenemusic.co/legal/policy"
    const val ZENE_HOME = "https://www.zenemusic.co/home"


    const val ZENE_URL = "https://www.zenemusic.co"
    const val ZENE_URL_CONNECT = "/connect"
    const val ZENE_URL_SEARCH = "/search"
    const val ZENE_URL_ENTERTAINMENT = "/entertainment"
    const val ZENE_URL_SETTINGS = "/settings"
    const val ZENE_URL_PODCASTS = "/podcasts"
    const val ZENE_URL_MY_LIBRARY = "/my-library"
    const val ZENE_URL_PRIVACY_POLICY = "/privacy-policy"


    const val ZENE_SONG = "/song/"
    const val ZENE_RADIO = "/radio/"
    const val ZENE_VIDEO = "/video/"
    const val ZENE_MIX = "/mix/"
    const val ZENE_ARTIST = "/artist/"

    const val ZENE_LIFESTYLE = "/lifestyle/"
    const val ZENE_PODCAST_SERIES = "/podcast-series/"
    const val ZENE_PODCAST = "/podcast/"
    const val ZENE_NEWS = "/news/"
    const val ZENE_M = "/m/"
    const val ZENE_MOVIE_IF_ENC = "v__"
    const val ZENE_AI_MUSIC = "/ai_music/"
    const val ZENE_LOVE_BUZZ = "/love-buzz/"
    const val ZENE_EVENTS = "/events/"

    fun connectShareURL(username: String) = "$ZENE_URL${ZENE_URL_CONNECT}/$username"

    const val SHAZAM_BASE_URL = "https://www.shazam.com/"

    enum class SongRecognitionType {
        NONE, LOADING, LISTENING, ERROR, NO_SONG
    }


    const val TUNE_MY_MUSIC_TRANSFER = "https://www.tunemymusic.com/transfer"

    const val YT_VIDEO_BASE_URL = "https://www.youtube-nocookie.com"
    const val YT_WEB_BASE_URL = "https://www.youtube.com/"

    const val LIKED_SONGS_ON_ZENE = "_liked_songs_on_zene"

    val ZENE_BASE_URL_SOCKET =  BuildConfig.API_ZENE_MUSIC_BASE_URL
//        if (BuildConfig.DEBUG) "http://192.168.0.101:4102" else BuildConfig.API_ZENE_MUSIC_BASE_URL
    val ZENE_BASE_URL_API = BuildConfig.API_ZENE_MUSIC_BASE_URL
//        if (BuildConfig.DEBUG) "http://192.168.0.108:4100" else BuildConfig.API_ZENE_MUSIC_BASE_URL

    const val ZENE_RECENT_HOME_MUSIC_API = "recent/home-music"
    const val ZENE_RECENT_HOME_PODCAST_API = "recent/home-podcast"
    const val ZENE_RECENT_HOME_RADIO_API = "recent/home-radio"
    const val ZENE_RECENT_HOME_VIDEOS_API = "recent/home-video"

    const val ZENE_SEARCH_ALL_API = "search/all"
    const val ZENE_SEARCH_A_SONG_API = "search/a-song"
    const val ZENE_SEARCH_PLACES_API = "search/places"
    const val ZENE_SEARCH_TRENDING_API = "search/trending"
    const val ZENE_SEARCH_KEYWORDS_API = "search/keywords"
    const val ZENE_SEARCH_TRENDING_GIF_API = "search/trending-gif"
    const val ZENE_SEARCH_GIF_API = "search/search-gif"
    const val ZENE_SEARCH_IMG_API = "search/search-img"


    const val ZENE_CONNECT_USERS_SEARCH_VIA_PHONE_NUMBER_API =
        "connect/users-search-via-phonenumber"
    const val ZENE_CONNECT_CREATE_PLAYLIST_API = "connect/create-playlist"
    const val ZENE_CONNECT_PLAYLIST_API = "connect/playlists"
    const val ZENE_CONNECT_SEARCH_API = "connect/search"
    const val ZENE_CONNECT_USER_INFO_API = "connect/user-info"
    const val ZENE_CONNECT_FRIENDS_API = "connect/friends"
    const val ZENE_CONNECT_FRIENDS_REQUEST_API = "connect/friends-request"
    const val ZENE_CONNECT_SEND_API = "connect/send"
    const val ZENE_CONNECT_ACCEPT_API = "connect/accept"
    const val ZENE_CONNECT_USER_SETTINGS_API = "connect/user-settings"
    const val ZENE_CONNECT_SEND_MESSAGE_API = "connect/send-message"
    const val ZENE_CONNECT_DELETE_MESSAGE_API = "connect/delete-message"
    const val ZENE_CONNECT_SEND_MEDIA_MESSAGE_API = "connect/send-media-message"
    const val ZENE_CONNECT_SEND_FILE_MESSAGE_API = "connect/send-file-message"
    const val ZENE_CONNECT_SEND_JAM_MESSAGE_API = "connect/send-jam-message"
    const val ZENE_CONNECT_CHAT_RECENT_MESSAGE_API = "connect/chat-recent-message"
    const val ZENE_CONNECT_MARK_MESSAGE_AS_READ_API = "connect/mark-message-as-read"
    const val ZENE_CONNECT_SEND_LOCATION_API = "connect/send-location"
    const val ZENE_CONNECT_SEND_PARTY_CALL_API = "connect/send-party-call"
    const val ZENE_CONNECT_DECLINE_PARTY_CALL_API = "connect/decline-party-call"
    const val ZENE_CONNECT_FRIENDS_VIBES_API = "connect/friends-vibes"
    const val ZENE_CONNECT_SHARE_VIBE_API = "connect/share-vibe"
    const val ZENE_CONNECT_ADD_A_COMMENT_API = "connect/add-a-comment"
    const val ZENE_CONNECT_GET_COMMENT_API = "connect/get-comment"


    const val ZENE_PLAYER_PODCAST_INFO_API = "player/podcast-info"
    const val ZENE_PLAYER_RADIO_INFO_API = "player/radio-info"
    const val ZENE_PLAYER_SIMILAR_VIDEOS_API = "player/similar-videos"
    const val ZENE_PLAYER_SIMILAR_ARTISTS_ALBUM_SONG_API = "player/similar-artists-album-of-song"
    const val ZENE_PLAYER_SONG_INFO_API = "player/song-info"
    const val ZENE_PLAYER_IS_PLAYLIST_ADDED_API = "player/is-playlist-added"
    const val ZENE_PLAYER_SIMILAR_SONGS_API = "player/similar-songs"
    const val ZENE_PLAYER_SIMILAR_PLAYLISTS_SONGS_API = "player/similar-playlists-songs"
    const val ZENE_PLAYER_SONGS_LYRICS_API = "player/song-lyrics"
    const val ZENE_PLAYER_VIDEO_FOR_SONGS_API = "player/video-for-songs"
    const val ZENE_PLAYER_SIMILAR_RADIO_API = "player/similar-radio"


    const val ZENE_INFO_PLAYLISTS_API = "info/playlists-info"
    const val ZENE_INFO_ARTIST_API = "info/artist-info"
    const val ZENE_INFO_ARTIST_FOLLOW_API = "info/artist-follow"
    const val ZENE_INFO_MOVIE_SHOW_INFO_API = "info/movie-show-info"
    const val ZENE_INFO_SEASON_MOVIE_SHOW_INFO_API = "info/season-movie-show-info"

    const val ZENE_PODCAST_PODCAST_INFO_API = "podcast-radio/podcast-audio-info"
    const val ZENE_PODCAST_PODCAST_MEDIA_URL_API = "podcast-radio/podcast-media-url"
    const val ZENE_RADIO_MEDIA_URL_API = "podcast-radio/radio-media-url"
    const val ZENE_SIMILAR_PODCASTS_API = "podcast-radio/similar-podcasts"
    const val ZENE_RADIO_COUNTRY_API = "podcast-radio/radio-country"
    const val ZENE_RADIO_PODCASTS_CATEGORY_API = "podcast-radio/podcasts-category"


    const val ZENE_AI_MUSIC_LIST_API = "ai/list"
    const val ZENE_AI_MUSIC_MEDIA_URL_API = "ai/ai-music-media-url"
    const val ZENE_AI_SIMILAR_MUSIC_API = "ai/similar-ai-music"
    const val ZENE_AI_MUSIC_LYRICS_API = "ai/ai-music-lyrics"
    const val ZENE_AI_MUSIC_INFO_API = "ai/ai-music-info"

    const val ZENE_USER_LOGIN_API = "users/user-login"
    const val ZENE_USER_UPDATE_API = "users/user-update"
    const val ZENE_USER_UPDATE_CONNECT_STATUS_API = "users/update-connect-status"
    const val ZENE_USER_UPDATE_TRUE_CALLER_API = "users/update-truecaller"
    const val ZENE_USER_SEND_NUMBER_OTP_API = "users/send-number-otp"
    const val ZENE_USER_CHECK_NUMBER_VERIFIED_THIS_WEEK_API =
        "users/check-number-verified-this-week"
    const val ZENE_USER_VERIFY_NUMBER_OTP_API = "users/verify-number-otp"
    const val ZENE_USER_ADD_HISTORY_API = "users/add-history"
    const val ZENE_USER_UPDATE_SUBSCRIPTION_PLAY_STORE_API = "users/update-subscription-play-store"
    const val ZENE_USER_IS_USER_PREMIUM_API = "users/is-user-premium"
    const val ZENE_USER_CHECK_USERNAME_API = "users/check-username"
    const val ZENE_USER_UPDATE_USERNAME_API = "users/update-username"
    const val ZENE_USER_UPDATE_NAME_API = "users/update-name"
    const val ZENE_USER_UPDATE_PROFILE_PHOTO_API = "users/update-profile-photo"
    const val ZENE_USER_GET_HISTORY_API = "users/get-history"
    const val ZENE_USER_UPDATE_COUPON_API = "users/update-coupon"
    const val ZENE_USER_DELETE_ACCOUNT_API = "users/delete-account"
    const val ZENE_USER_DELETE_INFO_API = "users/delete-account-info"
    const val ZENE_USER_CANCEL_DELETE_API = "users/cancel-delete-account"
    const val ZENE_USER_UPDATE_EMAIL_SUB_API = "users/update-email-subscription"


    const val ZENE_ENT_DISCOVER_TRENDING_NEWS_API = "entertainment/discover-trending-news"
    const val ZENE_ENT_BUZZ_NEWS_API = "entertainment/buzz-news"
    const val ZENE_ENT_LIFESTYLE_API = "entertainment/discover-lifestyle"
    const val ZENE_ENT_DATING_API = "entertainment/dating"
    const val ZENE_ENT_ALL_TRAILERS_API = "entertainment/all-trailers"
    const val ZENE_ENT_STREAMING_TRENDING_API = "entertainment/streaming-trending"
    const val ZENE_ENT_TOP_BOX_OFFICE_MOVIES_API = "entertainment/top-box-office-movies"
    const val ZENE_ENT_UPCOMING_MOVIES_API = "entertainment/upcoming-movies"
    const val ZENE_ENT_LIFESTYLES_EVENTS_API = "entertainment/lifestyles-events"
    const val ZENE_ENT_EVENT_FULL_INFO_API = "entertainment/event-full-info"
    const val ZENE_ENT_LOVE_BUZZ_FULL_INFO_API = "entertainment/love-buzz-full-info"

    const val ZENE_STORE_TOP_DEALS_API = "store/top-deals"
    const val ZENE_STORE_STRIPE_LINK_API = "store/get-stripe-link"

    const val ZENE_NOTIFICATION_RECOMMENDATION_API = "notification/notification-recommendation"

    const val ZENE_SPONSOR_ADS_API = "sponsor/ads"

    const val ZENE_FEED_FOLLOWED_ARTISTS_API = "feed/followed-artists"
    const val ZENE_FEED_ARTISTS_UPDATES_API = "feed/artists-updates"

    const val ZENE_USER_PLAYLISTS_CREATE_NEW_PLAYLISTS_API = "users-playlists/create-new-playlists"
    const val ZENE_USER_PLAYLISTS_SONG_CHECK_API = "users-playlists/playlists-song-check"
    const val ZENE_USER_ADD_TO_PLAYLISTS_API = "users-playlists/add-to-playlists"
    const val ZENE_USER_IS_LIKED_API = "users-playlists/is-liked"
    const val ZENE_USER_ADD_LIKE_API = "users-playlists/add-like"
    const val ZENE_USER_SAVE_PLAYLISTS_API = "users-playlists/save-playlists"
    const val ZENE_USER_SAVED_PLAYLISTS_API = "users-playlists/saved-playlists"
    const val ZENE_USER_MY_PLAYLISTS_API = "users-playlists/my-playlists"
    const val ZENE_USER_MY_ALL_PLAYLISTS_API = "users-playlists/my-all-playlists"
    const val ZENE_USER_DELETE_MY_PLAYLIST_API = "users-playlists/delete-my-playlist"
    const val ZENE_USER_MY_PLAYLIST_INFO_API = "users-playlists/my-playlist-info"
    const val ZENE_USER_UPDATE_PLAYLIST_NAME_API = "users-playlists/update-playlist-name"
    const val ZENE_USER_UPDATE_PLAYLIST_IMAGE_API = "users-playlists/update-playlist-image"
    const val ZENE_USER_MY_PLAYLISTS_SONGS_API = "users-playlists/my-playlists-songs"
    const val ZENE_USER_MY_PLAYLISTS_SONGS_REORDER_API = "users-playlists/my-playlists-songs-reorder"
    const val ZENE_USER_PLAYLIST_LIKE_COUNT_API = "users-playlists/like-count"
    const val ZENE_USER_PLAYLIST_IMPORT_LIKE_API = "users-playlists/import-songs-to-like"
    const val ZENE_USER_PLAYLIST_REMOVE_MEDIA_PLAYLISTS_API =
        "users-playlists/remove-media-playlists"


    const val IP_BASE_URL = "http://ip-api.com/"
    const val IP_JSON_API = "json"

    fun getSearchOnGoogle(q: String): String {
        return "https://www.google.com/search?q=${q.lowercase()}"
    }

    fun getSearchNewsOnGoogle(q: String): String {
        return "https://www.google.com/search?q=${q.lowercase()}&tbm=nws"
    }
}

object SaveParams {
    const val NEW_JOIN_USER_SOCKET = "NEW_JOIN_USER"
    const val OLD_JOIN_USER_SOCKET = "OLD_JOIN_USER"
    const val DELETE_MESSAGE_ON_SOCKET = "DELETE_MESSAGE_ON"
    const val USER_LEAVED_SOCKET = "USER_LEAVED"
    const val NEW_MESSAGE_ON_SOCKET = "NEW_MESSAGE_ON"
}