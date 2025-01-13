package com.rizwansayyed.zene.ui.main.connect.view

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.openAppSettings
import com.rizwansayyed.zene.utils.MainUtils.toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPermissionView(close: () -> Unit) {
    val rejected = stringResource(R.string.location_permission_reject)
    val permission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it) {
                rejected.toast()
                close()
                openAppSettings()
            }
        }
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
                    permission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }) {
                    TextViewNormal(stringResource(R.string.grant), 16)
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}