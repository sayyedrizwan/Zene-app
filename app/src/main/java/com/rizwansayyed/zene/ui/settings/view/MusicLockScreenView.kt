package com.rizwansayyed.zene.ui.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.datastore.MusicSpeedEnum
import com.rizwansayyed.zene.ui.settings.ViewLocalSongs
import com.rizwansayyed.zene.ui.theme.BlackLight
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

@Composable
fun MusicLockScreen() {
    val musicOnLockscreen by dataStoreManager.musicOnLockscreen.collectAsState(runBlocking { dataStoreManager.musicOnLockscreen.first() })

    QuickSandSemiBold(
        stringResource(id = R.string.show_playing_on_lock_screen),
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
        ViewLocalSongs(stringResource(id = R.string.enable), musicOnLockscreen) {
            dataStoreManager.musicOnLockscreen = flowOf(true)
        }

        ViewLocalSongs(stringResource(id = R.string.disable), !musicOnLockscreen) {
            dataStoreManager.musicOnLockscreen = flowOf(false)
        }

    }
}