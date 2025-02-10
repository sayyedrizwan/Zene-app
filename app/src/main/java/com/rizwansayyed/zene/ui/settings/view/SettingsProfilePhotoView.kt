package com.rizwansayyed.zene.ui.settings.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.ui.view.TextViewNormal

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SettingsProfilePhotoView(userInfo: UserInfoResponse?) {
    Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
        Spacer(Modifier.height(95.dp))
        GlideImage(
            userInfo?.photo,
            userInfo?.name,
            Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(14.dp))
        )
        Spacer(Modifier.height(15.dp))
        Box(Modifier.clickable { }) {
            TextViewNormal(stringResource(R.string.update_profile_photo), 14, Color.Blue)
        }
        Spacer(Modifier.height(75.dp))
    }
}