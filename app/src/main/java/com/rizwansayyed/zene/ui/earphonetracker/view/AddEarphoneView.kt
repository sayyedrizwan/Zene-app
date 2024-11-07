package com.rizwansayyed.zene.ui.earphonetracker.view

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.earphoneDevicesDB
import com.rizwansayyed.zene.data.db.model.BLEDeviceData
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.addOrRemoveBLEDevice
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.getConnectedBluetoothDevices
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.isBLEConnected
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.utils.Utils.isPermissionDisabledBluetooth
import com.rizwansayyed.zene.utils.Utils.startAppSettings
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
    val deviceList = remember { mutableStateListOf<BluetoothDevice>() }
    val devices by earphoneDevicesDB.collectAsState(initial = emptyArray())

    Dialog(close) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MainColor), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            val permission =
                rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
                    if (it) isBluetoothPermission = true
                    else {
                        context.resources.getString(R.string.please_grant_all_permissions).toast()
                        startAppSettings()
                    }
                }

            if (isBluetoothPermission) {
                LazyColumn(
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = 9.dp)
                ) {
                    item {
                        Spacer(Modifier.height(40.dp))
                        TextPoppinsSemiBold(stringResource(R.string.paired_devices), size = 15)
                    }
                    items(deviceList) {
                        DeviceUpdatedList(it, devices)
                    }
                }

                LaunchedEffect(Unit) {
                    deviceList.clear()
                    deviceList.addAll(getConnectedBluetoothDevices(context))
                }
            } else NeedBlePermission {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    permission.launch(Manifest.permission.BLUETOOTH_CONNECT)
                }
            }

            LaunchedEffect(Unit) {
                if (isPermissionDisabledBluetooth()) {
                    isBluetoothPermission = false
                    return@LaunchedEffect
                }
                isBluetoothPermission = true
            }
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceUpdatedList(device: BluetoothDevice, devices: Array<BLEDeviceData>?) {
    var isConnected by remember { mutableStateOf(false) }
    var isAdded by remember { mutableStateOf(false) }

    Box(
        Modifier
            .padding(2.dp)
            .padding(vertical = 9.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(Color.Black)
            .padding(9.dp)
            .padding(vertical = 9.dp)
    ) {
        Column {
            Spacer(Modifier.height(7.dp))
            TextPoppins(device.name ?: "", size = 16, limit = 1)
            Spacer(Modifier.height(7.dp))
            TextPoppins(
                stringResource(if (isConnected) R.string.connected else R.string.disconnected),
                size = 15, limit = 1
            )
            Spacer(Modifier.height(13.dp))
        }
        Row(
            Modifier
                .align(Alignment.BottomEnd)
                .offset(y = 10.dp)
                .padding(6.dp)
                .clip(RoundedCornerShape(100))
                .background(MainColor)
                .clickable {
                    isAdded = true
                    addOrRemoveBLEDevice(device.address, true)
                }
                .padding(6.dp)
        ) {
            ImageIcon(if (isAdded) R.drawable.ic_tick else R.drawable.ic_add, 23)
        }

        LaunchedEffect(Unit) {
            isConnected = isBLEConnected(device.address)
            isAdded = (devices?.indexOfFirst { it.address == device.address } ?: 0) >= 0
        }
    }
}

@Composable
fun NeedBlePermission(open: () -> Unit) {
    TextPoppins(stringResource(R.string.need_ble_permission), true, size = 15)

    Spacer(Modifier.height(20.dp))

    Row(
        Modifier
            .padding(vertical = 17.dp, horizontal = 14.dp)
            .clickable {
                open()
            }
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(Color.Black)
            .padding(vertical = 15.dp), Arrangement.Center, Alignment.CenterVertically) {
        TextPoppinsSemiBold(stringResource(R.string.grant), true, size = 14)
    }
}