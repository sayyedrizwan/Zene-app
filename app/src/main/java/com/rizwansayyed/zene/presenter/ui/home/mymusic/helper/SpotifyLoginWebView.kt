package com.rizwansayyed.zene.presenter.ui.home.mymusic.helper

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient


@SuppressLint("ViewConstructor", "SetJavaScriptEnabled")
class SpotifyLoginWebView(private val context: Context) : WebView(context) {

    private val spotifyLoginBaseURL =
        "https://accounts.spotify.com/login?continue=https%3A%2F%2Fopen.spotify.com%2F"

    init {
        clearCache(true)
        val webView = this
        webView.getSettings().setLoadsImagesAutomatically(true)
        webView.getSettings().setJavaScriptEnabled(true)
        webView.getSettings().setAllowContentAccess(true)
        webView.getSettings().setDomStorageEnabled(true)

        webView.getSettings().setUseWideViewPort(true)
        webView.getSettings().setLoadWithOverviewMode(true)
        webView.getSettings().setDomStorageEnabled(true)
        webView.clearView()
        webView.setHorizontalScrollBarEnabled(false)
//        webView.getSettings().setAppCacheEnabled(true)
        webView.getSettings().setDatabaseEnabled(true)
        webView.setVerticalScrollBarEnabled(false)
        webView.getSettings().setBuiltInZoomControls(true)
        webView.getSettings().setSupportZoom(true)
        webView.getSettings().setDisplayZoomControls(false)
        webView.getSettings().setAllowFileAccess(true)
        webView.getSettings().setPluginState(WebSettings.PluginState.OFF)
        webView.setScrollbarFadingEnabled(false)
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE)
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR)
        webView.setWebViewClient(WebViewClient())
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH)
        webView.setInitialScale(1)
        webView.scrollBarStyle = WebView.SCROLLBARS_OUTSIDE_OVERLAY;

        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                view.loadUrl(request.url.toString())
                return false
            }
        })
        getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
        setLayerType(View.LAYER_TYPE_HARDWARE, null);


        webView.loadUrl(spotifyLoginBaseURL)

//        webChromeClient =WebChromeClient()
//        webViewClient =object : WebViewClient(){
//            override fun shouldOverrideUrlLoading(
//                view: WebView?,
//                request: WebResourceRequest?
//            ): Boolean {
//                view?.loadUrl(request?.url.toString())
//                return true
//            }
//        }
//        setInitialScale(1)
//        getSettings().setJavaScriptEnabled(true);
//        getSettings().setLoadWithOverviewMode(true)
//        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN)
//        getSettings().setUseWideViewPort(true);
//        setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
//        setScrollbarFadingEnabled(false);
//        loadUrl(spotifyLoginBaseURL);
    }

}