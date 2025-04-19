package com.rizwansayyed.zene.ui.main.lux

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageWithBgRound
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.ui.view.TextViewNormal

@Composable
fun LuxCouponView() {
    var coupon by remember { mutableStateOf("") }

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
            TextFieldValue(text = coupon, selection = TextRange(coupon.length)),
            {
                coupon = if (it.text.length <= 100) it.text else it.text.take(100)
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

        AnimatedVisibility(coupon.length > 3) {
            ImageWithBorder(R.drawable.ic_tick) { }
        }
    }
}