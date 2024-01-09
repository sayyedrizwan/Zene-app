package com.rizwansayyed.zene.data.onlinesongs.fileuploader.implementation

import com.rizwansayyed.zene.data.onlinesongs.fileuploader.FileUploaderService
import com.rizwansayyed.zene.utils.Utils.daysOldTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


class FileUploaderImpl @Inject constructor(
    private val fileUploader: FileUploaderService
) : FileUploaderImplInterface {

    override suspend fun upload(file: File) = flow {
        val theFile = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        val expire = daysOldTimestamp(1826).toString()
            .toRequestBody("text/plain".toMediaTypeOrNull())

        emit(fileUploader.upload(theFile, expire))
    }.flowOn(Dispatchers.IO)
}