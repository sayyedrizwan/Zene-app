package com.rizwansayyed.zene.presenter.ui.musicplayer.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicPlayerData
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.service.player.utils.Utils.addAllPlayer
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageOfSongWithPlayIcon(
    item: MusicData?, pagerState: PagerState, page: Int, p: MusicPlayerData?
) {
    val width = LocalConfiguration.current.screenWidthDp.dp - 11.dp

    Box(
        Modifier
            .size(width)
            .padding(horizontal = 8.dp)
    ) {
        AsyncImage(item?.thumbnail, item?.name,
            Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .graphicsLayer {
                    val pageOffset = (
                            (pagerState.currentPage - page) + pagerState
                                .currentPageOffsetFraction
                            ).absoluteValue
                    alpha = lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                })

        if (item?.pId != p?.v?.songID)
            Image(
                painterResource(R.drawable.ic_play), "",
                Modifier
                    .padding(5.dp)
                    .padding(bottom = 17.dp)
                    .size(40.dp)
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(100))
                    .background(MainColor)
                    .padding(vertical = 10.dp)
                    .padding(start = 10.dp, end = 8.dp)
                    .clickable {
                        addAllPlayer(p?.songsLists?.toTypedArray(), page)
                    },
                colorFilter = ColorFilter.tint(Color.White)
            )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MusicTitleAndBodyText(p: MusicPlayerData?, pagerState: PagerState) {
    val homeNav: HomeNavViewModel = hiltViewModel()
    val coroutine = rememberCoroutineScope()

    TextBold(
        p?.songsLists?.get(pagerState.currentPage)?.name ?: "",
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp), true, size = 17
    )

    Spacer(Modifier.height(7.dp))

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp), Arrangement.Center, Alignment.CenterVertically
    ) {
        p?.songsLists?.get(pagerState.currentPage)?.artists?.split(",", "&")?.forEach {
            TextThin(it, Modifier.clickable {
                homeNav.setArtists(it)
                coroutine.launch {
                    val playerData = DataStorageManager.musicPlayerData.first()?.apply {
                        show = false
                    }
                    DataStorageManager.musicPlayerData = flowOf(playerData)
                }
            }, size = 15)
        }
    }
}