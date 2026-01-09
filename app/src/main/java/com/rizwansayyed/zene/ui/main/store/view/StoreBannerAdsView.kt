package com.rizwansayyed.zene.ui.main.store.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

private val bannerList = listOf(
    "https://i.ibb.co/s94Twz14/Gemini-Generated-Image-mdaq8xmdaq8xmdaq-1.png",
    "https://i.ibb.co/0jqL19Kw/Gemini-Generated-Image-75w7mi75w7mi75w7-1.png",
    "https://i.ibb.co/twKR8tj0/Gemini-Generated-Image-x5vmf8x5vmf8x5vm-1.png"
)

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun StoreBannerAdsView(pageDurationMs: Int = 4000) {
    val pagerState = rememberPagerState { bannerList.size }
    val progress = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    var autoScrollJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.isScrollInProgress }.distinctUntilChanged()
            .collect { isScrolling ->
                if (isScrolling) {
                    autoScrollJob?.cancel()
                    progress.stop()
                    progress.snapTo(0f)
                }
            }
    }

    LaunchedEffect(pagerState.currentPage) {
        autoScrollJob?.cancel()

        autoScrollJob = scope.launch {
            progress.snapTo(0f)

            progress.animateTo(
                1f, tween(pageDurationMs, easing = LinearEasing)
            )
            val nextPage = if (pagerState.currentPage == bannerList.lastIndex) 0
            else pagerState.currentPage + 1

            scope.launch {
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(
            pagerState, Modifier.fillMaxWidth(), PaddingValues(horizontal = 20.dp)
        ) { page ->
            GlideImage(
                bannerList[page], null, Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(bannerList.size) { index ->
                if (index == pagerState.currentPage) {
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(28.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress.value)
                                .background(Color.Red)
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }

                if (index != bannerList.lastIndex) {
                    Spacer(Modifier.width(8.dp))
                }
            }
        }
    }
}

