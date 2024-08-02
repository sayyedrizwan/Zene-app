package com.rizwansayyed.zene.ui.extra.mymusic.spotify

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.utils.Utils.URLS.APPLE_MUSIC_LOGIN
import com.rizwansayyed.zene.utils.Utils.URLS.YOUTUBE_URL
import com.rizwansayyed.zene.utils.Utils.enable
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse


@Composable
fun MyMusicImportSpotifyPlaylistView() {
    val context = LocalContext.current as Activity

    Column(
        Modifier
            .padding(vertical = 20.dp, horizontal = 10.dp)
            .width(210.dp)
            .height(260.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MainColor)
            .clickable {
                openSpotifyLogin(context)
            }, Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.ic_spotify), stringResource(R.string.import_from_spotify),
            Modifier.size(40.dp)
        )

        Spacer(Modifier.height(10.dp))

        TextPoppins(stringResource(R.string.import_from_spotify), true, size = 15)
    }
}

@Composable
fun MyMusicImportAppleMusicPlaylistView() {
    var loginDialog by remember { mutableStateOf(false) }

    Column(
        Modifier
            .padding(vertical = 20.dp, horizontal = 10.dp)
            .width(210.dp)
            .height(260.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MainColor)
            .clickable {
                loginDialog = true
            }, Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.ic_apple_music),
            stringResource(R.string.import_from_apple_music),
            Modifier.size(40.dp)
        )

        Spacer(Modifier.height(10.dp))

        TextPoppins(stringResource(R.string.import_from_apple_music), true, size = 15)
    }
}


fun openSpotifyLogin(activity: Activity) {
    val redirectURI = "com.rizwansayyed.zene.import://callback"
    val builder = AuthorizationRequest.Builder(
        BuildConfig.SPOTIFY_CLIENT_ID, AuthorizationResponse.Type.TOKEN, redirectURI
    )
    builder.setScopes(arrayOf("playlist-read-private", "playlist-read-collaborative"))

    AuthorizationClient.openLoginInBrowser(activity, builder.build())
}