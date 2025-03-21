package com.rizwansayyed.zene.utils

object URLSUtils {

    const val ZENE_MAIL = "knocknock@zenemusic.co"
    const val ZENE_FAQ_URL = "https://www.zenemusic.co/faq"
    const val ZENE_PRIVACY_POLICY_URL = "https://www.zenemusic.co/privacy-policy"
    const val ZENE_HOME = "https://www.zenemusic.co/home"


    const val ZENE_URL = "https://www.zenemusic.co"
    const val ZENE_URL_CONNECT = "/connect"
    const val ZENE_URL_SEARCH = "/search"
    const val ZENE_URL_ENTERTAINMENT = "/entertainment"
    const val ZENE_URL_SETTINGS = "/settings"
    const val ZENE_URL_PODCASTS = "/podcasts"
    const val ZENE_URL_MY_LIBRARY = "/my-library"

    fun connectShareURL(username: String) = "$ZENE_URL${ZENE_URL_CONNECT}/$username"

    const val SHAZAM_BASE_URL = "https://www.shazam.com/"

    enum class SongRecognitionType {
        NONE, LOADING, LISTENING, ERROR, NO_SONG
    }


    const val YT_VIDEO_BASE_URL = "https://www.youtube.com/"
    const val LIKED_SONGS_ON_ZENE = "_liked_songs_on_zene"
    const val X_VIDEO_BASE_URL = "https://www.x.com/"


    const val FB_GRAPH_ID = "https://graph.facebook.com/me"


    const val ZENE_BASE_URL_API = "http://192.168.0.100:4100"
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
    const val ZENE_SEARCH_IMG_API = "search/search-img"


    const val ZENE_CONNECT_USERS_SEARCH_VIA_PHONE_NUMBER_API =
        "connect/users-search-via-phonenumber"
    const val ZENE_CONNECT_SEARCH_API = "connect/search"
    const val ZENE_CONNECT_USER_INFO_API = "connect/user-info"
    const val ZENE_CONNECT_FRIENDS_API = "connect/friends"
    const val ZENE_CONNECT_FRIENDS_REQUEST_API = "connect/friends-request"
    const val ZENE_CONNECT_SEND_API = "connect/send"
    const val ZENE_CONNECT_ACCEPT_API = "connect/accept"
    const val ZENE_CONNECT_USER_SETTINGS_API = "connect/user-settings"
    const val ZENE_CONNECT_SEND_MESSAGE_API = "connect/send-message"
    const val ZENE_CONNECT_CHAT_RECENT_MESSAGE_API = "connect/chat-recent-message"
    const val ZENE_CONNECT_MARK_MESSAGE_AS_READ_API = "connect/mark-message-as-read"
    const val ZENE_CONNECT_SEND_LOCATION_API = "connect/send-location"
    const val ZENE_CONNECT_FRIENDS_VIBES_API = "connect/friends-vibes"
    const val ZENE_CONNECT_SHARE_VIBE_API = "connect/share-vibe"
    const val ZENE_CONNECT_ADD_A_COMMENT_API = "connect/add-a-comment"
    const val ZENE_CONNECT_GET_COMMENT_API = "connect/get-comment"


    const val ZENE_PLAYER_PODCAST_INFO_API = "player/podcast-info"
    const val ZENE_PLAYER_RADIO_INFO_API = "player/radio-info"
    const val ZENE_PLAYER_SIMILAR_VIDEOS_API = "player/similar-videos"
    const val ZENE_PLAYER_SIMILAR_ARTISTS_ALBUM_SONG_API = "player/similar-artists-album-of-song"
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


    const val ZENE_AI_MUSIC_LIST_API = "ai/list"
    const val ZENE_AI_MUSIC_MEDIA_URL_API = "ai/ai-music-media-url"
    const val ZENE_AI_SIMILAR_MUSIC_API = "ai/similar-ai-music"
    const val ZENE_AI_MUSIC_LYRICS_API = "ai/ai-music-lyrics"

    const val ZENE_USER_LOGIN_API = "users/user-login"
    const val ZENE_USER_UPDATE_API = "users/user-update"
    const val ZENE_USER_UPDATE_CONNECT_STATUS_API = "users/update-connect-status"
    const val ZENE_USER_UPDATE_TRUE_CALLER_API = "users/update-truecaller"
    const val ZENE_USER_SEND_NUMBER_OTP_API = "users/send-number-otp"
    const val ZENE_USER_CHECK_NUMBER_VERIFIED_THIS_WEEK_API =
        "users/check-number-verified-this-week"
    const val ZENE_USER_VERIFY_NUMBER_OTP_API = "users/verify-number-otp"
    const val ZENE_USER_ADD_HISTORY_API = "users/add-history"
    const val ZENE_USER_CHECK_USERNAME_API = "users/check-username"
    const val ZENE_USER_UPDATE_USERNAME_API = "users/update-username"
    const val ZENE_USER_UPDATE_NAME_API = "users/update-name"
    const val ZENE_USER_UPDATE_PROFILE_PHOTO_API = "users/update-profile-photo"
    const val ZENE_USER_GET_HISTORY_API = "users/get-history"

    const val ZENE_USER_PLAYLISTS_CREATE_NEW_PLAYLISTS_API = "users-playlists/create-new-playlists"
    const val ZENE_USER_PLAYLISTS_SONG_CHECK_API = "users-playlists/playlists-song-check"
    const val ZENE_USER_ADD_TO_PLAYLISTS_API = "users-playlists/add-to-playlists"
    const val ZENE_USER_IS_LIKED_API = "users-playlists/is-liked"
    const val ZENE_USER_ADD_LIKE_API = "users-playlists/add-like"
    const val ZENE_USER_SAVE_PLAYLISTS_API = "users-playlists/save-playlists"
    const val ZENE_USER_SAVED_PLAYLISTS_API = "users-playlists/saved-playlists"
    const val ZENE_USER_MY_PLAYLISTS_API = "users-playlists/my-playlists"
    const val ZENE_USER_DELETE_MY_PLAYLIST_API = "users-playlists/delete-my-playlist"
    const val ZENE_USER_MY_PLAYLIST_INFO_API = "users-playlists/my-playlist-info"
    const val ZENE_USER_UPDATE_PLAYLIST_NAME_API = "users-playlists/update-playlist-name"
    const val ZENE_USER_UPDATE_PLAYLIST_IMAGE_API = "users-playlists/update-playlist-image"
    const val ZENE_USER_MY_PLAYLISTS_SONGS_API = "users-playlists/my-playlists-songs"
    const val ZENE_USER_PLAYLIST_LIKE_COUNT_API = "users-playlists/like-count"
    const val ZENE_USER_PLAYLIST_REMOVE_MEDIA_PLAYLISTS_API =
        "users-playlists/remove-media-playlists"

    const val ZENE_CONTACT_CACHE = "zene_contact_cache"

    const val IP_BASE_URL = "http://ip-api.com/"
    const val IP_JSON_API = "json"
}