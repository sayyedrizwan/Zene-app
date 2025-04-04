package com.rizwansayyed.zene.ui.partycall.view

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
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
import java.security.MessageDigest


@Composable
fun CallingWebView(email: String, myEmail: String, viewModel: PartyViewModel) {
    val htmlContent = remember {
        getRawFolderString(R.raw.video_call_peerjs)
            .replace("<<<OtherEmail>>>", generatePeerId(email, viewModel.randomCode))
            .replace("<<<MyEmail>>>", generatePeerId(myEmail, viewModel.randomCode))
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
                loadDataWithBaseURL(ZENE_URL, htmlContent, "text/html", "UTF-8", null)
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

    LaunchedEffect(viewModel.isInPictureInPicture) {
        if (viewModel.isInPictureInPicture)
            viewModel.callWebViewMain?.evaluateJavascript("hideLocalVideo();") {}
        else
            viewModel.callWebViewMain?.evaluateJavascript("showLocalVideo();") {}

    }
}

fun generatePeerId(email: String, appSalt__: String): String {
    val appSalt = "12239jn"
    val input = "$email@$appSalt"
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(input.toByteArray())
    return hashBytes.joinToString("") { "%02x".format(it) }.take(16)
}