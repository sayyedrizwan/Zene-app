package com.rizwansayyed.zene.presenter.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.presenter.theme.DarkBlack
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainSplashView() {
    val columnModifier = Modifier
        .fillMaxSize()
        .background(DarkBlack)
    val pagerState = rememberPagerState(pageCount = {
        10
    })
    Box(Modifier.fillMaxWidth()) {
        HorizontalPager(state = pagerState) { page ->
            Text(
                text = "Page: $page",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }

//        val coroutineScope = rememberCoroutineScope()
//        Button(onClick = {
//            coroutineScope.launch {
//                // Call scroll to on pagerState
//                pagerState.animateScrollToPage(pagerState.currentPage + 1)
//            }
//        }, modifier = Modifier.align(Alignment.BottomCenter)) {
//            Text("Jump to Page 5")
//        }
    }
}