package com.rizwansayyed.zene.data

import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.EntertainmentDataResponse
import com.rizwansayyed.zene.data.model.MoviesDataResponse
import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.data.model.PodcastDataResponse
import com.rizwansayyed.zene.data.model.RadioDataResponse
import com.rizwansayyed.zene.data.model.StatusTypeResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.data.model.VideoDataResponse
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_ACCEPT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_NEAR_MUSIC_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEARCH_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEND_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEND_LOCATION_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_SEND_MESSAGE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_USERS_SEARCH_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_USER_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_CONNECT_USER_SETTINGS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_ENTERTAINMENT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_ENTERTAINMENT_MOVIES_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_MUSIC_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_PODCAST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_RADIO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_VIDEOS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_SEND_NUMBER_OTP_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_UPDATE_API
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
    @POST(ZENE_CONNECT_NEAR_MUSIC_API)
    suspend fun connectNearMusic(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_CONNECT_USERS_SEARCH_API)
    suspend fun connectUsersSearch(
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
}