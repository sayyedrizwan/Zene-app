package com.rizwansayyed.zene.ui.musicplayer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MediaContentUtils
import kotlin.math.absoluteValue


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MusicPlayingListView(player: MusicPlayerData?, data: ZeneMusicData?) {
    Row(Modifier
        .fillMaxWidth()
        .clickable {
            MediaContentUtils.startMedia(data, player?.lists ?: emptyList())
        }
        .padding(horizontal = 9.dp, vertical = 9.dp),
        Arrangement.Center,
        Alignment.CenterVertically) {
        GlideImage(
            data?.thumbnail,
            data?.name,
            Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Fit
        )

        Column(
            Modifier
                .padding(horizontal = 7.dp)
                .weight(1f)
        ) {
            TextViewBold(data?.name ?: "", 15, line = 2)
            if (data?.type() == MusicDataTypes.SONGS) {
                Spacer(Modifier.height(1.dp))
                TextViewNormal(data.artists ?: "", 13, line = 1)
            }
        }

        if (player?.data?.id == data?.id) {
            GlideImage(
                R.raw.song_playing_wave, "", Modifier.size(24.dp), contentScale = ContentScale.Crop
            )
        } else {
            Box(Modifier) {
                ImageIcon(R.drawable.ic_play, 19)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MusicPlayingGridView(
    player: MusicPlayerData?, page: Int, pagerState: PagerState, screenWidth: Dp
) {
    Column(
        Modifier
            .offset(
                y = if (page == pagerState.currentPage) (-120).dp else 0.dp
            )
            .animateContentSize()
            .fillMaxSize()
            .graphicsLayer {
                val pageOffset =
                    (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction

                alpha = lerp(0.5f, 1f, 1f - pageOffset.absoluteValue.coerceIn(0f, 1f))
            }, Arrangement.Top, Alignment.CenterHorizontally
    ) {
        Box(Modifier.size(screenWidth, screenWidth)) {
            GlideImage(
                player?.lists?.get(page)?.thumbnail,
                player?.lists?.get(page)?.name,
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Fit
            )

            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(15.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black)
                    .padding(10.dp)
            ) {
                if (player?.data?.id == player?.lists?.get(page)?.id) {
                    GlideImage(
                        R.raw.song_playing_wave,
                        "",
                        Modifier.size(25.dp),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(Modifier.clickable {
                        MediaContentUtils.startMedia(
                            player?.lists?.get(page), player?.lists ?: emptyList()
                        )
                    }) {
                        ImageIcon(R.drawable.ic_play, 20)
                    }
                }
            }
        }

        AnimatedVisibility(page == pagerState.currentPage) {
            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.End) {
                Spacer(Modifier.height(6.dp))
                TextViewBold(player?.lists?.get(page)?.name ?: "", 25, center = true, line = 2)
                if (player?.lists?.get(page)?.type() == MusicDataTypes.SONGS) {
                    TextViewNormal(
                        player.lists[page]?.artists ?: "", 13, center = true, line = 1
                    )
                }
            }
        }
    }
}
