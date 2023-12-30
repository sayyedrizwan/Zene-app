package com.rizwansayyed.zene.presenter.ui.home.mymusic.helper

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.CookieManager
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.appleMusicToken
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.spotifyToken
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.ytMusicToken
import com.rizwansayyed.zene.domain.WebMusicLoginTokens
import kotlinx.coroutines.flow.flowOf


@SuppressLint("ViewConstructor", "SetJavaScriptEnabled")
class MusicWebsitesLoginWebView(private val received: () -> Unit) {

    private val spotifyBaseURL = "spotify.com"
    private val googleBaseURL = "google.com"
    private val ytMusicBaseURL = "music.youtube.com"
    private val appleMusicBaseURL = "music.apple.com"

    private val spotifyLoginBaseURL =
        "https://accounts.spotify.com/login?continue=https%3A%2F%2Fopen.spotify.com%2F"
    private val appleMusicLoginBaseURL = "https://music.apple.com/login"
    private val googleLoginBaseURL =
        "https://accounts.google.com/v3/signin/identifier?continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Faction_handle_signin%3Dtrue%26app%3Ddesktop%26hl%3Den%26next%3Dhttps%253A%252F%252Fmusic.youtube.com%252F%26feature%3D__FEATURE__&hl=en&ltmpl=music&passive=true&service=youtube&uilel=3&flowName=GlifWebSignIn&theme=glif"

    private var web: WebView? = null

    fun init(webView: WebView) = apply {
        web = webView
        webView.clearCache(true)
        CookieManager.getInstance().removeAllCookies(null)

        webView.webViewClient = WebClient()
        webView.webChromeClient = WebChrome()

        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptEnabled = true
    }

    fun spotify() {
        web?.loadUrl(spotifyLoginBaseURL)
    }

    fun ytMusic() {
        web?.loadUrl(googleLoginBaseURL)
    }

    fun appleMusicMusic() {
        web?.loadUrl(appleMusicLoginBaseURL)
    }

    inner class WebChrome : WebChromeClient() {
        override fun onPermissionRequest(request: PermissionRequest?) {
            request?.grant(request.resources)
        }
    }

    inner class WebClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            CookieManager.getInstance().flush()
        }

        override fun shouldOverrideUrlLoading(v: WebView?, r: WebResourceRequest?): Boolean {
            if (r?.url.toString().contains(spotifyBaseURL) ||
                r?.url.toString().contains(googleBaseURL) ||
                r?.url.toString().contains(ytMusicBaseURL)
            ) {
                v?.loadUrl(r?.url.toString())
                return true
            }
            return false
        }

        override fun shouldInterceptRequest(
            v: WebView?, r: WebResourceRequest?
        ): WebResourceResponse? {
            val header = r?.requestHeaders.toString()
            if (r?.url.toString().contains(spotifyBaseURL) &&
                header.lowercase().contains("authorization=bearer")
            ) {
                val token = header.substringAfter("authorization=").substringBefore(", ")
                spotifyToken = flowOf(token)
                received()
            }
            if (r?.url.toString().contains(ytMusicBaseURL) &&
                header.lowercase().contains("authorization=")
            ) {
                val auth = header.substringAfter("Authorization=").substringBefore(", ")
                val cookies = getCookie("https://music.youtube.com")
                ytMusicToken = flowOf(WebMusicLoginTokens(auth, cookies ?: ""))
                received()
            }
            if (r?.url.toString().contains(appleMusicBaseURL) &&
                header.lowercase().contains("authorization=") &&
                (getCookie("https://music.apple.com")?.count { it == ';' } ?: 0) > 5
            ) {
                val auth = header.substringAfter("Authorization=").substringBefore(", ")
                val cookies = getCookie("https://music.apple.com")
                appleMusicToken = flowOf(WebMusicLoginTokens(auth, cookies ?: ""))
                received()
            }

            return super.shouldInterceptRequest(v, r)
        }
    }


    fun getCookie(siteName: String): String? {
        val cookieManager = CookieManager.getInstance()
        return cookieManager.getCookie(siteName)
    }
}