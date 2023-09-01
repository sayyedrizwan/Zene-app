package com.rizwansayyed.zene.utils

import com.rizwansayyed.zene.utils.Utils.USER_AGENT
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


fun postOkHttps(url: String, json: String): String? {
    val client = OkHttpClient().newBuilder().build()
    val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
    val body: RequestBody = json.toRequestBody(mediaType)
    val request: Request = Request.Builder()
        .url(url).method("POST", body).addHeader("user-agent", USER_AGENT)
        .addHeader("Content-Type", "text/plain")
        .build()
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        return response.body?.string()
    }

    return null
}

fun downloadHTMLOkhttp(url: String): String? {
    val cookies = OkhttpCookies()
    val client = OkHttpClient().newBuilder().cookieJar(cookies).build()
    val request = Request.Builder().url(url)
        .addHeader("User-Agent", Utils.USER_AGENT).method("GET", null).build()
    val response = client.newCall(request).execute()

    if (response.isSuccessful) {
        return response.body?.string()
    }
    return null
}

class OkhttpCookies : CookieJar {
    private val cookieStore = mutableMapOf<String, List<Cookie>>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
    }
}