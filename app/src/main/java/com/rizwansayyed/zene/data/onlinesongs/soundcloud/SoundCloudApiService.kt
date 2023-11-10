package com.rizwansayyed.zene.data.onlinesongs.soundcloud

import com.rizwansayyed.zene.data.utils.SoundCloudAPI.SOUND_CLOUD_SEARCH
import com.rizwansayyed.zene.data.utils.YoutubeAPI
import com.rizwansayyed.zene.domain.soundcloud.SoundCloudProfileResponse
import com.rizwansayyed.zene.domain.soundcloud.SoundCloudSearchResponse
import com.rizwansayyed.zene.domain.yt.YoutubePageResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SoundCloudApiService {

    @GET(SOUND_CLOUD_SEARCH)
    suspend fun search(
        @Query("q") q: String,
        @Query("client_id") clientId: String,
        @Query("limit") limit: Int = 1,
    ): SoundCloudSearchResponse

    @GET("users/{username}/web-profiles")
    suspend fun profile(
        @Path("username", encoded = true) username: String?,
        @Query("client_id") clientId: String
    ): SoundCloudProfileResponse
}