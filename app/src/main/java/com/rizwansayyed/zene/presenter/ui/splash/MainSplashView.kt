package com.rizwansayyed.zene.presenter.ui.splash

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextAntroSemiBold
import com.rizwansayyed.zene.presenter.ui.TextMedium
import kotlinx.coroutines.launch


val textSplashScreen = listOf(
    context.resources.getText(R.string.enjoy_free_access_one),
    context.resources.getText(R.string.enjoy_free_access_two),
    context.resources.getText(R.string.enjoy_free_access_three)
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainSplashView() {
    val coroutine = rememberCoroutineScope()

    var hideTempSplash by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { 3 })
    val appName = stringResource(R.string.app_name)

    val animateBtnColor by animateColorAsState(
        if (pagerState.canScrollForward) Color.White else MainColor,
        label = "color"
    )

    if (hideTempSplash)
        Box(Modifier.fillMaxSize()) {
            HorizontalPager(pagerState, Modifier.fillMaxSize()) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(if (it == 0) MainColor else if (it == 1) DarkGreyColor else BlackColor)
                ) {}
            }

            TextAntroSemiBold(
                appName,
                Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .animateContentSize()
                    .padding(vertical = 50.dp),
                color = Color.White,
                singleLine = true,
                doCenter = true,
                size = 85
            )

            Column(
                Modifier
                    .padding(bottom = 40.dp)
                    .padding(11.dp)
                    .align(Alignment.BottomCenter)
            ) {

                TextMedium(
                    v = textSplashScreen[pagerState.currentPage].toString(),
                    Modifier.fillMaxWidth(), size = 20
                )

                Spacer(Modifier.height(25.dp))

                Row {
                    for (i in 0 until pagerState.pageCount) {
                        Column(
                            Modifier
                                .animateContentSize()
                                .padding(bottom = 14.dp)
                                .padding(horizontal = 4.dp)
                                .size(if (pagerState.currentPage == i) 26.dp else 3.dp, 3.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color.White)
                        ) {}
                    }
                }

                CardButton(
                    if (pagerState.canScrollForward) stringResource(R.string.next) else
                        stringResource(R.string.get_started),
                    animateBtnColor,
                    if (pagerState.canScrollForward) Color.Black else Color.White, Modifier
                ) {
                    coroutine.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                }
            }
        }
    else
        SplashScreenMain {
            hideTempSplash = true
        }


//    Box(Modifier.fillMaxWidth()) {
//        HorizontalPager(state = pagerState) { page ->
//            Text(
//                text = "Page: $page",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(100.dp)
//            )
//        }
//
////        val coroutineScope = rememberCoroutineScope()
////        Button(onClick = {
////            coroutineScope.launch {
////                // Call scroll to on pagerState
////                pagerState.animateScrollToPage(pagerState.currentPage + 1)
////            }
////        }, modifier = Modifier.align(Alignment.BottomCenter)) {
////            Text("Jump to Page 5")
////        }
//    }
}