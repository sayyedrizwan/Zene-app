package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.musicPlayerData
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun TopPlayerHeader() {
    val coroutine = rememberCoroutineScope()

    Row(
        Modifier
            .padding(top = 25.dp)
            .fillMaxWidth(),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        SmallIcons(R.drawable.ic_arrow_down_sharp, 25, 10) {
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

        SmallIcons(R.drawable.ic_more_menu, 25, 10) {

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongsThumbnailsWithList(p: MusicPlayerData?, player: ExoPlayer) {
    Spacer(Modifier.height(20.dp))

    val pagerState = rememberPagerState(pageCount = { p?.songsLists?.size ?: 0 })

    HorizontalPager(
        pagerState, Modifier.fillMaxWidth(), PaddingValues(horizontal = 15.dp)
    ) { page ->
        val item = p?.songsLists?.get(page)

        ImageOfSongWithPlayIcon(item, pagerState, page, p)
    }

    Spacer(Modifier.height(7.dp))

    MusicTitleAndBodyText(p, pagerState)

    MusicPlayerSliders(player)


    LaunchedEffect(p) {
        p?.songsLists?.forEachIndexed { i, musicData ->
            if (musicData?.pId == p.v?.songID) pagerState.scrollToPage(i)
        }
    }
}
