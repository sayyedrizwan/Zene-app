package com.rizwansayyed.zene.utils

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.webkit.WebView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object WebViewUtils {

    @SuppressLint("SetJavaScriptEnabled")
    fun WebView.enable() = CoroutineScope(Dispatchers.Main).safeLaunch(Dispatchers.Main) {
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.setSupportZoom(false)
        settings.mediaPlaybackRequiresUserGesture = false
    }

    fun killWebViewData(webView: WebView) = CoroutineScope(Dispatchers.Main).safeLaunch(Dispatchers.Main) {
        webView.clearHistory()
        webView.clearCache(true)
        webView.loadUrl("about:blank")
        webView.onPause()
        webView.destroy()
    }

    fun clearWebViewData(webView: WebView) = CoroutineScope(Dispatchers.Main).safeLaunch(Dispatchers.Main) {
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()
        WebStorage.getInstance().deleteAllData()
        webView.clearCache(true)
        webView.clearFormData()
        webView.clearHistory()
    }
}