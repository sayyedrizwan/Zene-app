package com.rizwansayyed.zene.ui.main.lux

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.lux.billing.BillingManager
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@Composable
fun LuxItemView(billingManager: BillingManager, isMonthly: Boolean) {
    val monthlyCost by remember { derivedStateOf { billingManager.monthlyCost } }
    val yearlyCost by remember { derivedStateOf { billingManager.yearlyCost } }

    Column(
        Modifier
            .padding(5.dp)
            .shadow(10.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Black)
            .clickable {
                if (isMonthly) billingManager.buyMonthly()
                else billingManager.buyYearly()
            }
            .padding(vertical = 35.dp, horizontal = 3.dp),
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        TextViewSemiBold(stringResource(if (isMonthly) R.string.monthly else R.string.yearly), 21)
        Spacer(Modifier.height(5.dp))
        TextViewBold(if (isMonthly) monthlyCost else yearlyCost, 16)

        Spacer(Modifier.height(20.dp))

        ImageIcon(R.drawable.ic_arrow_up_right, 25)
    }
}