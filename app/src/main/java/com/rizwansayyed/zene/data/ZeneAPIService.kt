package com.rizwansayyed.zene.data

import com.rizwansayyed.zene.data.model.AIDataResponse
import com.rizwansayyed.zene.data.model.ArtistsResponse
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.CountResponse
import com.rizwansayyed.zene.data.model.EntertainmentDataResponse
import com.rizwansayyed.zene.data.model.MediaLikedResponse
import com.rizwansayyed.zene.data.model.MediaPathResponse
import com.rizwansayyed.zene.data.model.MoviesDataResponse
import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.data.model.MusicHistoryResponse
import com.rizwansayyed.zene.data.model.NewPlaylistResponse
import com.rizwansayyed.zene.data.model.PlayerLyricsInfoResponse
import com.rizwansayyed.zene.data.model.PlayerRadioResponse
import com.rizwansayyed.zene.data.model.PlayerVideoForSongsResponse
import com.rizwansayyed.zene.data.model.PodcastDataResponse
import com.rizwansayyed.zene.data.model.PodcastEposideResponse
import com.rizwansayyed.zene.data.model.PodcastPlaylistResponse
import com.rizwansayyed.zene.data.model.RadioDataResponse
import com.rizwansayyed.zene.data.model.SavedPlaylistsPodcastsResponse
import com.rizwansayyed.zene.data.model.SearchDataResponse
import com.rizwansayyed.zene.data.model.SearchPlacesDataResponse
import com.rizwansayyed.zene.data.model.SearchTrendingResponse
import com.rizwansayyed.zene.data.model.StatusTypeResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.data.model.UserPlaylistResponse
import com.rizwansayyed.zene.data.model.VibesCommentsResponse
import com.rizwansayyed.zene.data.model.VideoDataResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_MUSIC_LIST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_MUSIC_LYRICS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_MUSIC_MEDIA_URL_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_SIMILAR_MUSIC_API
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
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_INFO_ARTIST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_INFO_ARTIST_FOLLOW_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_INFO_PLAYLISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_PODCAST_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_RADIO_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_SIMILAR_PLAYLISTS_SONGS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_SIMILAR_RADIO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_SIMILAR_SONGS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_SIMILAR_VIDEOS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_SONGS_LYRICS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PLAYER_VIDEO_FOR_SONGS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PODCAST_PODCAST_INFO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PODCAST_PODCAST_MEDIA_URL_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RADIO_MEDIA_URL_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_ENTERTAINMENT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_ENTERTAINMENT_MOVIES_API
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
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_ADD_HISTORY_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_ADD_LIKE_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_ADD_TO_PLAYLISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_GET_HISTORY_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_IS_LIKED_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_LOGIN_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_MY_PLAYLISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_MY_PLAYLISTS_SONGS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_PLAYLISTS_CREATE_NEW_PLAYLISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_PLAYLISTS_SONG_CHECK_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_PLAYLIST_LIKE_COUNT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_SAVED_PLAYLISTS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_SAVE_PLAYLISTS_API
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
    ): PodcastEposideResponse

    @Headers("Content-Type: application/json")
    @POST(ZENE_PLAYER_RADIO_INFO_API)
    suspend fun playerRadioInfo(
        @Header("token") token: String, @Body data: RequestBody
    ): PlayerRadioResponse

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
    @POST(ZENE_USER_MY_PLAYLISTS_SONGS_API)
    suspend fun myPlaylistsSongs(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

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
    @POST(ZENE_SIMILAR_PODCASTS_API)
    suspend fun similarPodcasts(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

    @Headers("Content-Type: application/json")
    @POST(ZENE_PLAYER_SIMILAR_RADIO_API)
    suspend fun similarRadio(
        @Header("token") token: String, @Body data: RequestBody
    ): ZeneMusicDataList

}