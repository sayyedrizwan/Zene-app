package com.rizwansayyed.zene.data.api.imgbb

import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.data.api.model.ImgBBResponse
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
import com.rizwansayyed.zene.data.api.model.ZeneSearchData
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.data.api.model.ZeneVideosMusicData
import com.rizwansayyed.zene.utils.Utils.URLS.IMG_BB_UPLOAD
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_ARTISTS_DATA_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_ARTISTS_INFO_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_MOODS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_NEW_RELEASE_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYER_LYRICS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYER_MERCHANDISE_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYER_SUGGESTED_SONGS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYER_VIDEO_DATA_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_PLAYLISTS_API
import com.rizwansayyed.zene.utils.Utils.URLS.ZENE_SEARCH_API
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
import retrofit2.http.Query

interface ImgBBAPIService {

    @Multipart
    @POST(IMG_BB_UPLOAD)
    suspend fun upload(
        @Part filePart: MultipartBody.Part,
        @Query("key") key: String = BuildConfig.IMG_BB_API
    ): ImgBBResponse

}