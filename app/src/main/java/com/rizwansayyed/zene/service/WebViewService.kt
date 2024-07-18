package com.rizwansayyed.zene.service

import android.content.Context
import android.view.View
import android.webkit.WebView

class WebViewService(context: Context) : WebView(context) {

    override fun onWindowVisibilityChanged(visibility: Int) {
        if (visibility != View.GONE) super.onWindowVisibilityChanged(visibility)
    }
}