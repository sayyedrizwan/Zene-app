package com.rizwansayyed.zene.ui.premium.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.search.view.SearchInputView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.InputTypes
import com.rizwansayyed.zene.ui.view.SearchScreenBar
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PremiumCouponView() {
    var couponAlert by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .clickable { couponAlert = true }, Arrangement.Center, Alignment.CenterHorizontally
    ) {
        TextPoppinsSemiBold(stringResource(R.string.have_a_coupon), true, Color.White, 19)
    }

    if (couponAlert) CouponsDialog {
        couponAlert = false
    }
}

@Composable
fun CouponsDialog(close: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

    Dialog(close, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardColors(DarkCharcoal, DarkCharcoal, DarkCharcoal, DarkCharcoal)
        ) {
            Column(Modifier.fillMaxSize()) {
                SearchScreenBar(InputTypes.SEARCH, searchQuery, {
                    searchQuery = it
                }) {
                    searchQuery = it
                }
            }
        }
    }
}