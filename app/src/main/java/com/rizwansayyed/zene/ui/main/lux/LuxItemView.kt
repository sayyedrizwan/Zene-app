package com.rizwansayyed.zene.ui.main.lux

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.main.lux.BillingPeriod.MONTHLY
import com.rizwansayyed.zene.ui.main.lux.BillingPeriod.SEMI_ANNUAL
import com.rizwansayyed.zene.ui.main.lux.BillingPeriod.Yearly
import com.rizwansayyed.zene.ui.main.lux.billing.BillingManager
import com.rizwansayyed.zene.ui.theme.LuxColor
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

enum class BillingPeriod {
    MONTHLY, SEMI_ANNUAL, Yearly
}

@Composable
fun LuxItemView(billingManager: BillingManager) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BoostCard(SEMI_ANNUAL, Modifier.fillMaxWidth(), billingManager)
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BillingPeriod.entries.forEach { option ->
            if (option != SEMI_ANNUAL) BoostCard(option, Modifier.weight(1f), billingManager)
        }
    }
}

@Composable
fun BoostCard(
    option: BillingPeriod, modifier: Modifier = Modifier, billingManager: BillingManager
) {
    val monthlyCost by remember { derivedStateOf { billingManager.monthlyCost } }
    val yearlyCost by remember { derivedStateOf { billingManager.yearlyCost } }
    val semiAnnualCost by remember { derivedStateOf { billingManager.semiAnnualCost } }

    val bgColor = when (option) {
        MONTHLY -> MainColor
        SEMI_ANNUAL -> Color.Black
        Yearly -> LuxColor
    }


    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(1.dp)
            .clickable {
                when (option) {
                    MONTHLY -> billingManager.buyMonthly()
                    SEMI_ANNUAL -> billingManager.buySemiAnnual()
                    Yearly -> billingManager.buyMonthly()
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(Modifier.height(20.dp))
        when (option) {
            MONTHLY -> {
                TextViewBold("1", 35)
                TextViewSemiBold(stringResource(R.string.months), 17)
                Spacer(Modifier.height(10.dp))
                TextViewBold(monthlyCost, 25)
            }

            SEMI_ANNUAL -> {
                Spacer(Modifier.height(1.dp))
                ButtonWithBorder(stringResource(R.string.popular))
                Spacer(Modifier.height(5.dp))
                TextViewBold("6", 35)
                TextViewSemiBold(stringResource(R.string.months), 17)
                Spacer(Modifier.height(10.dp))
                TextViewBold(semiAnnualCost, 25)
            }

            Yearly -> {
                TextViewBold("12", 35)
                TextViewSemiBold(stringResource(R.string.months), 17)
                Spacer(Modifier.height(10.dp))
                TextViewBold(yearlyCost, 25)
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}