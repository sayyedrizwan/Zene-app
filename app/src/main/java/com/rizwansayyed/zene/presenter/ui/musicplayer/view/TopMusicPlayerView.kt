package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.musicplayer.utils.Utils
import com.rizwansayyed.zene.viewmodel.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun TopPlayerHeader(showedOnLockScreen: Boolean) {
    val coroutine = rememberCoroutineScope()
    val activity = LocalContext.current as Activity
    var dropDownMenu by remember { mutableStateOf(false) }


    val ringtone = stringResource(R.string.set_as_ringtone)
    val homeScreenWallpaper = stringResource(R.string.set_home_screen_wallpaper)
    val lockScreenWallpaper = stringResource(R.string.set_lock_screen_wallpaper)

    Row(
        Modifier
            .padding(top = 25.dp)
            .fillMaxWidth(),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        SmallIcons(
            if (showedOnLockScreen) R.drawable.ic_arrow_left else R.drawable.ic_arrow_down_sharp,
            25, 10
        ) {
            if (showedOnLockScreen)
                activity.finish()
            else
                coroutine.launch(Dispatchers.IO) {
                    val m = musicPlayerData.first()?.apply { show = false }
                    musicPlayerData = flowOf(m)
                }
        }

        TextSemiBold(
            stringResource(R.string.zene_music_player),
            Modifier
                .weight(1f)
                .fillMaxWidth(), true
        )


        Box {
            SmallIcons(R.drawable.ic_more_menu, 25, 10) {
                dropDownMenu = !dropDownMenu
            }

            Column(Modifier.offset(x = (-200).dp, y = 0.dp)) {
                DropdownMenu(dropDownMenu, { dropDownMenu = false }) {
                    DropdownMenuItem({ TextSemiBold(ringtone) }, onClick = {

                    })
                    DropdownMenuItem({ TextSemiBold(homeScreenWallpaper) }, onClick = {

                    })
                    DropdownMenuItem({ TextSemiBold(lockScreenWallpaper) }, onClick = {

                    })
                }
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongsThumbnailsWithList(p: MusicPlayerData?) {
    val playerViewModel: PlayerViewModel = hiltViewModel()
    Spacer(Modifier.height(20.dp))

    val pagerState = rememberPagerState(pageCount = { p?.songsLists?.size ?: 0 })

    HorizontalPager(
        pagerState, Modifier.fillMaxWidth(), PaddingValues(horizontal = 15.dp)
    ) { page ->
        val item = p?.songsLists?.get(page)

        ImageOfSongWithPlayIcon(item, pagerState, page, p)
    }

    Spacer(Modifier.height(7.dp))

    if ((p?.songsLists?.size ?: 0) > 0)
        MusicTitleAndBodyText(p, pagerState)

    LaunchedEffect(p) {
        p?.songsLists?.forEachIndexed { i, musicData ->
            if (musicData?.pId == p.v?.songID) pagerState.scrollToPage(i)
        }
    }
    LaunchedEffect(pagerState.currentPage) {
        playerViewModel.setMusicType(Utils.MusicViewType.MUSIC)
    }
}
