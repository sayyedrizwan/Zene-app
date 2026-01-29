package com.rizwansayyed.zene.ui.main.ent.discoverview

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.ui.main.home.EntSectionSelector
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.viewmodel.NavigationViewModel


@Composable
fun EntLifestyleLoadingView() {
    val pagerState = rememberPagerState { 9 }

    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
    ) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(20.dp))
            TextViewLight(stringResource(R.string.celebrity_lifestyle), 17, center = true)
            Spacer(Modifier.height(16.dp))

            HorizontalPager(
                pagerState,
                Modifier.fillMaxWidth(),
                PaddingValues(horizontal = 34.dp),
                pageSpacing = 16.dp,
            ) {
                ShimmerEffect(Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .size(350.dp, 500.dp))
            }
            Spacer(Modifier.height(12.dp))
            PagerDots(pagerState.pageCount, pagerState.currentPage)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun EntLifestyleView(data: ZeneMusicDataList, viewModel: NavigationViewModel) {
    val totalPages = data.size + 1
    val pagerState = rememberPagerState { totalPages }

    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
    ) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(Modifier.height(20.dp))
            TextViewLight(stringResource(R.string.celebrity_lifestyle), 17, center = true)
            Spacer(Modifier.height(16.dp))

            HorizontalPager(
                pagerState, Modifier.fillMaxWidth(),
                PaddingValues(horizontal = 34.dp), pageSpacing = 16.dp,
            ) { page ->
                if (page == data.size) {
                    ViewMoreCard {
                        viewModel.setEntNavigation(EntSectionSelector.LIFESTYLE)
                    }
                } else {
                CelebrityCard(data[page])
            }
            }
            Spacer(Modifier.height(12.dp))
            PagerDots(totalPages, pagerState.currentPage)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun ViewMoreCard(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(500.dp)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color.Black)
                .height(480.dp), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            ImageIcon(R.drawable.ic_arrow_right, 35)
            Spacer(Modifier.height(10.dp))
            TextViewBold(stringResource(R.string.view_more), 19)
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CelebrityCard(item: ZeneMusicData) {
    var fullInfoSheet by remember { mutableStateOf(false) }
    Column(
        Modifier.clickable {
            fullInfoSheet = true
        }, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GlideImage(
            model = item.thumbnail,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(15.dp))
                .height(500.dp)
        )

        Spacer(Modifier.height(16.dp))

        TextViewSemiBold(item.name ?: "", size = 16)
        TextViewLight(item.artists ?: "", size = 13)

        Spacer(Modifier.height(4.dp))
    }
}

@Composable
fun PagerDots(totalDots: Int, selectedIndex: Int) {
    Row(
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(if (index == selectedIndex) 8.dp else 6.dp)
                    .clip(CircleShape)
                    .background(if (index == selectedIndex) Color.Red else Color.DarkGray)
            )
        }
    }
}
