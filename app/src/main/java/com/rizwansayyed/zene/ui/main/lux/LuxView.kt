package com.rizwansayyed.zene.ui.main.lux

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.lux.billing.BillingManager
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBoldBig
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@Composable
fun LuxView() {
    val context = LocalActivity.current
    val billingManager by remember { mutableStateOf(BillingManager(context!!)) }

    Column(
        Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(20.dp))
        TextViewBoldBig(stringResource(R.string.app_name), 120)
        TextViewBoldBig(stringResource(R.string.lux), 120)
        Spacer(Modifier.height(10.dp))
        Column(Modifier.padding(horizontal = 10.dp)) {
            TextViewNormal(stringResource(R.string.lux_tag_line), 18)
        }

        Spacer(Modifier.height(35.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White)
                .clickable { billingManager.buyYearly() }
                .padding(vertical = 10.dp, horizontal = 25.dp),
            Arrangement.Center, Alignment.CenterVertically) {
            ImageIcon(R.drawable.ic_crown, 21, Color.Black)
            Spacer(Modifier.width(10.dp))
            TextViewSemiBold(
                stringResource(R.string.start_free_trial), 17, Color.Black
            )
        }

        Spacer(Modifier.height(40.dp))
        TextViewNormal(stringResource(R.string.lux_only_for_music_enthusiasts), 15, center = true)
        Spacer(Modifier.height(20.dp))
        TextViewNormal(stringResource(R.string.upgrading_lux_give_ad_free), 15, center = true)

        Spacer(Modifier.height(80.dp))
        BillingPeriod.entries.forEach {
            LuxItemView(billingManager, it)
            Spacer(Modifier.height(20.dp))
        }

        TextViewLight(
            stringResource(R.string.you_can_cancel_subscription_anytime), 15, center = true
        )

        Spacer(Modifier.height(400.dp))
    }


    LaunchedEffect(Unit) {
        billingManager.startConnection()
    }
}