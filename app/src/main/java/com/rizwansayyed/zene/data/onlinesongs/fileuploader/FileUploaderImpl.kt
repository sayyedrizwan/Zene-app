package com.rizwansayyed.zene.data.onlinesongs.fileuploader

import com.rizwansayyed.zene.data.utils.FileUploader0x0.FILE_UPLOADER_BASE_URL
import com.rizwansayyed.zene.utils.Utils.daysOldTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


class FileUploaderImpl @Inject constructor() : FileUploaderImplInterface {

    override suspend fun upload(file: File) = flow {
        val client = OkHttpClient().newBuilder()
            .build()

        val fileForm = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())

        val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, fileForm)
            .addFormDataPart("expires",  daysOldTimestamp(1826).toString())
            .build()

        val request = Request.Builder()
            .url(FILE_UPLOADER_BASE_URL).method("POST", body).build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful){
            throw Exception("")
            return@flow
        }

        emit(response.body?.string())
    }.flowOn(Dispatchers.IO)
}