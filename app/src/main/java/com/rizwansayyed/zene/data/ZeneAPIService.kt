package com.rizwansayyed.zene.data

import com.rizwansayyed.zene.data.model.AIDataResponse
import com.rizwansayyed.zene.data.model.ArtistsResponse
import com.rizwansayyed.zene.data.model.ConnectChatMessageResponse
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.CountResponse
import com.rizwansayyed.zene.data.model.DeleteAccountInfoResponse
import com.rizwansayyed.zene.data.model.EntertainmentDataResponse
import com.rizwansayyed.zene.data.model.EntertainmentDiscoverResponse
import com.rizwansayyed.zene.data.model.MediaLikedResponse
import com.rizwansayyed.zene.data.model.MediaPathResponse
import com.rizwansayyed.zene.data.model.MediaStatusTypeResponse
import com.rizwansayyed.zene.data.model.MoviesDataResponse
import com.rizwansayyed.zene.data.model.MoviesTvShowResponse
import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.data.model.MusicHistoryResponse
import com.rizwansayyed.zene.data.model.NewPlaylistResponse
import com.rizwansayyed.zene.data.model.PlayerLyricsInfoResponse
import com.rizwansayyed.zene.data.model.PlayerRadioResponse
import com.rizwansayyed.zene.data.model.PlayerVideoForSongsResponse
import com.rizwansayyed.zene.data.model.PodcastDataResponse
import com.rizwansayyed.zene.data.model.PodcastEpisodeResponse
import com.rizwansayyed.zene.data.model.PodcastPlaylistResponse
import com.rizwansayyed.zene.data.model.RadioDataResponse
import com.rizwansayyed.zene.data.model.RecommendationNotificationResponse
import com.rizwansayyed.zene.data.model.SavedPlaylistsPodcastsResponse
import com.rizwansayyed.zene.data.model.SearchDataResponse
import com.rizwansayyed.zene.data.model.SearchPlacesDataResponse
import com.rizwansayyed.zene.data.model.SearchTrendingResponse
import com.rizwansayyed.zene.data.model.SponsorAdsResponse
import com.rizwansayyed.zene.data.model.StatusCouponResponse
import com.rizwansayyed.zene.data.model.StatusTypeResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.data.model.UserPlaylistResponse
import com.rizwansayyed.zene.data.model.VibesCommentsResponse
import com.rizwansayyed.zene.data.model.VideoDataResponse
import com.rizwansayyed.zene.data.model.WhoDatedWhoData
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_MUSIC_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_MUSIC_LIST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_MUSIC_LYRICS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_MUSIC_MEDIA_URL_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_SIMILAR_MUSIC_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_ACCEPT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_ADD_A_COMMENT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_CHAT_RECENT_MESSAGE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_CREATE_PLAYLIST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_DECLINE_PARTY_CALL_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_DELETE_MESSAGE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_FRIENDS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_FRIENDS_REQUEST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_FRIENDS_VIBES_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_GET_COMMENT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_MARK_MESSAGE_AS_READ_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_PLAYLIST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEARCH_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEND_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEND_FILE_MESSAGE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEND_JAM_MESSAGE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEND_LOCATION_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEND_MEDIA_MESSAGE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEND_MESSAGE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEND_PARTY_CALL_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SHARE_VIBE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_USERS_SEARCH_VIA_PHONE_NUMBER_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_USER_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_USER_SETTINGS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_ALL_TRAILERS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_BUZZ_NEWS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_DATING_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_DISCOVER_TRENDING_NEWS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_ENT_LIFESTYLE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_FEED_ARTISTS_UPDATES_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_FEED_FOLLOWED_ARTISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_INFO_ARTIST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_INFO_ARTIST_FOLLOW_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_INFO_MOVIE_SHOW_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_INFO_PLAYLISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_INFO_SEASON_MOVIE_SHOW_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_NOTIFICATION_RECOMMENDATION_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_IS_PLAYLIST_ADDED_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_PODCAST_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_RADIO_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_SIMILAR_ARTISTS_ALBUM_SONG_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_SIMILAR_PLAYLISTS_SONGS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_SIMILAR_RADIO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_SIMILAR_SONGS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_SIMILAR_VIDEOS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_SONGS_LYRICS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_SONG_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_VIDEO_FOR_SONGS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PODCAST_PODCAST_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PODCAST_PODCAST_MEDIA_URL_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RADIO_COUNTRY_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RADIO_MEDIA_URL_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RADIO_PODCASTS_CATEGORY_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_MUSIC_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_PODCAST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_RADIO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_VIDEOS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_ALL_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_A_SONG_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_GIF_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_IMG_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_KEYWORDS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_PLACES_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_TRENDING_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_TRENDING_GIF_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SIMILAR_PODCASTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SPONSOR_ADS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_ADD_HISTORY_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_ADD_LIKE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_ADD_TO_PLAYLISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_CANCEL_DELETE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_CHECK_NUMBER_VERIFIED_THIS_WEEK_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_CHECK_USERNAME_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_DELETE_ACCOUNT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_DELETE_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_DELETE_MY_PLAYLIST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_GET_HISTORY_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_IS_LIKED_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_IS_USER_PREMIUM_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_LOGIN_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_MY_ALL_PLAYLISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_MY_PLAYLISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_MY_PLAYLISTS_SONGS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_MY_PLAYLISTS_SONGS_REORDER_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_MY_PLAYLIST_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_PLAYLISTS_CREATE_NEW_PLAYLISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_PLAYLISTS_SONG_CHECK_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_PLAYLIST_IMPORT_LIKE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_PLAYLIST_LIKE_COUNT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_PLAYLIST_REMOVE_MEDIA_PLAYLISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_SAVED_PLAYLISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_SAVE_PLAYLISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_SEND_NUMBER_OTP_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_CONNECT_STATUS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_COUPON_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_EMAIL_SUB_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_NAME_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_PLAYLIST_IMAGE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_PLAYLIST_NAME_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_PROFILE_PHOTO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_SUBSCRIPTION_PLAY_STORE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_TRUE_CALLER_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_USERNAME_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_VERIFY_NUMBER_OTP_API
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ZeneAPIService {

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_LOGIN_API)
    suspend fun loginUser(@Body data: RequestBody): UserInfoResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_UPDATE_API)
    suspend fun updateUser(
        @Header("token") token: String, @Body data: RequestBody
    ): UserInfoResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_ADD_HISTORY_API)
    suspend fun addHistory(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_UPDATE_SUBSCRIPTION_PLAY_STORE_API)
    suspend fun updateSubscription(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_IS_USER_PREMIUM_API)
    suspend fun isUserPremium(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_CHECK_USERNAME_API)
    suspend fun checkUsername(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_UPDATE_USERNAME_API)
    suspend fun updateUsername(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_UPDATE_NAME_API)
    suspend fun updateName(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @POST(ZENE_USER_UPDATE_PROFILE_PHOTO_API)
    suspend fun updateProfilePhoto(
        @Header("token") token: String, @Body data: RequestBody?
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_GET_HISTORY_API)
    suspend fun getHistory(
        @Header("token") token: String, @Body data: RequestBody
    ): List<MusicHistoryResponse>

    @Headers("Content-Type: application/json")
    @POST(ZENE_RECENT_HOME_MUSIC_API)
    suspend fun recentHome(
        @Header("token") token: String, @Body data: RequestBody
    ): MusicDataResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_AI_MUSIC_LIST_API)
    suspend fun trendingAIMusic(
        @Header("token") token: String, @Body data: RequestBody
    ): AIDataResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_SEARCH_TRENDING_API)
    suspend fun trendingData(
        @Header("token") token: String, @Body data: RequestBody
    ): SearchTrendingResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_SEARCH_KEYWORDS_API)
    suspend fun searchKeywords(
        @Header("token") token: String, @Body data: RequestBody
    ): List<String>

    @Headers("Content-Type: application/json")
    @POST(ZENE_SEARCH_IMG_API)
    suspend fun searchImages(
        @Header("token") token: String, @Body data: RequestBody
    ): List<String>

    @Headers("Content-Type: application/json")
    @POST(ZENE_RECENT_HOME_PODCAST_API)
    suspend fun homePodcast(
        @Header("token") token: String, @Body data: RequestBody
    ): PodcastDataResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_RECENT_HOME_RADIO_API)
    suspend fun homeRadio(
        @Header("token") token: String, @Body data: RequestBody
    ): RadioDataResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_RECENT_HOME_VIDEOS_API)
    suspend fun homeVideos(
        @Header("token") token: String, @Body data: RequestBody
    ): VideoDataResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_SEARCH_ALL_API)
    suspend fun search(
        @Header("token") token: String, @Body data: RequestBody
    ): SearchDataResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_SEARCH_A_SONG_API)
    suspend fun searchASong(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicData

    @Headers("Content-Type: application/json")
    @POST(ZENE_SEARCH_PLACES_API)
    suspend fun searchPlaces(
        @Header("token") token: String, @Body data: RequestBody
    ): List<SearchPlacesDataResponse>

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_UPDATE_TRUE_CALLER_API)
    suspend fun updateTrueCallerPhone(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_SEND_NUMBER_OTP_API)
    suspend fun sendVerifyPhoneNumber(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_VERIFY_NUMBER_OTP_API)
    suspend fun verifyPhoneNumber(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_CHECK_NUMBER_VERIFIED_THIS_WEEK_API)
    suspend fun checkNumberVerified(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_CREATE_PLAYLIST_API)
    suspend fun connectCreatePlaylists(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_USERS_SEARCH_VIA_PHONE_NUMBER_API)
    suspend fun connectUsersSearchViaPhoneNumber(
        @Header("token") token: String, @Body data: RequestBody
    ): List<ConnectUserResponse>

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_SEARCH_API)
    suspend fun connectSearch(
        @Header("token") token: String, @Body data: RequestBody
    ): List<ConnectUserResponse>

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_USER_INFO_API)
    suspend fun connectUserInfo(
        @Header("token") token: String, @Body data: RequestBody
    ): ConnectUserInfoResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_FRIENDS_API)
    suspend fun connectFriendsList(
        @Header("token") token: String, @Body data: RequestBody
    ): List<ConnectUserInfoResponse>

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_FRIENDS_REQUEST_API)
    suspend fun connectFriendsRequestList(
        @Header("token") token: String, @Body data: RequestBody
    ): List<ConnectUserResponse>

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_SEND_API)
    suspend fun connectSendRequest(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_ACCEPT_API)
    suspend fun connectAcceptRequest(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_USER_SETTINGS_API)
    suspend fun updateConnectSettings(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_CHAT_RECENT_MESSAGE_API)
    suspend fun getChatConnectRecentMessage(
        @Header("token") token: String, @Body data: RequestBody
    ): List<ConnectChatMessageResponse>

    @POST(ZENE_CONNECT_SEND_FILE_MESSAGE_API)
    suspend fun sendConnectFileMessage(
        @Header("token") token: String, @Body data: RequestBody
    ): MediaStatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_SEND_MESSAGE_API)
    suspend fun sendConnectMessage(
        @Header("token") token: String, @Body data: RequestBody
    ): MediaStatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_DELETE_MESSAGE_API)
    suspend fun deleteConnectMessage(
        @Header("token") token: String, @Body data: RequestBody
    ): MediaStatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_SEND_JAM_MESSAGE_API)
    suspend fun sendConnectJamMessage(
        @Header("token") token: String, @Body data: RequestBody
    ): MediaStatusTypeResponse

    @POST(ZENE_CONNECT_SEND_MEDIA_MESSAGE_API)
    suspend fun sendConnectMediaMessage(
        @Header("token") token: String, @Body data: RequestBody
    ): MediaStatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_MARK_MESSAGE_AS_READ_API)
    suspend fun markMessageAsRead(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_SEND_LOCATION_API)
    suspend fun sendConnectLocation(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_SEND_PARTY_CALL_API)
    suspend fun sendPartyCall(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_DECLINE_PARTY_CALL_API)
    suspend fun declinePartyCall(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_FRIENDS_VIBES_API)
    suspend fun connectFriendsVibes(
        @Header("token") token: String, @Body data: RequestBody
    ): List<ConnectFeedDataResponse>

    @POST(ZENE_CONNECT_SHARE_VIBE_API)
    suspend fun shareConnectVibe(
        @Header("token") token: String, @Body body: RequestBody?
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_SEARCH_TRENDING_GIF_API)
    suspend fun trendingGif(
        @Header("token") token: String, @Body data: RequestBody
    ): List<String>

    @Headers("Content-Type: application/json")
    @POST(ZENE_SEARCH_GIF_API)
    suspend fun searchGif(
        @Header("token") token: String, @Body data: RequestBody
    ): List<String>

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_UPDATE_CONNECT_STATUS_API)
    suspend fun updateConnectStatus(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_ADD_A_COMMENT_API)
    suspend fun postCommentOnVibes(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_GET_COMMENT_API)
    suspend fun getCommentOfVibes(
        @Header("token") token: String, @Body data: RequestBody
    ): List<VibesCommentsResponse>

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_PLAYLIST_API)
    suspend fun getConnectPlaylists(
        @Header("token") token: String, @Body data: RequestBody
    ): SavedPlaylistsPodcastsResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_PLAYER_SIMILAR_ARTISTS_ALBUM_SONG_API)
    suspend fun similarArtistsAlbumOfSong(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicData

    @Headers("Content-Type: application/json")
    @POST(ZENE_PLAYER_SONG_INFO_API)
    suspend fun songInfo(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicData

    @Headers("Content-Type: application/json")
    @POST(ZENE_PLAYER_IS_PLAYLIST_ADDED_API)
    suspend fun isPlaylistAdded(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_PLAYER_SIMILAR_VIDEOS_API)
    suspend fun similarVideos(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_PLAYER_SIMILAR_SONGS_API)
    suspend fun similarSongs(
        @Header("token") token: String, @Body data: RequestBody
    ): SearchDataResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_PLAYER_PODCAST_INFO_API)
    suspend fun playerPodcastInfo(
        @Header("token") token: String, @Body data: RequestBody
    ): PodcastEpisodeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_PLAYER_RADIO_INFO_API)
    suspend fun playerRadioInfo(
        @Header("token") token: String, @Body data: RequestBody
    ): PlayerRadioResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_RADIO_COUNTRY_API)
    suspend fun radioByCountry(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_PLAYLISTS_CREATE_NEW_PLAYLISTS_API)
    suspend fun createNewPlaylists(
        @Header("token") token: String, @Body data: RequestBody
    ): NewPlaylistResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_PLAYLISTS_SONG_CHECK_API)
    suspend fun playlistSongCheck(
        @Header("token") token: String, @Body data: RequestBody
    ): List<UserPlaylistResponse>

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_ADD_TO_PLAYLISTS_API)
    suspend fun addItemToPlaylists(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_ADD_LIKE_API)
    suspend fun likeItems(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_IS_LIKED_API)
    suspend fun likedStatus(
        @Header("token") token: String, @Body data: RequestBody
    ): MediaLikedResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_PLAYER_SIMILAR_PLAYLISTS_SONGS_API)
    suspend fun similarPlaylistsSongs(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_PLAYER_SONGS_LYRICS_API)
    suspend fun playerLyrics(
        @Header("token") token: String, @Body data: RequestBody
    ): PlayerLyricsInfoResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_PLAYER_VIDEO_FOR_SONGS_API)
    suspend fun playerVideoForSongs(
        @Header("token") token: String, @Body data: RequestBody
    ): PlayerVideoForSongsResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_PODCAST_PODCAST_INFO_API)
    suspend fun podcastInfo(
        @Header("token") token: String, @Body data: RequestBody
    ): PodcastPlaylistResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_RADIO_PODCASTS_CATEGORY_API)
    suspend fun podcastCategories(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_INFO_PLAYLISTS_API)
    suspend fun playlistsInfo(
        @Header("token") token: String, @Body data: RequestBody
    ): PodcastPlaylistResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_SAVE_PLAYLISTS_API)
    suspend fun savePlaylists(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_SAVED_PLAYLISTS_API)
    suspend fun getSavePlaylists(
        @Header("token") token: String, @Body data: RequestBody
    ): SavedPlaylistsPodcastsResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_MY_PLAYLISTS_API)
    suspend fun myPlaylists(
        @Header("token") token: String, @Body data: RequestBody
    ): SavedPlaylistsPodcastsResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_MY_ALL_PLAYLISTS_API)
    suspend fun myAllPlaylists(
        @Header("token") token: String, @Body data: RequestBody
    ): SavedPlaylistsPodcastsResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_DELETE_MY_PLAYLIST_API)
    suspend fun deleteMyPlaylists(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_MY_PLAYLIST_INFO_API)
    suspend fun myPlaylistInfo(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicData

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_UPDATE_PLAYLIST_NAME_API)
    suspend fun nameUserPlaylist(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @POST(ZENE_USER_UPDATE_PLAYLIST_IMAGE_API)
    suspend fun updateUserPlaylistImage(
        @Header("token") token: String, @Body data: RequestBody?
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_MY_PLAYLISTS_SONGS_API)
    suspend fun myPlaylistsSongs(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_MY_PLAYLISTS_SONGS_REORDER_API)
    suspend fun myPlaylistsSongsReorder(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_PLAYLIST_REMOVE_MEDIA_PLAYLISTS_API)
    suspend fun removeMyPlaylistsSongs(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_PLAYLIST_IMPORT_LIKE_API)
    suspend fun importSongsToLike(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_PLAYLIST_LIKE_COUNT_API)
    suspend fun likeSongsCount(
        @Header("token") token: String, @Body data: RequestBody
    ): CountResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_INFO_ARTIST_API)
    suspend fun artistsInfo(
        @Header("token") token: String, @Body data: RequestBody
    ): ArtistsResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_INFO_ARTIST_FOLLOW_API)
    suspend fun followArtists(
        @Header("token") token: String, @Body data: RequestBody
    ): Boolean

    @Headers("Content-Type: application/json")
    @POST(ZENE_PODCAST_PODCAST_MEDIA_URL_API)
    suspend fun podcastMediaURL(
        @Header("token") token: String, @Body data: RequestBody
    ): MediaPathResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_RADIO_MEDIA_URL_API)
    suspend fun radioMediaURL(
        @Header("token") token: String, @Body data: RequestBody
    ): MediaPathResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_AI_MUSIC_MEDIA_URL_API)
    suspend fun aiMusicMediaURL(
        @Header("token") token: String, @Body data: RequestBody
    ): MediaPathResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_AI_SIMILAR_MUSIC_API)
    suspend fun similarAISongs(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_AI_MUSIC_LYRICS_API)
    suspend fun lyricsAIMusic(
        @Header("token") token: String, @Body data: RequestBody
    ): PlayerLyricsInfoResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_AI_MUSIC_INFO_API)
    suspend fun aiMusicInfo(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicData

    @Headers("Content-Type: application/json")
    @POST(ZENE_SIMILAR_PODCASTS_API)
    suspend fun similarPodcasts(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_PLAYER_SIMILAR_RADIO_API)
    suspend fun similarRadio(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_INFO_MOVIE_SHOW_INFO_API)
    suspend fun moviesTvShowsInfo(
        @Header("token") token: String, @Body data: RequestBody
    ): MoviesTvShowResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_INFO_SEASON_MOVIE_SHOW_INFO_API)
    suspend fun seasonMoviesTvShowsInfo(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_UPDATE_COUPON_API)
    suspend fun updateCoupon(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusCouponResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_NOTIFICATION_RECOMMENDATION_API)
    suspend fun notificationRecommendation(
        @Body data: RequestBody
    ): RecommendationNotificationResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_SPONSOR_ADS_API)
    suspend fun sponsorAds(): SponsorAdsResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_FEED_FOLLOWED_ARTISTS_API)
    suspend fun feedFollowedArtists(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_FEED_ARTISTS_UPDATES_API)
    suspend fun feedUpdatesArtists(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_DELETE_ACCOUNT_API)
    suspend fun deleteAccount(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_DELETE_INFO_API)
    suspend fun deleteAccountInfo(
        @Header("token") token: String, @Body data: RequestBody
    ): DeleteAccountInfoResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_CANCEL_DELETE_API)
    suspend fun cancelDeleteAccount(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_UPDATE_EMAIL_SUB_API)
    suspend fun updateEmailSubscription(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_ENT_DISCOVER_TRENDING_NEWS_API)
    suspend fun entDiscoverNews(
        @Header("token") token: String, @Body data: RequestBody
    ): EntertainmentDiscoverResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_ENT_BUZZ_NEWS_API)
    suspend fun entBuzzNews(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_ENT_LIFESTYLE_API)
    suspend fun entDiscoverLifeStyle(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_ENT_DATING_API)
    suspend fun entDating(
        @Header("token") token: String, @Body data: RequestBody
    ): List<WhoDatedWhoData>?


    @Headers("Content-Type: application/json")
    @POST(ZENE_ENT_ALL_TRAILERS_API)
    suspend fun entAllTrailers(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList
}