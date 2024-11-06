package com.rizwansayyed.zene.ui.earphonetracker.view

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.ActivityCompat
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.getConnectedBluetoothDevices
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.utils.Utils.isPermissionDisabled
import com.rizwansayyed.zene.utils.Utils.isPermissionDisabledBluetooth
import com.rizwansayyed.zene.utils.Utils.toast

@Composable
fun AddEarphoneView(modifier: Modifier = Modifier) {
    var addEarphoneAlert by remember { mutableStateOf(false) }

    Row(modifier
        .padding(bottom = 70.dp)
        .padding(10.dp)
        .clip(RoundedCornerShape(100))
        .background(MainColor)
        .clickable { addEarphoneAlert = true }
        .padding(15.dp)) {
        ImageIcon(R.drawable.ic_headphones, 34)
    }

    if (addEarphoneAlert) AddEarphoneAlertView {
        addEarphoneAlert = false
    }
}


@Composable
fun AddEarphoneAlertView(close: () -> Unit) {
    val context = LocalContext.current.applicationContext
    var isBluetoothPermission by remember { mutableStateOf(false) }

    Dialog(close) {
        Column {
            val permission =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {

                }



            LaunchedEffect(Unit) {
                if (isPermissionDisabledBluetooth()) {
                    isBluetoothPermission = true
                    return@LaunchedEffect
                }
                isBluetoothPermission = false
                val list = getConnectedBluetoothDevices(context)
                list.size.toast()
            }
        }
    }
}