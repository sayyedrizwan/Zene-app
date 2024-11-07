package com.rizwansayyed.zene.ui.earphonetracker.view

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.openGPSActivity
import com.rizwansayyed.zene.ui.home.view.TextSize
import com.rizwansayyed.zene.ui.home.view.TextTitleHeader
import com.rizwansayyed.zene.ui.view.AlertDialogView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.utils.Utils.startAppSettings
import com.rizwansayyed.zene.utils.Utils.toast

@Composable
fun TopEarphoneHeaderView() {
    Spacer(Modifier.height(60.dp))
    Column(Modifier.padding(start = 5.dp)) {
        TextPoppins(stringResource(R.string.earphones_tracker_finder_), size = 40, lineHeight = 43)
        TextPoppins(stringResource(R.string.earphones_tracker_finder_desc), size = 14)
    }
}

@Composable
fun NoHeadphoneAddedView() {
    Spacer(Modifier.height(100.dp))
    TextPoppins(stringResource(R.string.no_devices_added), true, size = 16)
    Spacer(Modifier.height(100.dp))
}

@Composable
fun HeaderPermissionsView() {
    var isGPSEnabled by remember { mutableStateOf(true) }
    var isLocationAlert by remember { mutableStateOf(false) }
    var isLocationEnabled by remember { mutableStateOf(true) }
    var isBluetoothEnabled by remember { mutableStateOf(true) }

    val lifecycleOwner = LocalLifecycleOwner.current

    fun isPermissionEnabled() {
        isGPSEnabled = Utils.isGPSEnabled()
        isLocationEnabled = !Utils.isLocationDisabled()
        isBluetoothEnabled = Utils.isBluetoothEnabled()
    }


    val listener =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            isPermissionEnabled()
        }

    Spacer(Modifier.height(30.dp))
    Row(Modifier.fillMaxSize(), Arrangement.End, Alignment.CenterVertically) {
        if (!isGPSEnabled) Row(Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(100))
            .background(Color.Red)
            .clickable {
                openGPSActivity()
            }
            .padding(5.dp)) {
            ImageIcon(R.drawable.ic_gps, size = 24)
        }

        if (!isLocationEnabled) Row(Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(100))
            .background(Color.Red)
            .clickable {
                isLocationAlert = true
            }
            .padding(5.dp)) {
            ImageIcon(R.drawable.ic_location, size = 24)
        }

        if (!isBluetoothEnabled) Row(Modifier
            .padding(5.dp)
            .clip(RoundedCornerShape(100))
            .background(Color.Red)
            .clickable {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                listener.launch(enableBtIntent)
            }
            .padding(5.dp)) {
            ImageIcon(R.drawable.ic_bluetooth, size = 24)
        }
    }
    Spacer(Modifier.height(30.dp))

    if (isLocationAlert) AlertDialogView(
        R.string.need_location_permission, R.string.need_location_permission_desc, R.string.grant
    ) {
        if (it) {
            startAppSettings()
        }
        isLocationAlert = false
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) isPermissionEnabled()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}