package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.utils.ContactData
import com.rizwansayyed.zene.utils.MainUtils.openShareConnectShareSMS
import com.rizwansayyed.zene.service.notification.NavigationUtils
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_CONNECT_PROFILE_PAGE
import com.rizwansayyed.zene.utils.URLSUtils.connectShareURL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserSearchInfo(user: ConnectUserResponse) {

    fun showProfile() {
        NavigationUtils.triggerHomeNav("$NAV_CONNECT_PROFILE_PAGE${user.email}")
    }

    Row(
        Modifier
            .padding(horizontal = 13.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clickable { showProfile() }, verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            user.profile_photo,
            user.name,
            Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(20)),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .padding(horizontal = 7.dp)
                .weight(1f), Arrangement.Center, Alignment.Start
        ) {
            TextViewSemiBold(user.name ?: "", 16, line = 1)
            TextViewNormal("@${user.username}", 13, line = 1)
        }

        ButtonWithBorder(R.string.view) {
            showProfile()
        }
    }
}

@Composable
fun UserContactInfo(user: ContactData) {
    Row(
        Modifier
            .padding(horizontal = 13.dp, vertical = 10.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .padding(horizontal = 7.dp)
                .weight(1f), Arrangement.Center, Alignment.Start
        ) {
            TextViewSemiBold(user.name ?: "", 16, line = 1)
            TextViewNormal(user.number ?: "", 13, line = 1)
        }

        ButtonWithBorder(R.string.invite) {
            CoroutineScope(Dispatchers.IO).launch {
                val username = DataStorageManager.userInfo.firstOrNull()?.username
                openShareConnectShareSMS(connectShareURL(username!!), user.number)

                if (isActive) cancel()
            }
        }
    }
}