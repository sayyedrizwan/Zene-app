package com.rizwansayyed.zene.data.onlinesongs.downloader


import com.rizwansayyed.zene.data.utils.SongDownloader.YT_CW_BASE_URL
import com.rizwansayyed.zene.domain.download.DownloadYTCWResponse
import com.rizwansayyed.zene.domain.download.DownloadYTConvertor
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface SongDownloaderService {

    @GET
    suspend fun download(
        @Query("url") watchUrl: String,
        @Query("mp3_task") task: Int = 2,
        @Url url: String = YT_CW_BASE_URL
    ): DownloadYTCWResponse

    @GET
    suspend fun converter(@Url url: String): DownloadYTConvertor

}
