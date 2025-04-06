package com.rizwansayyed.zene.ui.partycall.view

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.partycall.PartyViewModel
import com.rizwansayyed.zene.utils.MainUtils.getRawFolderString
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_URL
import com.rizwansayyed.zene.utils.WebViewUtils.clearWebViewData
import com.rizwansayyed.zene.utils.WebViewUtils.enable
import com.rizwansayyed.zene.utils.WebViewUtils.killWebViewData
import com.rizwansayyed.zene.viewmodel.generateRoomId


@Composable
fun CallingWebView(email: String, myEmail: String, myName: String, viewModel: PartyViewModel) {
    Log.d("TAG", "CallingWebView: ${generateRoomId(email, myEmail)}")
    val htmlContent = remember {
        getRawFolderString(R.raw.video_call_mirotalk)
            .replace("<<<RoomID>>>", generateRoomId(email, myEmail))
            .replace("<<<Name>>>", clearNameUnique(myName, viewModel.name))
    }

    class WebAppInterface {
        @JavascriptInterface
        fun callPicked() {
            viewModel.hideCallingView()
            stopRingtoneFromEarpiece()
        }

        @JavascriptInterface
        fun isVideoEnabled(isEnabled: Boolean) {
            viewModel.setVideoOnOrOff(isEnabled)
        }

        @JavascriptInterface
        fun isMicEnabled(isEnabled: Boolean) {
            viewModel.setMicOnOrOff(isEnabled)
        }
    }

    AndroidView(
        factory = { ctx ->
            WebView(ctx).apply {
                enable()
                viewModel.setCallWebView(this)

                webChromeClient = object : WebChromeClient() {
                    override fun onPermissionRequest(request: PermissionRequest) {
                        request.grant(request.resources)
                    }

                    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                        super.onConsoleMessage(consoleMessage)
                        Log.d("TAG", "onConsoleMessage: data ${consoleMessage?.message()}")
                        return false
                    }
                }
                addJavascriptInterface(WebAppInterface(), "Zene")
                settings.cacheMode = WebSettings.LOAD_DEFAULT

                settings.mediaPlaybackRequiresUserGesture = false
                settings.domStorageEnabled = true
                settings.allowFileAccess = true
                settings.javaScriptCanOpenWindowsAutomatically = true

                loadDataWithBaseURL("https://mirotalk-zene.onrender.com", htmlContent, "text/html", "UTF-8", null)
            }
        }, Modifier
            .fillMaxSize()
            .background(Color.Black)
    )

    DisposableEffect(Unit) {
        onDispose {
            viewModel.callWebViewMain?.let {
                clearWebViewData(it)
                killWebViewData(it)
            }

            viewModel.setCallWebView(null)
        }
    }

//    LaunchedEffect(viewModel.isInPictureInPicture) {
//        if (viewModel.isInPictureInPicture)
//            viewModel.callWebViewMain?.evaluateJavascript("hideLocalVideo();") {}
//        else
//            viewModel.callWebViewMain?.evaluateJavascript("showLocalVideo();") {}
//
//    }
}

fun clearNameUnique(myName: String, name: String): String {
    if (myName != name) return myName

    return "$myName${(111..999999).random()}"
}