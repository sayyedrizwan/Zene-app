package com.rizwansayyed.zene.ui.view

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.GoldColor
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SUBSCRIPTION
import com.rizwansayyed.zene.utils.NavigationUtils.sendNavCommand

@Composable
fun JoinPremiumCommunity(modifier: Modifier = Modifier) {
    Row(
        modifier
            .padding(top = 10.dp, start = 8.dp)
            .border(BorderStroke(1.dp, GoldColor), RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(GoldColor.copy(0.4f))
            .padding(vertical = 5.dp, horizontal = 9.dp)
            .clickable { sendNavCommand(NAV_SUBSCRIPTION) }) {
        ImageIcon(R.drawable.ic_crown, 20, GoldColor)
        Spacer(Modifier.width(9.dp))
        TextPoppins(
            stringResource(R.string.join_premium_community), false, GoldColor, 14
        )
    }
}

@Composable
fun UpgradeToPremiumCard() {
    Column(
        Modifier
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
    ) {
        Spacer(Modifier.height(30.dp))
        TextPoppinsSemiBold(stringResource(R.string.join_zene_premium), true, size = 18)
        Spacer(Modifier.height(15.dp))
        TextPoppins(stringResource(R.string.join_premium_community_desc), true, size = 14)

        Spacer(Modifier.height(20.dp))
        Row(
            Modifier
                .padding(5.dp)
                .padding(horizontal = 10.dp)
                .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(14.dp))
                .padding(5.dp).clickable {
                    sendNavCommand(NAV_SUBSCRIPTION)
                }
        ) {
            TextPoppins(
                stringResource(R.string.upgrade_if_you_are_a_music_enthusiasts),
                true, size = 13
            )
        }

        Spacer(Modifier.height(20.dp))
        Row(
            Modifier
                .padding(5.dp)
                .padding(horizontal = 10.dp)
                .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(14.dp))
                .padding(5.dp)
        ) {
            TextPoppins(
                stringResource(R.string.share_zene_friends_family),
                true, size = 13
            )
        }

        Spacer(Modifier.height(30.dp))
    }

    Spacer(Modifier.height(40.dp))
}