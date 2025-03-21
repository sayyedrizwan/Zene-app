package com.rizwansayyed.zene.ui.main.connect.connectchat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.data.model.ConnectChatMessageResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse

@Composable
fun ConnectChatItemView(
    data: ConnectChatMessageResponse, otherUser: ConnectUserResponse?, userInfo: UserInfoResponse?
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp, vertical = 11.dp)
    ) {
        if (data.from == otherUser?.email) {
            ProfileImageOfUser(otherUser?.profile_photo, otherUser?.name)
        }
        Spacer(Modifier.weight(1f))

        if (data.from == userInfo?.email) {
            ProfileImageOfUser(userInfo?.photo, userInfo?.name)
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