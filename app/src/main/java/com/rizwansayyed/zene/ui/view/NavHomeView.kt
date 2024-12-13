package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.model.MusicPlayerData
import com.rizwansayyed.zene.ui.player.PlayerThumbnail
import com.rizwansayyed.zene.ui.theme.PurpleGrey40
import com.rizwansayyed.zene.ui.view.NavHomeMenu.*
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_FEED
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_HOME
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MY_MUSIC
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_RADIO
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SEARCH
import com.rizwansayyed.zene.utils.NavigationUtils.sendNavCommand
import com.rizwansayyed.zene.utils.Utils.vibratePhone
import com.rizwansayyed.zene.viewmodel.HomeNavModel

enum class NavHomeMenu {
    HOME, RADIO, SEARCH, MY_MUSIC
}

@Composable
fun NavHomeView(
    modifier: Modifier = Modifier, playerInfo: MusicPlayerData?, homeNavModel: HomeNavModel
) {
    val isBig = isScreenBig()
    var showMenu by remember { mutableStateOf(false) }

    Column(modifier.fillMaxWidth()) {
        Row {
            Spacer(Modifier.weight(1f))
            if (playerInfo?.player?.id != null) PlayerThumbnail(Modifier, playerInfo) {
                homeNavModel.showMusicPlayer(true)
            }
        }

        if (showMenu) Row(
            modifier
                .padding(bottom = if (isBig) 90.dp else 50.dp, end = 10.dp, start = 10.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.Black)
                .padding(14.dp), Arrangement.SpaceAround, Alignment.CenterVertically
        ) {
            NavHomeMenu.entries.forEach {
                MenuItems(it, homeNavModel.selectedMenuItems) {
                    when (it) {
                        HOME -> sendNavCommand(NAV_HOME)
                        RADIO -> sendNavCommand(NAV_RADIO)
                        SEARCH -> sendNavCommand(NAV_SEARCH)
                        MY_MUSIC -> sendNavCommand(NAV_MY_MUSIC)
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        showMenu = true
    }
}

@Composable
fun MenuItems(nav: NavHomeMenu, selected: NavHomeMenu, click: () -> Unit) {
    Column(Modifier.clickable { click() }, Arrangement.Center, Alignment.CenterHorizontally) {
        when (nav) {
            HOME -> ImageIcon(R.drawable.ic_home, 25)
            RADIO -> ImageIcon(R.drawable.ic_radio, 25)
            SEARCH -> ImageIcon(R.drawable.ic_search, 25)
            MY_MUSIC -> ImageIcon(R.drawable.ic_music_note_square, 25)
        }
        Spacer(Modifier.height(7.dp))

        if (selected == nav) {
            Spacer(Modifier.height(5.dp))
            Spacer(
                Modifier
                    .size(9.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.White)
            )
            Spacer(Modifier.height(5.dp))
        } else when (nav) {
            HOME -> TextPoppins(stringResource(R.string.home), size = 13)
            RADIO -> TextPoppins(stringResource(R.string.radio), size = 13)
            SEARCH -> TextPoppins(stringResource(R.string.search), size = 13)
            MY_MUSIC -> TextPoppins(stringResource(R.string.music), size = 13)
        }
    }
}