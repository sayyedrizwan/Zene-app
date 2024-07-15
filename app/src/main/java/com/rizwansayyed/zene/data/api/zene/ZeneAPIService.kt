package com.rizwansayyed.zene.data.api.zene

import com.rizwansayyed.zene.data.api.model.StatusResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_MOODS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_NEW_RELEASE_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_ALBUMS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_GLOBAL_ARTISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_LISTEN_SONGS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_PLAYLISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_SONGS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_TOP_VIDEOS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_USER_API
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ZeneAPIService {

    @POST(ZENE_USER_API)
    suspend fun updateUser(@Body body: RequestBody): StatusResponse

    @GET(ZENE_USER_API)
    suspend fun getUser(@Query("user") user: String): ZeneUsersResponse

    @GET(ZENE_MOODS_API)
    suspend fun moodLists(): ZeneMusicDataResponse

    @GET(ZENE_NEW_RELEASE_API)
    suspend fun latestReleases(@Query("i") id: String): ZeneMusicDataResponse

    @GET(ZENE_TOP_LISTEN_SONGS_API)
    suspend fun topMostListeningSong(): ZeneMusicDataResponse

    @GET(ZENE_TOP_GLOBAL_ARTISTS_API)
    suspend fun topMostListeningArtists(): ZeneMusicDataResponse

    @POST(ZENE_TOP_PLAYLISTS_API)
    suspend fun recommendedPlaylists(@Body body: RequestBody): ZeneMusicDataResponse

    @POST(ZENE_TOP_ALBUMS_API)
    suspend fun recommendedAlbums(@Body body: RequestBody): ZeneMusicDataResponse

    @POST(ZENE_TOP_VIDEOS_API)
    suspend fun recommendedVideo(@Body body: RequestBody): ZeneMusicDataResponse

    @POST(ZENE_TOP_SONGS_API)
    suspend fun suggestTopSongs(@Body body: RequestBody): ZeneMusicDataResponse
}