package com.rizwansayyed.zene.utils

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebStorage
import android.webkit.WebView

object WebViewUtils {

    @SuppressLint("SetJavaScriptEnabled")
    fun WebView.enable() {
        settings.javaScriptEnabled = true
//        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.domStorageEnabled = true
//        setInitialScale(1)
//        settings.useWideViewPort = true
//        settings.loadWithOverviewMode = true
//        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        settings.setSupportZoom(false)
        settings.mediaPlaybackRequiresUserGesture = false
//        settings.allowContentAccess = true
    }

    fun killWebViewData(webView: WebView) {
        webView.clearHistory();
        webView.clearCache(true)
        webView.loadUrl("about:blank")
        webView.onPause()
        webView.removeAllViews()
        webView.pauseTimers()
        webView.destroy()
    }

    fun clearWebViewData(webView: WebView) {
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()
        WebStorage.getInstance().deleteAllData()
        webView.clearCache(true)
        webView.clearFormData()
        webView.clearHistory()
    }
}