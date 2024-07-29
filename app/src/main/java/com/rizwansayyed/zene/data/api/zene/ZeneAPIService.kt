package com.rizwansayyed.zene.data.api.zene

import com.rizwansayyed.zene.data.api.model.StatusResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsData
import com.rizwansayyed.zene.data.api.model.ZeneArtistsDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsInfoResponse
import com.rizwansayyed.zene.data.api.model.ZeneBooleanResponse
import com.rizwansayyed.zene.data.api.model.ZeneLyricsData
import com.rizwansayyed.zene.data.api.model.ZeneMoodPlaylistData
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicHistoryResponse
import com.rizwansayyed.zene.data.api.model.ZenePlaylistAlbumsData
import com.rizwansayyed.zene.data.api.model.ZeneSavedPlaylistsResponse
import com.rizwansayyed.zene.data.api.model.ZeneSearchData
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.data.api.model.ZeneVideosMusicData
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_ARTISTS_DATA_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_ARTISTS_INFO_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_MOODS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_NEW_RELEASE_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYER_LYRICS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYER_MERCHANDISE_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYER_SUGGESTED_SONGS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYER_VIDEO_DATA_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYLISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_REMOVE_PLAYLISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_SEARCH_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_SEARCH_IMG_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_SEARCH_SUGGESTIONS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_SUGGESTED_SONGS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_ALBUMS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_ARTISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_GLOBAL_ARTISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_LISTEN_SONGS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_PLAYLISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_SONGS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_VIDEOS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_PLAYLISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_SONG_HISTORY_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_UPDATE_ARTISTS_API
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Query

interface ZeneAPIService {

    @POST(ZENE_USER_API)
    suspend fun updateUser(@Body body: RequestBody): StatusResponse

    @GET(ZENE_USER_API)
    suspend fun getUser(@Query("user") user: String): ZeneUsersResponse

    @GET(ZENE_MOODS_API)
    suspend fun moodLists(): ZeneMusicDataResponse

    @POST(ZENE_MOODS_API)
    suspend fun moodLists(@Body body: RequestBody): ZeneMoodPlaylistData

    @GET(ZENE_PLAYER_MERCHANDISE_API)
    suspend fun merchandise(@Query("n") name: String): ZeneMusicDataResponse

    @GET(ZENE_NEW_RELEASE_API)
    suspend fun latestReleases(@Query("i") id: String): ZeneMusicDataResponse

    @GET(ZENE_PLAYER_LYRICS_API)
    suspend fun lyrics(
        @Query("s") id: String, @Query("n") name: String, @Query("a") artists: String
    ): ZeneLyricsData

    @GET(ZENE_PLAYER_VIDEO_DATA_API)
    suspend fun playerVideoData(
        @Query("n") name: String, @Query("a") artists: String
    ): ZeneVideosMusicData

    @GET(ZENE_TOP_LISTEN_SONGS_API)
    suspend fun topMostListeningSong(): ZeneMusicDataResponse

    @GET(ZENE_TOP_GLOBAL_ARTISTS_API)
    suspend fun topMostListeningArtists(): ZeneMusicDataResponse

    @GET(ZENE_SEARCH_API)
    suspend fun searchData(@Query("s") id: String): ZeneSearchData

    @POST(ZENE_USER_UPDATE_ARTISTS_API)
    suspend fun updateArtists(@Body body: RequestBody): ZeneBooleanResponse

    @GET(ZENE_SEARCH_SUGGESTIONS_API)
    suspend fun searchSuggestions(@Query("s") id: String): List<String>

    @POST(ZENE_TOP_ARTISTS_API)
    suspend fun favArtistsData(@Body body: RequestBody): ZeneArtistsData


    @POST(ZENE_SUGGESTED_SONGS_API)
    suspend fun suggestedSongs(@Body body: RequestBody): ZeneMusicDataResponse

    @GET(ZENE_PLAYER_SUGGESTED_SONGS_API)
    suspend fun suggestedSongs(@Query("s") id: String): ZeneMusicDataResponse

    @POST(ZENE_TOP_PLAYLISTS_API)
    suspend fun recommendedPlaylists(@Body body: RequestBody): ZeneMusicDataResponse

    @POST(ZENE_TOP_ALBUMS_API)
    suspend fun recommendedAlbums(@Body body: RequestBody): ZeneMusicDataResponse

    @POST(ZENE_TOP_VIDEOS_API)
    suspend fun recommendedVideo(@Body body: RequestBody): ZeneMusicDataResponse

    @POST(ZENE_TOP_SONGS_API)
    suspend fun suggestTopSongs(@Body body: RequestBody): ZeneMusicDataResponse

    @POST(ZENE_USER_SONG_HISTORY_API)
    suspend fun addSongHistory(@Body body: RequestBody): ZeneBooleanResponse

    @GET(ZENE_PLAYLISTS_API)
    suspend fun playlistAlbums(
        @Query("id") id: String, @Query("email") email: String
    ): ZenePlaylistAlbumsData

    @POST(ZENE_ARTISTS_INFO_API)
    suspend fun artistsInfo(@Body body: RequestBody): ZeneArtistsInfoResponse

    @POST(ZENE_ARTISTS_DATA_API)
    suspend fun artistsData(@Body body: RequestBody): ZeneArtistsDataResponse

    @GET(ZENE_USER_SONG_HISTORY_API)
    suspend fun getSongHistory(
        @Query("email") email: String, @Query("page") page: Int
    ): ZeneMusicHistoryResponse

    @GET(ZENE_SEARCH_IMG_API)
    suspend fun searchImg(@Query("s") search: String): List<String>

    @GET(ZENE_REMOVE_PLAYLISTS_API)
    suspend fun deletePlaylists(
        @Query("email") email: String,
        @Query("id") id: String
    ): ZeneBooleanResponse

    @Multipart
    @POST(ZENE_USER_PLAYLISTS_API)
    suspend fun playlistCreate(
        @Part file: MultipartBody.Part?,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("id") id: RequestBody?
    ): ZeneBooleanResponse

    @GET(ZENE_USER_PLAYLISTS_API)
    suspend fun savedPlaylists(
        @Query("email") email: String,
        @Query("page") page: Int
    ): ZeneSavedPlaylistsResponse
}