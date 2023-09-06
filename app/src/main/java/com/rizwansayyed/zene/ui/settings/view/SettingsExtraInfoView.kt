package com.rizwansayyed.zene.ui.settings.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.utils.QuickSandRegular


@Composable
fun SettingsExtraInfo() {

    val context = LocalContext.current.applicationContext

    QuickSandRegular(
        stringResource(id = R.string.privacy_policy),
        size = 14,
        modifier = Modifier
            .padding(15.dp)
            .clickable {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://thewallpo.com/policyzene")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(this)
                }
            },
        align = TextAlign.Start
    )
    QuickSandRegular(
        "${stringResource(id = R.string.version)}: ${BuildConfig.VERSION_NAME}",
        size = 14,
        modifier = Modifier.padding(15.dp),
        align = TextAlign.Start
    )
}