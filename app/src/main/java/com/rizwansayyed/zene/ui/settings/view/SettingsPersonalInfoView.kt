package com.rizwansayyed.zene.ui.settings.view

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal

@Composable
fun SettingsPersonalInfo(userInfo: UserInfoResponse?) {
    Box(Modifier.padding(horizontal = 6.dp)) {
        TextViewBold(stringResource(R.string.personal_info), 18)
    }
    Spacer(Modifier.height(13.dp))
    PersonalInfoWith(
        stringResource(R.string.email_address), userInfo?.email, R.drawable.ic_mail
    )
    PersonalInfoWith(
        stringResource(R.string.your_name), userInfo?.name, R.drawable.ic_user
    )
    PersonalInfoWith(
        stringResource(R.string.username), userInfo?.username, R.drawable.ic_at_the_rate
    )
    PersonalInfoWith(
        stringResource(R.string.phone_number), userInfo?.phoneNumber, R.drawable.ic_telephone
    )
}


@Composable
fun PersonalInfoWith(top: String, bottom: String?, img: Int) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 5.dp, vertical = 15.dp),
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(5.dp))
        ImageIcon(img, 26)

        Spacer(Modifier.width(10.dp))
        Column(
            Modifier.weight(1f).padding(horizontal = 5.dp)
        ) {
            TextViewLight(top, 14, line = 1)
            TextViewNormal(bottom ?: "", 16, line = 1)
        }

        Row(Modifier.rotate(-90f)) {
            ImageIcon(R.drawable.ic_arrow_down, 26)
        }
    }
}