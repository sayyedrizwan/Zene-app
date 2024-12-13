package com.rizwansayyed.zene.data.api.zene

import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.api.model.MoodLists
import com.rizwansayyed.zene.data.api.model.SongLikedResponse
import com.rizwansayyed.zene.data.api.model.StatusResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsData
import com.rizwansayyed.zene.data.api.model.ZeneArtistsDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsInfoResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsPostsResponse
import com.rizwansayyed.zene.data.api.model.ZeneBooleanResponse
import com.rizwansayyed.zene.data.api.model.ZeneCacheTopSongsArtistsPostsResponse
import com.rizwansayyed.zene.data.api.model.ZeneLyricsData
import com.rizwansayyed.zene.data.api.model.ZeneMoodPlaylistData
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicHistoryResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicImportPlaylistsDataResponse
import com.rizwansayyed.zene.data.api.model.ZenePlaylistAlbumsData
import com.rizwansayyed.zene.data.api.model.ZeneSavedPlaylistsResponse
import com.rizwansayyed.zene.data.api.model.ZeneSearchData
import com.rizwansayyed.zene.data.api.model.ZeneSponsorsResponse
import com.rizwansayyed.zene.data.api.model.ZeneUpdateAvailabilityResponse
import com.rizwansayyed.zene.data.api.model.ZeneUsersPremiumResponse
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.data.api.model.ZeneVideosMusicData
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_ADD_SONGS_PLAYLISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_ARTISTS_DATA_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_ARTISTS_INFO_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_COUNTRIES_BY_RADIO_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_COUNTRIES_RADIO_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_EXTRA_APP_UPDATE_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_EXTRA_IS_COUPON_AVAILABLE_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_EXTRA_SPONSORS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_FEEDS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_IMPORT_PLAYLISTS_SPOTIFY_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_IS_USER_PREMIUM_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_LANGUAGES_BY_RADIO_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_LANGUAGES_RADIO_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_MOODS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_NEW_RELEASE_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYER_LYRICS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYER_MERCHANDISE_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYER_SUGGESTED_SONGS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYER_VIDEO_DATA_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYLISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_RADIOS_Y_M_L_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_RADIO_INFO_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_RELATED_VIDEO_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_REMOVE_PLAYLISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_SEARCH_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_SEARCH_IMG_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_SEARCH_SUGGESTIONS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_SIMILAR_SONGS_TO_PLAY_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_SONG_INFO_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_SUGGESTED_SONGS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_ALBUMS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_ARTISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_GLOBAL_ARTISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_LISTEN_SONGS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_PLAYLISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_RADIO_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_SONGS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_VIDEOS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_IS_SONG_IN_PLAYLISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_LIKED_SONGS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_MY_PLAYLISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_NUMBER_USER_INFO_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_NUMBER_VERIFICATION_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_NUMBER_VERIFICATION_UPDATE_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_PLAYLISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_SONG_HISTORY_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_TOP_CACHE_SONGS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_UPDATE_ARTISTS_API
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ZeneAPIService {

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_USER_API)
    suspend fun updateUser(@Body body: RequestBody): StatusResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_USER_API)
    suspend fun getUser(@Query("user") user: String): ZeneUsersResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_IS_USER_PREMIUM_API)
    suspend fun updateUserSubscription(@Body body: RequestBody): ZeneBooleanResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_IS_USER_PREMIUM_API)
    suspend fun isUserSubscribe(@Query("email") name: String): ZeneUsersPremiumResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_EXTRA_SPONSORS_API)
    suspend fun sponsors(): ZeneSponsorsResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_EXTRA_APP_UPDATE_API)
    suspend fun updateAvailability(): ZeneUpdateAvailabilityResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_MOODS_API)
    suspend fun moodLists(): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_MOODS_API)
    suspend fun moodLists(@Body body: RequestBody): ZeneMoodPlaylistData

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_PLAYER_MERCHANDISE_API)
    suspend fun merchandise(@Query("n") name: String): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_NEW_RELEASE_API)
    suspend fun latestReleases(@Query("i") id: String): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_PLAYER_LYRICS_API)
    suspend fun lyrics(
        @Query("s") id: String, @Query("n") name: String, @Query("a") artists: String
    ): ZeneLyricsData

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_PLAYER_VIDEO_DATA_API)
    suspend fun playerVideoData(
        @Query("n") name: String, @Query("a") artists: String
    ): ZeneVideosMusicData

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_TOP_LISTEN_SONGS_API)
    suspend fun topMostListeningSong(): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_TOP_GLOBAL_ARTISTS_API)
    suspend fun topMostListeningArtists(): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_SEARCH_API)
    suspend fun searchData(@Query("s") id: String): ZeneSearchData

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_USER_UPDATE_ARTISTS_API)
    suspend fun updateArtists(@Body body: RequestBody): ZeneBooleanResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_SEARCH_SUGGESTIONS_API)
    suspend fun searchSuggestions(@Query("s") id: String): List<String>

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_TOP_ARTISTS_API)
    suspend fun favArtistsData(@Body body: RequestBody): ZeneArtistsData

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_SUGGESTED_SONGS_API)
    suspend fun suggestedSongs(@Body body: RequestBody): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_PLAYER_SUGGESTED_SONGS_API)
    suspend fun suggestedSongs(@Query("s") id: String): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_TOP_PLAYLISTS_API)
    suspend fun recommendedPlaylists(@Body body: RequestBody): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_TOP_ALBUMS_API)
    suspend fun recommendedAlbums(@Body body: RequestBody): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_TOP_VIDEOS_API)
    suspend fun recommendedVideo(@Body body: RequestBody): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_TOP_SONGS_API)
    suspend fun suggestTopSongs(@Body body: RequestBody): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_USER_SONG_HISTORY_API)
    suspend fun addSongHistory(@Body body: RequestBody): ZeneBooleanResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_PLAYLISTS_API)
    suspend fun playlistAlbums(
        @Query("id") id: String, @Query("email") email: String
    ): ZenePlaylistAlbumsData

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_ARTISTS_INFO_API)
    suspend fun artistsInfo(@Body body: RequestBody): ZeneArtistsInfoResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_ARTISTS_DATA_API)
    suspend fun artistsData(@Body body: RequestBody): ZeneArtistsDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_USER_SONG_HISTORY_API)
    suspend fun getSongHistory(
        @Query("email") email: String, @Query("page") page: Int
    ): ZeneMusicHistoryResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_SEARCH_IMG_API)
    suspend fun searchImg(@Query("s") search: String): List<String>

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_REMOVE_PLAYLISTS_API)
    suspend fun deletePlaylists(
        @Query("email") email: String,
        @Query("id") id: String
    ): ZeneBooleanResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @Multipart
    @POST(ZENE_USER_PLAYLISTS_API)
    suspend fun playlistCreate(
        @Part file: MultipartBody.Part?,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("id") id: RequestBody?
    ): ZeneBooleanResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_USER_PLAYLISTS_API)
    suspend fun savedPlaylists(
        @Query("email") email: String,
        @Query("page") page: Int
    ): ZeneSavedPlaylistsResponse


    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_USER_IS_SONG_IN_PLAYLISTS_API)
    suspend fun checkIfSongPresentInPlaylists(
        @Query("email") email: String,
        @Query("page") page: Int,
        @Query("songId") songId: String
    ): ZeneMusicDataResponse


    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_ADD_SONGS_PLAYLISTS_API)
    suspend fun addRemoveSongFromPlaylists(
        @Query("playlistId") playlistId: String,
        @Query("songId") songId: String,
        @Query("doAdd") doAdd: Boolean,
        @Query("email") email: String
    ): ZeneBooleanResponse


    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_FEEDS_API)
    suspend fun artistsPosts(@Body body: RequestBody): ZeneArtistsPostsResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_USER_TOP_CACHE_SONGS_API)
    suspend fun topCacheSongsAndArtists(@Body body: RequestBody): ZeneCacheTopSongsArtistsPostsResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_USER_MY_PLAYLISTS_API)
    suspend fun userPlaylistData(@Body body: RequestBody): ZeneMusicDataItems


    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_USER_MY_PLAYLISTS_API)
    suspend fun userPlaylistSongs(
        @Query("playlistID") playlistID: String, @Query("page") page: Int
    ): ZeneMusicDataResponse


    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_IMPORT_PLAYLISTS_SPOTIFY_API)
    suspend fun importSpotifyPlaylists(
        @Query("token") token: String, @Query("url") url: String?
    ): ZeneMusicImportPlaylistsDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_SONG_INFO_API)
    suspend fun songInfo(@Query("id") id: String): ZeneMusicDataItems

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_SIMILAR_SONGS_TO_PLAY_API)
    suspend fun similarSongsToPlay(@Query("id") id: String): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_RELATED_VIDEO_API)
    suspend fun relatedVideos(@Query("videoID") videoID: String): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_USER_LIKED_SONGS_API)
    suspend fun isSongLiked(@Body body: RequestBody): SongLikedResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_EXTRA_IS_COUPON_AVAILABLE_API)
    suspend fun isCouponAvailable(@Body body: RequestBody): ZeneBooleanResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_USER_NUMBER_VERIFICATION_API)
    suspend fun numberVerification(@Body body: RequestBody): ZeneBooleanResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_USER_NUMBER_VERIFICATION_UPDATE_API)
    suspend fun numberVerificationUpdate(@Body body: RequestBody): ZeneBooleanResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_USER_NUMBER_USER_INFO_API)
    suspend fun numberUserInfo(@Body body: RequestBody): List<ZeneUsersResponse>

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_RADIO_INFO_API)
    suspend fun radioInfo(@Query("id") id: String): ZeneMusicDataItems

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_TOP_RADIO_API)
    suspend fun topRadio(@Body body: RequestBody): List<MoodLists>

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_RADIOS_Y_M_L_API)
    suspend fun radiosYouMayLike(@Body body: RequestBody): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_LANGUAGES_RADIO_API)
    suspend fun radioLanguages(): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @GET(ZENE_COUNTRIES_RADIO_API)
    suspend fun radioCountries(): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_LANGUAGES_BY_RADIO_API)
    suspend fun radioViaLanguages(@Body body: RequestBody): ZeneMusicDataResponse

    @Headers("auth: ${BuildConfig.AUTH_HEADER}")
    @POST(ZENE_COUNTRIES_BY_RADIO_API)
    suspend fun radioViaCountries(@Body body: RequestBody): ZeneMusicDataResponse

}