@file:Suppress("DEPRECATION")

package com.rizwansayyed.zene.ui.main.search.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.ConsoleMessage
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.main.search.view.SongRecognitionType.ERROR
import com.rizwansayyed.zene.ui.main.search.view.SongRecognitionType.LISTENING
import com.rizwansayyed.zene.ui.main.search.view.SongRecognitionType.LOADING
import com.rizwansayyed.zene.ui.main.search.view.SongRecognitionType.NONE
import com.rizwansayyed.zene.ui.main.search.view.SongRecognitionType.NO_SONG
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.RippleLoadingAnimation
import com.rizwansayyed.zene.utils.MainUtils.runMusicDataInfos
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

const val SHAZAM_BASE_URL = "https://www.shazam.com/"

enum class SongRecognitionType {
    NONE, LOADING, LISTENING, ERROR, NO_SONG
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SearchSongRecognition(close: () -> Unit) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    var recType by remember { mutableStateOf(NONE) }
    var webView by remember { mutableStateOf<WebView?>(null) }
    var job by remember { mutableStateOf<Job?>(null) }
    var searchedSong by remember { mutableStateOf("") }

    val coroutine = rememberCoroutineScope()

    val webCClient = object : WebChromeClient() {
        override fun onPermissionRequest(request: PermissionRequest?) {
            request?.resources?.forEach {
                if (it == PermissionRequest.RESOURCE_AUDIO_CAPTURE) request.grant(request.resources)
            }
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
            if (consoleMessage?.message() == "button clicked!") {
                recType = LISTENING
            } else if (consoleMessage?.message() == "button click error!") {
                recType = ERROR
            } else if (consoleMessage?.message() == "cannot detect song!") {
                recType = NO_SONG
            }
            return true
        }
    }

    val webVClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?, request: WebResourceRequest?
        ): Boolean = false

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            recType = LOADING
            detectWhenNoSongFound(view)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            CoroutineScope(Dispatchers.IO).launch {
                delay(2.seconds)
                clickButtonToStart(view)
            }
            detectWhenIsSongFound(view)
            job?.cancel()
            job = coroutine.launch(Dispatchers.Main) {
                while (true) {
                    delay(2.seconds)
                    if (view?.url?.contains("https://www.shazam.com/song/") == true) {
                        searchedSong = view.title?.substringBefore(":") ?: ""
                    }
                }
            }
        }
    }

    Box(Modifier.fillMaxSize(), Alignment.Center) {
        if (searchedSong.length > 3) {
            when (val v = homeViewModel.searchASongData) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> CircularLoadingView()
                is ResponseResult.Success -> Column(
                    Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(),
                    Arrangement.Center, Alignment.CenterHorizontally
                ) {
                    ItemCardView(v.data)
                    Spacer(Modifier.height(10.dp))
                    ButtonWithBorder(R.string.play) {
                        close()
                        runMusicDataInfos(v.data)
                    }
                }
            }
            LaunchedEffect(Unit) {
                homeViewModel.searchASong(searchedSong)
                job?.cancel()
                webView?.let { killWebViewData(it) }
                webView = null
            }
        } else {
            AndroidView({ ctx ->
                WebView(ctx).apply {
                    settings.javaScriptEnabled = true
                    settings.javaScriptCanOpenWindowsAutomatically = true
                    settings.domStorageEnabled = true
                    webViewClient = webVClient
                    setInitialScale(1)
                    settings.useWideViewPort = true
                    settings.loadWithOverviewMode = true
                    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    settings.setSupportZoom(false)
                    settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    webChromeClient = webCClient
                    settings.mediaPlaybackRequiresUserGesture = false
                    settings.allowContentAccess = true
                    webView = this
                    clearWebViewData(this)
                    loadUrl(SHAZAM_BASE_URL)
                }
            }, Modifier.fillMaxSize())

            Spacer(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            )

            Box(
                Modifier
                    .clickable { }
                    .fillMaxSize()
                    .background(Color.Black), Alignment.Center
            ) {
                if (recType == LOADING || recType == LISTENING) {
                    RippleLoadingAnimation(MainColor)
                    ImageIcon(R.drawable.zene_logo, 18)
                }

                SearchSongListeningTextView(Modifier.align(Alignment.BottomCenter), recType, close)
            }
        }
    }


    LifecycleResumeEffect(Unit) {
        onPauseOrDispose {
            job?.cancel()
            webView?.let { killWebViewData(it) }
            webView = null
            close()
        }
    }
}