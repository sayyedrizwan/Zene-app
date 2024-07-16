package com.rizwansayyed.zene.ui.subscription

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.subscription.view.BuyNowBtn
import com.rizwansayyed.zene.ui.subscription.view.PremiumMusicDesc
import com.rizwansayyed.zene.ui.subscription.view.SubscriptionInfoCard
import com.rizwansayyed.zene.ui.subscription.view.UpgradeToBtn
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.PurpleGrey80
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageView
import com.rizwansayyed.zene.ui.view.TextAntroVenctra
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubscriptionView() {
    val pagerState = rememberPagerState(pageCount = { 10 })


    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
//        item {
//            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
//                Spacer(Modifier.height(90.dp))
//                ImageView(R.mipmap.logo, Modifier.size(90.dp))
//                Spacer(Modifier.height(20.dp))
//                TextAntroVenctra(stringResource(R.string.app_name_premium), size = 36)
//            }
//        }
//
//        item {
//            PremiumMusicDesc()
//        }
//
//        item {
//            UpgradeToBtn()
//        }
//
//        item {
//            Spacer(Modifier.height(50.dp))
//
//            LazyRow {
//                items(9) {
//                    SubscriptionInfoCard(
//                        R.drawable.ic_music_note_square, R.string.make_and_sync_playlists
//                    )
//                }
//            }
//        }

        item {
            Column(Modifier.padding(horizontal = 5.dp)) {
                Spacer(Modifier.height(120.dp))
                TextPoppinsSemiBold(stringResource(R.string.available_plans), size = 15)
                Spacer(Modifier.height(20.dp))
            }
        }
        item {
            HorizontalPager(pagerState, Modifier.fillMaxWidth(), PaddingValues(end = 64.dp)) {
                BuyNowBtn()
            }
        }
//        item { Spacer(Modifier.height(50.dp)) }
//        item { BuyNowBtn() }

        item {
            Spacer(Modifier.height(150.dp))
        }
    }
}