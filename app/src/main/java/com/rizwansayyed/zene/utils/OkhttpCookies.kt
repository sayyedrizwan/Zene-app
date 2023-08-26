package com.rizwansayyed.zene.utils

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class OkhttpCookies : CookieJar {
    private val cookieStore = mutableMapOf<String, List<Cookie>>()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
    }
}