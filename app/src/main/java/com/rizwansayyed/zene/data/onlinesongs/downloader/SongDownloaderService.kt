package com.rizwansayyed.zene.data.onlinesongs.downloader


import com.rizwansayyed.zene.data.utils.InstagramAPI.INSTAGRAM_PROFILE_API
import com.rizwansayyed.zene.data.utils.SongDownloader.SONG_DOWNLOAD_URL
import com.rizwansayyed.zene.domain.SongDownloadResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface SongDownloaderService {

    @GET("$SONG_DOWNLOAD_URL{sId}")
    suspend fun download(
        @Path(value = "sId", encoded = true) songId: String
    ): SongDownloadResponse

}
