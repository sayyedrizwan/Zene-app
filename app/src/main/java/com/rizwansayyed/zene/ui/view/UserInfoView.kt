package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.utils.ContactData
import com.rizwansayyed.zene.utils.MainUtils.openShareConnectShareSMS
import com.rizwansayyed.zene.utils.URLSUtils.connectShareURL
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UserSearchInfo(user: ConnectUserResponse) {
    var showUserInfo by remember { mutableStateOf(false) }

    Row(
        Modifier
            .padding(horizontal = 13.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clickable { },
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            user.profile_photo, user.name,
            Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(20))
        )

        Column(
            Modifier
                .padding(horizontal = 7.dp)
                .weight(1f), Arrangement.Center, Alignment.Start
        ) {
            TextViewSemiBold(user.name ?: "", 16, line = 1)
            TextViewNormal(user.username ?: "", 13, line = 1)
        }

        ButtonWithBorder(R.string.view) {
            showUserInfo = true
        }
    }

    if (showUserInfo) ModalBottomSheet(
        { showUserInfo = false }, contentColor = MainColor, containerColor = MainColor
    ) {
        UserInfoDialog(user)
    }
}

@Composable
fun UserInfoDialog(user: ConnectUserResponse) {
    val connectViewModel: ConnectViewModel = hiltViewModel()

    Column(Modifier.fillMaxSize()) {

    }

    LaunchedEffect(Unit) {
        user.email?.let { connectViewModel.connectUserInfo(it) }
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