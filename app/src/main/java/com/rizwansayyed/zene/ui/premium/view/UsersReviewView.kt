package com.rizwansayyed.zene.ui.premium.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold

val reviewList = listOf(
    "It's is amazing! Not only do I get uninterrupted music, but it’s also the most budget-friendly Pro version out there!",
    "I can’t believe how affordable Zene Pro is compared to others, and the ad-free experience makes it even better!",
    "Zene Pro delivers the ultimate ad-free experience, and it’s so affordable! Why pay more when you can get it all here? ",
    "Zene Pro makes music listening so much better. No ads mean no distractions, and I couldn’t be happier!",
    "Ads used to ruin my vibe, but Zene Pro fixed that. Plus, it’s the most economical Pro subscription I’ve seen.",
    "Affordable and effective! Zene Pro offers the affordable way to enjoy ad-free music without compromising quality.",
    "The cheapest Pro version with the best value! Ad-free music on Zene Pro is a dream come true for any music lover.",
    "Ads used to kill the vibe for me, but not anymore! Zene Pro is the best thing I’ve invested in for my music needs."
)


@Composable
fun UsersReviewView() {
    Column {
        Spacer(Modifier.height(80.dp))
        TextPoppinsSemiBold(stringResource(R.string.what_our_pro_users_say), size = 19)
        Spacer(Modifier.height(10.dp))

        LazyRow(Modifier.fillMaxWidth()) {
            items(reviewList) {
                Column(
                    Modifier
                        .padding(horizontal = 5.dp)
                        .width(280.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(MainColor)
                        .padding(10.dp)
                        .padding(vertical = 10.dp),
                    Arrangement.Center, Alignment.CenterHorizontally
                ) {
                    Row {
                        for (i in 1..5) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = Color.Yellow,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    TextPoppins(it, true, size = 16)
                }
            }
        }
    }
}