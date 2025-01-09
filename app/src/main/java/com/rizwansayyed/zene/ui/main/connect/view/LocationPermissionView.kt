package com.rizwansayyed.zene.ui.main.connect.view

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPermissionView(close: () -> Unit) {
    val context = LocalContext.current.applicationContext
    ModalBottomSheet(close, contentColor = MainColor, containerColor = MainColor) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            TextViewSemiBold(stringResource(R.string.location_permission), 18)
            Spacer(Modifier.height(10.dp))
            TextViewNormal(stringResource(R.string.location_permission_desc), 16)
            Spacer(Modifier.height(10.dp))

            Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
                FilledTonalButton(onClick = {
                    close()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse("package:" + context.packageName)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)
                }) {
                    TextViewNormal(stringResource(R.string.grant), 16)
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}