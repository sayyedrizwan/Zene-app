package com.rizwansayyed.zene.ui.main.ent.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.NavigationUtils
import com.rizwansayyed.zene.utils.share.MediaContentUtils.startMedia
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun TopSliderVideoNewsView(news: List<ZeneMusicData?>?) {
    val pagerState = rememberPagerState(pageCount = { news?.size ?: 0 })
    val coroutines = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }
    var text by remember { mutableStateOf("") }

    HorizontalPager(pagerState, Modifier.fillMaxWidth()) { page ->
        Box(
            Modifier
                .combinedClickable(onLongClick = { NavigationUtils.triggerInfoSheet(news?.get(page)) },
                    onClick = { startMedia(news?.get(page)) })
                .fillMaxWidth()
                .padding(horizontal = 3.dp)
        ) {
            GlideImage(
                news?.get(page)?.thumbnail,
                news?.get(page)?.name,
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
            )

            Box(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black)
                    .padding(9.dp), Alignment.Center
            ) {
                ImageIcon(R.drawable.ic_play, 20)
            }
        }
    }

    if (pagerState.pageCount > 0) {
        Spacer(Modifier.height(10.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
        ) {
            TextViewSemiBold(text, 14, center = true)
        }

        Spacer(Modifier.height(14.dp))

        Row(
            Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterVertically
        ) {
            repeat(pagerState.pageCount) {
                Spacer(
                    Modifier
                        .padding(horizontal = 5.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .width(if (pagerState.currentPage == it) 30.dp else 7.dp)
                        .height(7.dp)
                )
            }
        }

        LaunchedEffect(pagerState.currentPage) {
            text = news?.get(pagerState.currentPage)?.name ?: ""
        }

        DisposableEffect(true) {
            job?.cancel()
            job = coroutines.launch {
                while (true) {
                    delay(4.seconds)
                    if (!pagerState.isScrollInProgress) {
                        if (pagerState.pageCount == (pagerState.currentPage + 1))
                            pagerState.animateScrollToPage(0)
                        else
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }

            onDispose {
                job?.cancel()
            }
        }


    }

}