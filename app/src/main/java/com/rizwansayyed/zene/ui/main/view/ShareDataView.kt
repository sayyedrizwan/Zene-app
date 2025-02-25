package com.rizwansayyed.zene.ui.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.theme.FacebookColor
import com.rizwansayyed.zene.ui.theme.InstagramColor
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.PinterestColor
import com.rizwansayyed.zene.ui.theme.SnapchatColor
import com.rizwansayyed.zene.ui.theme.WhatsAppColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal

@Composable
fun ShareDataView(data: ZeneMusicData?, close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        Box(Modifier
            .clickable { close() }
            .fillMaxSize()
            .background(Color.Black.copy(0.5f))) {
            Row(
                Modifier
                    .padding(bottom = 30.dp)
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(13.dp))
                    .background(MainColor)
                    .padding(vertical = 30.dp)
                    .horizontalScroll(rememberScrollState())
                    .align(Alignment.BottomCenter)
            ) {

                ShareRoundIcon(R.drawable.ic_link, R.string.copy_link) {

                }

                ShareRoundIcon(R.drawable.ic_share, R.string.share_to_) {

                }

                ShareRoundIcon(R.drawable.ic_hotspot, R.string.connect_) {

                }

                ShareRoundIcon(R.drawable.ic_whatsapp, R.string.whatsapp, WhatsAppColor) {

                }

                ShareRoundIcon(R.drawable.ic_instagram, R.string.instagram, InstagramColor) {

                }

                ShareRoundIcon(R.drawable.ic_snapchat, R.string.snapchat, SnapchatColor) {

                }

                ShareRoundIcon(R.drawable.ic_facebook_border, R.string.facebook, FacebookColor) {

                }

                ShareRoundIcon(R.drawable.ic_twitter_x, R.string.x, Color.Black) {

                }

                ShareRoundIcon(R.drawable.ic_pinterest, R.string.pinterest, PinterestColor) {

                }
            }

            Box(
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(), Alignment.Center
            ) {
                TextViewBold(stringResource(R.string.share), size = 55)
            }
        }
    }
}

@Composable
fun ShareRoundIcon(icon: Int, text: Int, bg: Color = Color.Gray.copy(0.4f), click: () -> Unit) {
    Column(
        Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = click
            )
            .width(90.dp), Arrangement.Center, Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clip(RoundedCornerShape(100))
                .background(bg)
                .padding(15.dp), Arrangement.Center, Alignment.CenterVertically
        ) {
            ImageIcon(icon, 23)
        }
        Spacer(Modifier.height(7.dp))
        TextViewNormal(stringResource(text), 13, center = true)
    }
}