package com.rizwansayyed.zene.ui.premium.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.premium.utils.PremiumUtils
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold

enum class PremiumBuyingType {
    MONTHLY, YEARLY
}

@Composable
fun PremiumBuyingCards(premium: PremiumUtils = hiltViewModel()) {
    val context = LocalContext.current as Activity

    var selectedSubscription by remember { mutableStateOf(PremiumBuyingType.YEARLY) }

    if (premium.isLoading) Row(
        Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically
    ) {
        LoadingView(Modifier.size(32.dp))
    } else LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        item {
            Column(
                Modifier
                    .padding(horizontal = 5.dp)
                    .fillMaxWidth()
                    .clickable {
                        selectedSubscription = PremiumBuyingType.MONTHLY
                    }
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, Color.White, RoundedCornerShape(14.dp))
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                TextPoppins(stringResource(R.string.monthly), size = 14, limit = 1)
                Spacer(Modifier.width(10.dp))
                TextPoppinsSemiBold(premium.monthlyPricing, size = 20)
                Spacer(Modifier.height(5.dp))

                Spacer(
                    Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(100))
                        .background(if (selectedSubscription == PremiumBuyingType.MONTHLY) Color.White else Color.Black)
                )
            }
        }

        item {
            Column(
                Modifier
                    .padding(horizontal = 5.dp)
                    .fillMaxWidth()
                    .clickable {
                        selectedSubscription = PremiumBuyingType.YEARLY
                    }
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, Color.White, RoundedCornerShape(14.dp))
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                TextPoppins(stringResource(R.string.yearly), size = 14, limit = 1)
                Spacer(Modifier.width(10.dp))
                TextPoppinsSemiBold(premium.yearlyPricing, size = 20)
                Spacer(Modifier.height(5.dp))

                Spacer(
                    Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(100))
                        .background(if (selectedSubscription == PremiumBuyingType.YEARLY) Color.White else Color.Black)
                )
            }
        }

        item(span = { GridItemSpan(2) }) {
            Row(
                Modifier
                    .padding(top = 15.dp)
                    .padding(vertical = 17.dp, horizontal = 14.dp)
                    .clickable {
                        premium.buySubscription(
                            context, selectedSubscription == PremiumBuyingType.YEARLY
                        )
                    }
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(13.dp))
                    .background(MainColor)
                    .padding(vertical = 15.dp),
                Arrangement.Center, Alignment.CenterVertically
            ) {
                TextPoppinsSemiBold(stringResource(R.string.start_free_trial), true, size = 14)
            }
        }
    }

    LaunchedEffect(Unit) {
        premium.start()
    }
}