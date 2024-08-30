package com.rizwansayyed.zene.ui.mymusic.view.spotify

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextPoppins
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
    AuthorizationClient.clearCookies(activity)
    val redirectURI = "com.rizwansayyed.zene.import://callback"
    val builder = AuthorizationRequest.Builder(
        BuildConfig.SPOTIFY_CLIENT_ID, AuthorizationResponse.Type.TOKEN, redirectURI
    )
    builder.setScopes(arrayOf("playlist-read-private", "playlist-read-collaborative", "user-library-read"))

    AuthorizationClient.openLoginInBrowser(activity, builder.build())
}