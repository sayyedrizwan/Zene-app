package com.rizwansayyed.zene.data.onlinesongs.downloader


import com.rizwansayyed.zene.data.utils.SongDownloader.APPLICATION_X_FORM_URL_ENCODE
import com.rizwansayyed.zene.data.utils.SongDownloader.KEEP_VID_BASE_URL
import com.rizwansayyed.zene.data.utils.SongDownloader.YT_CW_BASE_URL
import com.rizwansayyed.zene.domain.download.ConvertorKeepVidConvertorResponse
import com.rizwansayyed.zene.domain.download.DownloadKeepVidConvertorResponse
import com.rizwansayyed.zene.domain.download.DownloadYTCWResponse
import com.rizwansayyed.zene.domain.download.DownloadYTConvertor
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

interface SongDownloaderService {

    @GET
    suspend fun download(
        @Url url: String = YT_CW_BASE_URL,
        @Query("url") watchUrl: String,
        @Query("mp3_task") task: Int = 2,
    ): DownloadYTCWResponse

    @GET
    suspend fun converter(@Url url: String): DownloadYTConvertor

    @POST
    suspend fun convertorKeepVid(
        @Header("Content-Type") content: String = APPLICATION_X_FORM_URL_ENCODE,
        @Header("Referer") referer: String,
        @Url url: String = KEEP_VID_BASE_URL,
        @Body body: RequestBody
    ): ConvertorKeepVidConvertorResponse

    @GET
    suspend fun downloaderKeepVid(
        @Header("Referer") referer: String,
        @Header("X-Requested-With") x: String = "XMLHttpRequest",
        @Url url: String = KEEP_VID_BASE_URL,
        @Query("jobid") jobId: String,
        @Query("time") time: String,
    ): DownloadKeepVidConvertorResponse

}
