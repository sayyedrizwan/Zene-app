package com.rizwansayyed.zene.data.api

import com.rizwansayyed.zene.data.api.imgbb.ImgBBAPIInterface
import com.rizwansayyed.zene.data.api.imgbb.ImgBBAPIService
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


class ImgBBAPIImpl @Inject constructor(private val api: ImgBBAPIService) : ImgBBAPIInterface {

    override suspend fun uploadImage(file: File) = flow {
        val filePart = MultipartBody.Part.createFormData(
            "image", file.name, file.asRequestBody("image/*".toMediaTypeOrNull())
        )

        emit(api.upload(filePart))
    }
}