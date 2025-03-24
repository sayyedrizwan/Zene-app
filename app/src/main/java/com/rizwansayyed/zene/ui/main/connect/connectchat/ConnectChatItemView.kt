package com.rizwansayyed.zene.ui.main.connect.connectchat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectChatMessageResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.ui.connect_status.view.ConnectVibeMediaItemAlert
import com.rizwansayyed.zene.ui.theme.BlackTransparent
import com.rizwansayyed.zene.ui.view.ImageWithBgRound
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.toast

@Composable
fun ConnectChatItemView(
    data: ConnectChatMessageResponse, otherUser: ConnectUserResponse?, userInfo: UserInfoResponse?
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 11.dp),
        Arrangement.End,
        Alignment.Bottom
    ) {
        if (data.from == otherUser?.email) {
            ProfileImageOfUser(otherUser?.profile_photo, otherUser?.name)
            MessageItemView(data, true)
        }
        Spacer(Modifier.weight(1f))

        if (data.from == userInfo?.email) {
            MessageItemView(data, false)
            ProfileImageOfUser(userInfo?.photo, userInfo?.name)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MessageItemView(data: ConnectChatMessageResponse, isSender: Boolean) {
    var showMediaDialog by remember { mutableStateOf(false) }

    val candidMeaning = stringResource(R.string.candid_desc)

    BoxWithConstraints(Modifier) {
        val maxWidth = maxWidth * 0.6f
        Box(
            modifier = Modifier
                .padding(horizontal = 7.dp)
                .background(
                    color = BlackTransparent, shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomEnd = if (isSender) 16.dp else 0.dp,
                        bottomStart = if (isSender) 0.dp else 16.dp
                    )
                )
                .padding(12.dp)
                .padding(horizontal = 12.dp)
                .widthIn(max = maxWidth)
        ) {
            if (data.getMusicData() != null) {
                ItemCardView(data.getMusicData())
            } else if (data.candid_media != null) {
                Column {
                    Box(Modifier.clickable { showMediaDialog = true }, Alignment.Center) {
                        if (data.candid_thumbnail != null)
                            GlideImage(data.candid_thumbnail, "", Modifier.height(250.dp))
                        else
                            GlideImage(data.candid_media, "", Modifier.height(250.dp))

                        if (data.candid_media.contains(".mp4"))
                            ImageWithBgRound(R.drawable.ic_play) {
                                showMediaDialog = true
                            }
                    }
                    Spacer(Modifier.width(7.dp))
                    Box(Modifier.clickable { candidMeaning.toast() }) {
                        TextViewSemiBold(stringResource(R.string.candid), 14, Color.White)
                    }
                }
            } else if (data.gif != null)
                GlideImage(data.gif, data.message, Modifier.fillMaxWidth())
            else TextViewBold(data.message ?: "", 16, Color.White)
        }
    }

    if (showMediaDialog) ConnectVibeMediaItemAlert(data.candid_media) {
        showMediaDialog = false
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfileImageOfUser(profilePhoto: String?, name: String?) {
    GlideImage(
        profilePhoto, name, Modifier
            .clip(RoundedCornerShape(100))
            .size(35.dp)
    )
}