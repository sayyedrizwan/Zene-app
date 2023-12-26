package com.rizwansayyed.zene.data.onlinesongs.auddrecognition


import com.rizwansayyed.zene.data.utils.AuddRecognition.AUDD_BASE_URL
import com.rizwansayyed.zene.data.utils.SongDownloader.YT_CW_BASE_URL
import com.rizwansayyed.zene.data.utils.VideoDownloaderAPI.SAVE_FROM_VIDEO_API
import com.rizwansayyed.zene.domain.auddSongRecognition.AuddSongRecognitionResponse
import com.rizwansayyed.zene.domain.download.SaveFromVideoResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Url

interface AuddSongRecognitionService {

    @Multipart
    @POST
    suspend fun recognition(
        @Url url: String = AUDD_BASE_URL,
        @Part file: MultipartBody.Part,
        @Query("api_token") apiToken: String = "test2",
        @Query("return") r: String = "apple_music,spotify"
    ): AuddSongRecognitionResponse

}
