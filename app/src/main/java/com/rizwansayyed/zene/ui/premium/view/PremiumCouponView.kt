package com.rizwansayyed.zene.ui.premium.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.db.DataStoreManager.isUserPremiumDB
import com.rizwansayyed.zene.ui.premium.viewmodel.PremiumViewModel
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.flow.flowOf


@Composable
fun PremiumCouponView(viewModel: PremiumViewModel, success: () -> Unit) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically
    ) {
        TextField(
            value = searchQuery,
            onValueChange = {
                if (it.length <= 25) searchQuery = it
            },
            keyboardActions = KeyboardActions(
                onDone = { viewModel.couponCodeVerify(searchQuery) }
            ),
            placeholder = {
                TextPoppins(
                    stringResource(R.string.enter_coupon_code), false, Color.Gray, 15
                )
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                focusedContainerColor = MainColor,
                unfocusedContainerColor = MainColor,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            maxLines = 1,
            singleLine = true,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp)
                .height(60.dp)
                .weight(1f),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_discount_voucher),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        )

        if (isLoading) LoadingView(Modifier.size(32.dp))
        else Box(
            Modifier
                .padding(vertical = 2.dp, horizontal = 6.dp)
                .clip(RoundedCornerShape(100))
                .background(Color.Black)
                .clickable {
                    viewModel.couponCodeVerify(searchQuery)
                }
                .border(1.dp, Color.White, RoundedCornerShape(100))
                .padding(vertical = 9.dp, horizontal = 18.dp)) {
            ImageIcon(R.drawable.ic_tick, size = 20)
        }
    }

    LaunchedEffect(viewModel.couponCodeStatus) {
        isLoading = false
        when (val v = viewModel.couponCodeStatus) {
            APIResponse.Empty -> {}
            is APIResponse.Error ->
                context.resources.getString(R.string.enter_a_valid_coupon_code).toast()

            APIResponse.Loading -> {
                keyboardController?.hide()
                isLoading = true
            }

            is APIResponse.Success -> {
                if (v.data) {
                    isUserPremiumDB = flowOf(true)
                    success()
                }
                else context.resources.getString(R.string.enter_a_valid_coupon_code).toast()
            }
        }
    }
}