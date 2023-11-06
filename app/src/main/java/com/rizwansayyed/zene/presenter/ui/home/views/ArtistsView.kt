package com.rizwansayyed.zene.presenter.ui.home.views

import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.home.artists.MainImageAndList
import com.rizwansayyed.zene.presenter.ui.home.artists.TopNameView
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.viewmodel.ArtistsViewModel
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


class TheInterface {
    @JavascriptInterface
    fun onSpanTextChanged(toast: String?) {
        Log.d("TAG", "onSpanTextChanged: the daata $toast")
    }
}

@Composable
fun YoutubePlayerWebView() {
    val height = (LocalConfiguration.current.screenHeightDp / 1.2)

    AndroidView(factory = { ctx ->
        WebView(ctx).apply {
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    val script = """
                        var target = document.querySelector('.time-first');
                        var observer = new MutationObserver(function(mutations) {
                            mutations.forEach(function(mutation) {
                                if (mutation.type === 'childList') {
                                    var newText = mutation.target.textContent;
                                    Android.onSpanTextChanged(newText);
                                }
                            });
                        });
                        observer.observe(target, { childList: true });
                    """

                    view.evaluateJavascript(script, null)

                }
            }
            addJavascriptInterface(TheInterface(), "Android")
            webChromeClient = WebChromeClient()
            settings.loadsImagesAutomatically = true
            settings.javaScriptEnabled = true
            settings.allowFileAccess = true
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.pluginState = WebSettings.PluginState.ON
            settings.mediaPlaybackRequiresUserGesture = false
            settings.domStorageEnabled = true
            settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            requestFocus(View.FOCUS_DOWN)

            loadUrl("https://m.youtube.com/watch?v=2Vv-BfVoq4g")

//            val data_html =
//                "<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /> .frame-container {\n" +
//                        "    position: relative;\n" +
//                        "    padding-bottom: 56.25%; /* 16:9 */  \n" +
//                        "    padding-top: 25px;\n" +
//                        "    width: 300%; /* enlarge beyond browser width */\n" +
//                        "    left: -100%; /* center */\n" +
//                        "}\n" +
//                        "\n" +
//                        ".frame-container iframe {\n" +
//                        "    position: absolute; \n" +
//                        "    top: 0; \n" +
//                        "    left: 0; \n" +
//                        "    width: 100%; \n" +
//                        "    height: 100%;\n" +
//                        "} </head> <body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\"> " +
//                        "<div class=\"frame-container\"><iframe width=\"560\" height=\"${height}\" src=\"https://www.youtube.com/embed/fRD_3vJagxk?si=vSv6jod2l9jju79g&amp;controls=0&amp;showinfo=0&amp;autoplay=1&amp;mute=1&amp;start=125\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe></div>" +
//                        " </body> </html> "
//
//            loadDataWithBaseURL("https://www.youtube.com", data_html, "text/html", "UTF-8", null)
        }
    })
}

@Composable
fun ArtistsView() {
    val artistsViewModel: ArtistsViewModel = hiltViewModel()
    val homeNav: HomeNavViewModel = hiltViewModel()

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID),
        Modifier
            .fillMaxSize()
            .background(DarkGreyColor),
    ) {
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column(Modifier.fillMaxWidth()) {
                YoutubePlayerWebView()
                TopNameView()
                MainImageAndList()
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Spacer(Modifier.height(130.dp))
        }
    }

    LaunchedEffect(Unit) {
        artistsViewModel.init(homeNav.selectedArtists)
    }
}