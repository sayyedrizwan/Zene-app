package com.rizwansayyed.zene.ui.subscription.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.GoldColor
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.PurpleGrey80
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextAntroVenctra
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.utils.SubscriptionPurchaseUtils

@Composable
fun SubscriptionInfoCard(img: Int, s: Int) {
    Box(
        Modifier
            .padding(8.dp)
            .size(200.dp, 260.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        Box(
            Modifier
                .align(Alignment.TopStart)
                .padding(7.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black)
                .padding(10.dp)
        ) {
            ImageIcon(img, 24)
        }

        Column(Modifier.align(Alignment.Center)) {
            TextPoppinsSemiBold(stringResource(s), true, Color.Black, 17)
        }

        Column(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            ImageIcon(R.drawable.ic_crown, 19, GoldColor)
        }
    }
}

@Composable
fun PremiumMusicDesc() {
    Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(Modifier.height(50.dp))
        TextPoppins(
            stringResource(R.string.zene_premium_is_for_music_enthusiast), true, PurpleGrey80, 19
        )

        Spacer(Modifier.height(50.dp))
        TextPoppins(
            stringResource(R.string.zene_premium_is_for_music_enthusiast_desc), true, size = 14
        )
    }
}

@Composable
fun UpgradeToBtn() {
    Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(Modifier.height(50.dp))

        Column(
            Modifier
                .padding(horizontal = 20.dp, vertical = 15.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { }
                .background(MainColor)
                .padding(horizontal = 10.dp, vertical = 15.dp)) {
            TextPoppins(
                stringResource(R.string.upgrade_to_zene_premium), true, Color.White, 15
            )
        }
    }
}

@Composable
fun BuyNowBtn(subscription: SubscriptionPurchaseUtils) {
    Column(
        Modifier
            .fillMaxWidth()
            .height(390.dp)
            .padding(top = 10.dp, bottom = 5.dp)
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Color.Black)
            .padding(horizontal = 16.dp),
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        TextAntroVenctra(stringResource(R.string.monthly), true, size = 44)
        Spacer(Modifier.height(20.dp))
        TextPoppins("$12 ${stringResource(R.string.per_month)}", true, size = 15)
        Spacer(Modifier.height(2.dp))
        TextPoppinsThin(stringResource(R.string.three_days_free_trial), true, size = 14)
        Spacer(Modifier.height(25.dp))
        Column(
            Modifier
                .padding(horizontal = 15.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(12.dp))
                .clickable { subscription.buy() }
                .background(MainColor)
                .padding(horizontal = 10.dp, vertical = 15.dp)) {
            TextPoppins(
                stringResource(R.string.upgrade).uppercase(), true, Color.White, 15
            )
        }
    }
}