package com.rizwansayyed.zene.ui.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.openAppOnPlayStore
import com.rizwansayyed.zene.utils.safeLaunch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await

@Composable
fun SettingsUpdateVersionView(isUpdateAvailable: Boolean) {
    if (isUpdateAvailable) Row(
        Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }) {
                openAppOnPlayStore()
            }
            .padding(horizontal = 5.dp, vertical = 20.dp),
        Arrangement.Center,
        Alignment.CenterVertically) {
        Spacer(Modifier.width(5.dp))
        ImageIcon(R.drawable.ic_reload, 24)

        Spacer(Modifier.width(10.dp))
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 1.dp)
        ) {
            TextViewNormal(stringResource(R.string.app_update_available), 16)
        }

        Row(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Red)
                .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
            TextViewSemiBold(stringResource(R.string.new_), 14)
        }
        Spacer(Modifier.width(5.dp))
        Row(Modifier.rotate(-90f)) {
            ImageIcon(R.drawable.ic_arrow_down, 26)
        }
    }
}


fun isUpdateVersionAvailable(isAvailable: () -> Unit) {
    CoroutineScope(Dispatchers.IO).safeLaunch {
        val appUpdateManager = AppUpdateManagerFactory.create(ZeneBaseApplication.context)
        try {
            val availableVersionCode = appUpdateManager.appUpdateInfo.await().availableVersionCode()
            val currentVersionCode = BuildConfig.VERSION_CODE
            if (availableVersionCode > currentVersionCode) isAvailable()
        } catch (_: Exception) {
        }
    }
}