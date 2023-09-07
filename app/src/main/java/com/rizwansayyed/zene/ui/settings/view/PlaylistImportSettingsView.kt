package com.rizwansayyed.zene.ui.settings.view

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.settings.ViewLocalSongs
import com.rizwansayyed.zene.ui.settings.ViewLocalSongsImport
import com.rizwansayyed.zene.ui.theme.BlackLight
import com.rizwansayyed.zene.utils.QuickSandRegular
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import com.rizwansayyed.zene.utils.Utils.showToast
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.coroutines.flow.flowOf

const val redirectUri = "com.rizwansayyed.zene://callback"
const val requestCodeSpotify = 9968639

@Composable
fun PlaylistImportSettings() {
    val c = LocalContext.current as Activity

    QuickSandSemiBold(
        stringResource(id = R.string.import_playlist),
        size = 16,
        modifier = Modifier.padding(top = 35.dp, start = 15.dp)
    )

    Column(
        Modifier
            .padding(vertical = 15.dp, horizontal = 9.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BlackLight)
            .padding(5.dp)
    ) {
        ViewLocalSongsImport(stringResource(id = R.string.spotify)) {
            val builder = AuthorizationRequest
                .Builder(BuildConfig.S_KEY, AuthorizationResponse.Type.TOKEN, redirectUri).apply {
                    setScopes(arrayOf("playlist-read-private", "playlist-read-collaborative"))
                }.build()

            AuthorizationClient.openLoginActivity(c, requestCodeSpotify, builder)
        }
        ViewLocalSongsImport(stringResource(id = R.string.apple_music)) {
            "Apple Music import coming soon".showToast()
        }
        ViewLocalSongsImport(stringResource(id = R.string.youtube_music)) {
            "Youtube Music import coming soon".showToast()
        }
    }

}
