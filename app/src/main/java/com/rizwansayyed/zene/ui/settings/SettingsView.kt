package com.rizwansayyed.zene.ui.settings

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.home.homenavmodel.HomeNavViewModel
import com.rizwansayyed.zene.ui.settings.view.MusicLockScreen
import com.rizwansayyed.zene.ui.settings.view.OfflineOptionSettings
import com.rizwansayyed.zene.ui.settings.view.PlaylistImportSettings
import com.rizwansayyed.zene.ui.settings.view.SeekSettings
import com.rizwansayyed.zene.ui.settings.view.SettingsExtraInfo
import com.rizwansayyed.zene.ui.settings.view.SettingsPlayingSpeed
import com.rizwansayyed.zene.utils.QuickSandBold
import com.rizwansayyed.zene.utils.QuickSandRegular

@Composable
fun SettingsView(homeNavViewModel: HomeNavViewModel = hiltViewModel()) {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        QuickSandBold(
            stringResource(id = R.string.settings),
            size = 28,
            modifier = Modifier.padding(15.dp)
        )
        OfflineOptionSettings()

        Spacer(Modifier.height(20.dp))

        SeekSettings()

        Spacer(Modifier.height(20.dp))

        SettingsPlayingSpeed(homeNavViewModel)

        Spacer(Modifier.height(20.dp))

        MusicLockScreen()

        Spacer(Modifier.height(20.dp))

        PlaylistImportSettings()

        Spacer(Modifier.height(20.dp))

        SettingsExtraInfo()

        Spacer(Modifier.height(180.dp))
    }
}


@Composable
fun ViewLocalSongs(txt: String, doTick: Boolean, click: () -> Unit) {
    Row(
        Modifier
            .fillMaxSize()
            .clickable {
                click()
            }, Arrangement.Start, Alignment.CenterVertically
    ) {
        QuickSandRegular(
            txt,
            size = 14,
            modifier = Modifier
                .padding(15.dp)
                .weight(1f),
            align = TextAlign.Start
        )

        if (doTick) Image(
            painterResource(id = R.drawable.ic_tick),
            "",
            Modifier
                .size(30.dp)
                .animateContentSize(),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}


@Composable
fun ViewLocalSongsImport(txt: String, click: () -> Unit) {
    Row(
        Modifier
            .fillMaxSize()
            .clickable {
                click()
            }, Arrangement.Start, Alignment.CenterVertically
    ) {
        QuickSandRegular(
            txt,
            size = 14,
            modifier = Modifier
                .padding(15.dp)
                .weight(1f),
            align = TextAlign.Start
        )

        Image(
            painterResource(id = R.drawable.ic_arrow_right_round),
            "",
            Modifier
                .size(30.dp)
                .padding(4.dp)
                .animateContentSize(),
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}