package com.rizwansayyed.zene.data

import com.rizwansayyed.zene.data.model.AIDataResponse
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.EntertainmentDataResponse
import com.rizwansayyed.zene.data.model.MoviesDataResponse
import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.data.model.PodcastDataResponse
import com.rizwansayyed.zene.data.model.RadioDataResponse
import com.rizwansayyed.zene.data.model.SearchDataResponse
import com.rizwansayyed.zene.data.model.SearchPlacesDataResponse
import com.rizwansayyed.zene.data.model.SearchTrendingResponse
import com.rizwansayyed.zene.data.model.StatusTypeResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.data.model.VibesCommentsResponse
import com.rizwansayyed.zene.data.model.VideoDataResponse
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_MUSIC_LIST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_ACCEPT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_ADD_A_COMMENT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_FRIENDS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_FRIENDS_REQUEST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_FRIENDS_VIBES_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_GET_COMMENT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEARCH_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEND_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEND_LOCATION_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEND_MESSAGE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SHARE_VIBE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_USERS_SEARCH_VIA_PHONE_NUMBER_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_USER_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_USER_SETTINGS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_ENTERTAINMENT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_ENTERTAINMENT_MOVIES_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_MUSIC_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_PODCAST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_RADIO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_VIDEOS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_ALL_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_GIF_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_KEYWORDS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_PLACES_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_TRENDING_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_TRENDING_GIF_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_SEND_NUMBER_OTP_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_CONNECT_STATUS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_TRUE_CALLER_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_VERIFY_NUMBER_OTP_API
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ZeneAPIService {

    @Headers("Content-Type: application/json")
    @POST(ZENE_USER_UPDATE_API)
    suspend fun updateUser(@Body data: RequestBody): UserInfoResponse

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
    @POST(ZENE_SEARCH_PLACES_API)
    suspend fun searchPlaces(
        @Header("token") token: String, @Body data: RequestBody
    ): List<SearchPlacesDataResponse>

    @Headers("Content-Type: application/json")
    @POST(ZENE_RECENT_HOME_ENTERTAINMENT_API)
    suspend fun entertainmentNews(
        @Header("token") token: String, @Body data: RequestBody
    ): EntertainmentDataResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_RECENT_HOME_ENTERTAINMENT_MOVIES_API)
    suspend fun entertainmentMovies(
        @Header("token") token: String, @Body data: RequestBody
    ): MoviesDataResponse

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
    @POST(ZENE_CONNECT_SEND_MESSAGE_API)
    suspend fun sendConnectMessage(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_SEND_LOCATION_API)
    suspend fun sendConnectLocation(
        @Header("token") token: String, @Body data: RequestBody
    ): StatusTypeResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_FRIENDS_VIBES_API)
    suspend fun connectFriendsVibes(
        @Header("token") token: String, @Body data: RequestBody
    ): List<ConnectFeedDataResponse>

    @POST(ZENE_CONNECT_SHARE_VIBE_API)
    suspend fun shareConnectVibe(
        @Header("token") token: String,
        @Body body: RequestBody?
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


}