package com.rizwansayyed.zene.ui.phoneverifier

import android.util.Log
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit


object TrueCallerAPIUtils {

    private const val GET_TOKEN = "https://oauth-account-noneu.truecaller.com/v1/token"
    private const val USER_INFO = "https://oauth-account-noneu.truecaller.com/v1/userinfo"

    suspend fun getTCPhoneNumber(code: String, codeVerifier: String) = withContext(Dispatchers.IO) {
        val client = OkHttpClient.Builder().readTimeout(30, TimeUnit.MINUTES)
            .connectTimeout(30, TimeUnit.MINUTES).build()

        try {
            val token = getTCToken(code, codeVerifier)
            Log.d("TAG", "getTCPhoneNumber: $token == $code == $codeVerifier")
            val request = Request.Builder().url(USER_INFO)
                .addHeader("Authorization", "Bearer $token").build()

            val response = client.newCall(request).execute()
            val number = JSONObject(response.body?.string()!!).optString("phone_number")
            Log.d("TAG", "getTCPhoneNumber: $number")

        } catch (e: Exception) {
            e.message?.toast()
        }
    }


    private suspend fun getTCToken(code: String, codeVerifier: String): String =
        withContext(Dispatchers.IO) {
            val client = OkHttpClient.Builder().readTimeout(30, TimeUnit.MINUTES)
                .connectTimeout(30, TimeUnit.MINUTES).build()

            val mediaType = "application/x-www-form-urlencoded".toMediaTypeOrNull()
            val body =
                "grant_type=authorization_code&client_id=${BuildConfig.TRUE_CALLER_CLIENT_ID}&code=$code&code_verifier=$codeVerifier"
                    .toRequestBody(mediaType)

//            val body = MultipartBody.Builder().setType(MultipartBody.FORM)
//                .addFormDataPart("grant_type", "authorization_code")
//                .addFormDataPart("client_id", BuildConfig.TRUE_CALLER_CLIENT_ID)
//                .addFormDataPart("code", code).addFormDataPart("code_verifier", codeVerifier)
//                .build()

            val request = Request.Builder().url(GET_TOKEN).post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded").build()

            val response = client.newCall(request).execute()
            val json = JSONObject(response.body?.string()!!).optString("access_token")

            return@withContext json
        }
}