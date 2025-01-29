package com.rizwansayyed.zene.ui.main.connect.connectview

import android.Manifest
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.ui.main.connect.profile.ConnectUserProfileActivity
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.openAppSettings
import com.rizwansayyed.zene.utils.MainUtils.toast

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ConnectFriendsLists(user: ConnectUserInfoResponse) {
    val context = LocalContext.current.applicationContext
    Box(
        Modifier
            .padding(horizontal = 9.dp)
            .clickable {
                Intent(context, ConnectUserProfileActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    putExtra(Intent.ACTION_MAIN, user.user?.email)
                    context.startActivity(this)
                }
            }) {
        Column(Modifier.width(100.dp), Arrangement.Center, Alignment.CenterHorizontally) {
            GlideImage(
                user.user?.profile_photo, user.user?.name,
                Modifier
                    .clip(RoundedCornerShape(100))
                    .size(100.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(5.dp))
            TextViewNormal(user.user?.name ?: "", 15, line = 1)
            Spacer(Modifier.height(2.dp))
            TextViewNormal(user.user?.connect_status ?: "", 15, line = 1)
            Spacer(Modifier.height(10.dp))
        }

        if ((user.message?.message?.length ?: 0) > 3 && user.message?.fromCurrentUser == false)
            Spacer(
                Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 9.dp, y = 9.dp)
                    .size(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.Red)
            )

        if (user.songDetails?.thumbnail != null)
            GlideImage(
                user.songDetails.thumbnail, user.songDetails.name,
                Modifier
                    .align(Alignment.TopEnd)
                    .clip(RoundedCornerShape(10))
                    .size(30.dp),
                contentScale = ContentScale.Crop
            )

    }
}

@Composable
fun ConnectRecentContactsView() {
    val needContact = stringResource(R.string.need_location_permission_to_read_contact)
    var contactsView by remember { mutableStateOf(false) }
    var editUserView by remember { mutableStateOf(false) }
    val contactPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) contactsView = true
            else {
                needContact.toast()
                openAppSettings()
            }
        }

    Spacer(Modifier.height(52.dp))
    Row(Modifier.padding(horizontal = 9.dp)) {
        TextViewBold(stringResource(R.string.friends), 18)
        Spacer(Modifier.weight(1f))
        Box(Modifier.clickable { editUserView = true }) {
            ImageIcon(R.drawable.ic_user_edit, 23)
        }
        Spacer(Modifier.width(14.dp))
        Box(Modifier.clickable { contactPermission.launch(Manifest.permission.READ_CONTACTS) }) {
            ImageIcon(R.drawable.ic_user_search, 23)
        }
        Spacer(Modifier.width(14.dp))
        Box(Modifier.clickable { editUserView = true }) {
            ImageIcon(R.drawable.ic_bookmark, 23)
        }
    }
    Spacer(Modifier.height(12.dp))

    if (contactsView) Dialog(
        { contactsView = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ContactListsInfo()
    }
    if (editUserView) Dialog(
        { editUserView = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ConnectEditProfileView()
    }
}