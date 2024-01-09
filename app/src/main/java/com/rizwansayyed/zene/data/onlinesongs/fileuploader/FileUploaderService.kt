package com.rizwansayyed.zene.data.onlinesongs.fileuploader


import com.rizwansayyed.zene.utils.Utils.daysOldTimestamp
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileUploaderService {

    @Multipart
    @POST
    suspend fun upload(
        @Part("file") file: RequestBody,
        @Part("expires") expires: RequestBody
    ): String

}
