package com.rizwansayyed.zene.ui.main.search.view

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun SearchSongRecognition() {
    AndroidView({ ctx ->
        WebView(ctx)
    })
}