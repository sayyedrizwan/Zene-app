package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.rizwansayyed.zene.presenter.theme.MainColor


const val TESTHTL = """
    <html><body>
    <object width="480" height="330"><param name="movie" 
value="http://www.youtube.com/v/"here is your video id"?version=3&amp;hl=pt_BR&amp;rel=0">   
</param><param name="allowFullScreen" value="true"></param>
<param name="allowscriptaccess" value="always"></param>
<embed src="http://www.youtube.com/v/"here is your video id"?version=3&amp;hl=pt_BR&amp;rel=0" 
type="application/x-shockwave-flash" width="480" height="330" allowscriptaccess="always" allowfullscreen="true" /></object>

    <iframe width="260" height="215" src="https://www.youtube.com/embed/UNo0TG9LwwI" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>
    
    </body></html>
"""

@Composable
fun MusicYoutubeWebView(modifier: Modifier) {
    var webView: WebView? = null

    val iFramePlayerOptions = IFramePlayerOptions.Builder()
        .controls(1)
        .rel(0)
        .ivLoadPolicy(1)
        .fullscreen(1)
        .ccLoadPolicy(1)
        .build()

    Box(modifier) {
        AndroidView(factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                setWebChromeClient(WebChromeClient())
                setFocusable(true);
                setFocusableInTouchMode(true);
                getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                getSettings().setDomStorageEnabled(true);
                getSettings().setDatabaseEnabled(true);
                setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
                settings.javaScriptCanOpenWindowsAutomatically = true
                settings.setSupportMultipleWindows(true)
                settings.setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/17E148")
                settings.builtInZoomControls = true
                settings.javaScriptEnabled = true
                getSettings().setAllowFileAccess(true);
                settings.databaseEnabled = true
                settings.domStorageEnabled = true
                settings.setGeolocationEnabled(true)
                setBackgroundColor(MainColor.value.toInt())
                loadDataWithBaseURL(null, TESTHTL, "text/html", "UTF-8", null);
//                loadUrl("https://www.youtube.com/watch?v=y5XoL2aTgVY")
            }
        }, update = {
            it. loadDataWithBaseURL(null, TESTHTL, "text/html", "UTF-8", null);
        }, modifier = Modifier.fillMaxSize())

//        val ctx = LocalContext.current
//        AndroidView(factory = {
//
//            var view = YouTubePlayerView(it)
//
//
//            val fragment = view.addYouTubePlayerListener(
//                object : AbstractYouTubePlayerListener() {
//                    override fun onReady(youTubePlayer: YouTubePlayer) {
//                        super.onReady(youTubePlayer)
//                        youTubePlayer.loadVideo("UNo0TG9LwwI", 0f)
//                    }
//                }
//            )
//            view
//        }, modifier = Modifier.fillMaxSize())
    }
}