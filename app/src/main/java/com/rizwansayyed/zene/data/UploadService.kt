package com.rizwansayyed.zene.data

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


object UploadService {

    private const val ZERO_X_URL = "https://0x0.st"
    fun uploadFile(file: File): String? {
        try {
            val client = OkHttpClient().newBuilder().build()

            val fileStream = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
            val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", file.name, fileStream)
                .addFormDataPart("secret", generateRandomString())
                .addFormDataPart("expires", "72").build()

            val request = Request.Builder().url(ZERO_X_URL).method("POST", body).build()
            val response = client.newCall(request).execute()
            val string = response.body?.string()
            if (string?.contains("/") == true && string.contains("."))
                return string

            return null
        } catch (e: Exception) {
            return null
        }
    }

    private fun generateRandomString(length: Int = 21): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }
}