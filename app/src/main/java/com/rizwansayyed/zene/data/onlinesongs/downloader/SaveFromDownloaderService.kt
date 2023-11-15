package com.rizwansayyed.zene.data.onlinesongs.downloader


import com.rizwansayyed.zene.data.utils.SongDownloader.YT_CW_BASE_URL
import com.rizwansayyed.zene.data.utils.VideoDownloaderAPI.SAVE_FROM_VIDEO_API
import com.rizwansayyed.zene.domain.download.SaveFromVideoResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface SaveFromDownloaderService {

    @FormUrlEncoded
    @POST(SAVE_FROM_VIDEO_API)
    suspend fun download(
        @Field("url") url: String,
        @Field("token") token: String
    ): SaveFromVideoResponse

}
