package com.rizwansayyed.zene.presenter.util

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.webkit.WebView

class WebViewService: Service() {

    private var mWebView: WebView? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        mWebView = PlayerMusicWebView(applicationContext)
    }
}