package com.rizwansayyed.zene.ui.main.lux

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.rizwansayyed.zene.ui.main.lux.BillingPeriod.MONTHLY
import com.rizwansayyed.zene.ui.main.lux.BillingPeriod.SEMI_ANNUAL
import com.rizwansayyed.zene.ui.main.lux.BillingPeriod.Yearly
import com.rizwansayyed.zene.ui.main.lux.billing.BillingManager
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

enum class BillingPeriod {
    MONTHLY, SEMI_ANNUAL, Yearly
}

@Composable
fun LuxItemView(billingManager: BillingManager, period: BillingPeriod) {
    val monthlyCost by remember { derivedStateOf { billingManager.monthlyCost } }
    val yearlyCost by remember { derivedStateOf { billingManager.yearlyCost } }
    val semiAnnualCost by remember { derivedStateOf { billingManager.semiAnnualCost } }

    Row(
        Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .shadow(10.dp)
            .clip(RoundedCornerShape(14.dp))
            .shadow(10.dp)
            .background(Color.Black)
            .clickable {
                when (period) {
                    MONTHLY -> billingManager.buyMonthly()
                    SEMI_ANNUAL -> billingManager.buySemiAnnual()
                    Yearly -> billingManager.buyMonthly()
                }
            }
            .padding(vertical = 40.dp, horizontal = 20.dp),
        Arrangement.Center, Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f), Arrangement.Center) {
            when (period) {
                MONTHLY -> {
                    TextViewSemiBold(stringResource(R.string.monthly), 17)
                    Spacer(Modifier.height(5.dp))
                    TextViewBold(monthlyCost, 30)
                }

                SEMI_ANNUAL -> {
                    TextViewSemiBold(stringResource(R.string.semi_annual), 17)
                    Spacer(Modifier.height(5.dp))
                    TextViewBold(semiAnnualCost, 30)
                }

                Yearly -> {
                    TextViewSemiBold(stringResource(R.string.yearly), 17)
                    Spacer(Modifier.height(5.dp))
                    TextViewBold(yearlyCost, 30)
                }
            }
        }
        ButtonWithBorder(stringResource(R.string.upgrade))
    }
}