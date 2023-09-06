package com.rizwansayyed.zene.ui.settings.view

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.settings.ViewLocalSongs
import com.rizwansayyed.zene.ui.settings.ViewLocalSongsImport
import com.rizwansayyed.zene.ui.theme.BlackLight
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import com.rizwansayyed.zene.utils.Utils.showToast
import kotlinx.coroutines.flow.flowOf

@Composable
fun PlaylistImportSettings() {
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
            "Spotify import coming soon".showToast()
        }
        ViewLocalSongsImport(stringResource(id = R.string.apple_music)) {
            "Apple Music import coming soon".showToast()
        }
        ViewLocalSongsImport(stringResource(id = R.string.youtube_music)) {
            "Youtube Music import coming soon".showToast()
        }

    }

}