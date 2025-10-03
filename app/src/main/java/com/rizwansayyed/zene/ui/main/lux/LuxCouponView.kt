package com.rizwansayyed.zene.ui.main.lux

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.jakewharton.processphoenix.ProcessPhoenix
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingViewSmall
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun LuxCouponView(homeViewModel: HomeViewModel, show: () -> Unit) {
    var coupon by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val context = LocalActivity.current

    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .padding(10.dp)
            .fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        TextField(
            coupon,
            {
                coupon = if (it.length <= 100) it.replace("\n", "")
                else it.take(100).replace("\n", "")
            },
            Modifier
                .weight(1f)
                .padding(vertical = 4.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            placeholder = {
                TextViewNormal(stringResource(R.string.enter_a_coupon_code), 14)
            },
            leadingIcon = {
                ImageIcon(R.drawable.ic_discount, 24)
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MainColor,
                unfocusedContainerColor = MainColor,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White
            ),
            singleLine = true
        )

        Spacer(Modifier.width(5.dp))

        AnimatedVisibility(coupon.length > 3) {
            when (homeViewModel.couponApplied) {
                ResponseResult.Empty -> ImageWithBorder(R.drawable.ic_tick) {
                    focusManager.clearFocus()
                    homeViewModel.applyCouponCode(coupon) { ProcessPhoenix.triggerRebirth(context) }
                }

                is ResponseResult.Error -> ImageWithBorder(R.drawable.ic_tick) {
                    focusManager.clearFocus()
                    homeViewModel.applyCouponCode(coupon) { ProcessPhoenix.triggerRebirth(context) }
                }

                ResponseResult.Loading -> CircularLoadingViewSmall()
                is ResponseResult.Success -> LaunchedEffect(Unit) { show() }
            }

        }
    }
}