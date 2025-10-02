package com.rizwansayyed.zene.ui.main.lux

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.rizwansayyed.zene.ui.main.lux.billing.BillingManager
import kotlin.math.absoluteValue

enum class BillingPeriod {
    MONTHLY, SEMI_ANNUAL, Yearly
}

@Composable
fun LuxItemView(billingManager: BillingManager) {
    val itemCount = BillingPeriod.entries.size
    val pagerState = rememberPagerState(pageCount = { Int.MAX_VALUE })

    HorizontalPager(
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 64.dp),
    ) { page ->
        val cardText = BillingPeriod.entries[page % itemCount]

        Card(
            Modifier
                .size(200.dp)
                .graphicsLayer {
                    val pageOffset = ((pagerState.currentPage - page) + pagerState
                        .currentPageOffsetFraction).absoluteValue
                    alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "cardText",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }


//    val monthlyCost by remember { derivedStateOf { billingManager.monthlyCost } }
//    val yearlyCost by remember { derivedStateOf { billingManager.yearlyCost } }
//    val semiAnnualCost by remember { derivedStateOf { billingManager.semiAnnualCost } }
//
//    Row(
//        Modifier
//            .padding(5.dp)
//            .fillMaxWidth()
//            .shadow(10.dp)
//            .clip(RoundedCornerShape(14.dp))
//            .shadow(10.dp)
//            .background(Color.Black)
//            .clickable {
//                when (period) {
//                    MONTHLY -> billingManager.buyMonthly()
//                    SEMI_ANNUAL -> billingManager.buySemiAnnual()
//                    Yearly -> billingManager.buyMonthly()
//                }
//            }
//            .padding(vertical = 40.dp, horizontal = 20.dp),
//        Arrangement.Center, Alignment.CenterVertically
//    ) {
//        Column(Modifier.weight(1f), Arrangement.Center) {
//            when (period) {
//                MONTHLY -> {
//                    TextViewSemiBold(stringResource(R.string.monthly), 17)
//                    Spacer(Modifier.height(5.dp))
//                    TextViewBold(monthlyCost, 30)
//                }
//
//                SEMI_ANNUAL -> {
//                    TextViewSemiBold(stringResource(R.string.semi_annual), 17)
//                    Spacer(Modifier.height(5.dp))
//                    TextViewBold(semiAnnualCost, 30)
//                }
//
//                Yearly -> {
//                    TextViewSemiBold(stringResource(R.string.yearly), 17)
//                    Spacer(Modifier.height(5.dp))
//                    TextViewBold(yearlyCost, 30)
//                }
//            }
//        }
//        ButtonWithBorder(stringResource(R.string.upgrade))
//    }
}