package com.rizwansayyed.zene.ui.settings.view

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.ui.phoneverification.PhoneVerificationActivity
import com.rizwansayyed.zene.ui.settings.dialog.EditProfileNameDialog
import com.rizwansayyed.zene.ui.settings.dialog.EditProfileUsernameDialog
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun SettingsPersonalInfo(userInfo: UserInfoResponse?) {
    val viewModel: HomeViewModel = hiltViewModel()
    val context = LocalContext.current
    var nameUpdateView by remember { mutableStateOf(false) }
    var usernameNameUpdateView by remember { mutableStateOf(false) }

    Box(Modifier.padding(horizontal = 6.dp)) {
        TextViewBold(stringResource(R.string.personal_info), 18)
    }
    Spacer(Modifier.height(13.dp))
    PersonalInfoWith(
        stringResource(R.string.email_address), userInfo?.email, R.drawable.ic_mail, null
    )

    PersonalInfoWith(
        stringResource(R.string.your_name), userInfo?.name, R.drawable.ic_user
    ) { nameUpdateView = true }

    PersonalInfoWith(
        stringResource(R.string.username), userInfo?.username, R.drawable.ic_at_the_rate
    ) { usernameNameUpdateView = true }

//    PersonalInfoWith(
//        stringResource(R.string.phone_number), userInfo?.phoneNumber, R.drawable.ic_telephone
//    ) {
//        Intent(context, PhoneVerificationActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            context.startActivity(this)
//        }
//    }

    if (nameUpdateView) EditProfileNameDialog(viewModel) {
        nameUpdateView = false
        if (it) viewModel.userInfo()
    }

    if (usernameNameUpdateView) EditProfileUsernameDialog(viewModel) {
        usernameNameUpdateView = false
        if (it) viewModel.userInfo()
    }
}

@Composable
fun PersonalInfoWith(top: String, bottom: String?, img: Int, click: (() -> Unit)?) {
    Row(Modifier
        .fillMaxWidth()
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) { if (click != null) click() }
        .padding(horizontal = 5.dp, vertical = 15.dp),
        Arrangement.Center, Alignment.CenterVertically) {
        Spacer(Modifier.width(5.dp))
        ImageIcon(img, 26)

        Spacer(Modifier.width(10.dp))
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 5.dp)
        ) {
            TextViewLight(top, 14, line = 1)
            if ((bottom?.trim()?.length ?: 0) > 6) TextViewNormal(bottom ?: "", 16, line = 1)
            else TextViewNormal("-", 16, line = 1)
        }

        if (click != null) Row(Modifier.rotate(-90f)) {
            ImageIcon(R.drawable.ic_arrow_down, 26)
        }
    }
}