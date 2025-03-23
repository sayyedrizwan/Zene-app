package com.rizwansayyed.zene.ui.main.connect.connectchat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.data.model.ConnectChatMessageResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.ui.theme.BlackTransparent
import com.rizwansayyed.zene.ui.view.TextViewBold

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
        ProfileImageOfUser(otherUser?.profile_photo, otherUser?.name)
        MessageItemView(data, data.from == otherUser?.email)

        Spacer(Modifier.weight(1f))

        MessageItemView(data, data.from != otherUser?.email)
        ProfileImageOfUser(userInfo?.photo, userInfo?.name)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MessageItemView(data: ConnectChatMessageResponse, isSender: Boolean) {
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
            if (data.candid_media != null || data.candid_thumbnail != null) {
                GlideImage(data.candid_thumbnail, "", Modifier.height(180.dp))
            } else TextViewBold(data.message ?: "", 16, Color.White)
        }
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