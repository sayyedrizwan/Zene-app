package com.rizwansayyed.zene.ui.earphonetracker.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.earphoneDevicesDB
import com.rizwansayyed.zene.data.db.DataStoreManager.getEarphoneConnection
import com.rizwansayyed.zene.data.db.DataStoreManager.getEarphoneDisconnection
import com.rizwansayyed.zene.data.db.DataStoreManager.setEarphoneConnection
import com.rizwansayyed.zene.data.db.DataStoreManager.setEarphoneDisconnection
import com.rizwansayyed.zene.data.db.model.BLEDeviceData
import com.rizwansayyed.zene.data.roomdb.model.UpdateData
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.IMG.HEADPHONE_TEMPS
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.addOrRemoveBLEDevice
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.getBatteryLevel
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.isBLEConnected
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.openLocationOnMaps
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.AlertDialogView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.SheetDialogSheet
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.ZeneViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@Composable
fun EarphonesDeviceItemsView(data: BLEDeviceData, status: Boolean) {
    val viewModel : ZeneViewModel = hiltViewModel()
    var isConnected by remember { mutableStateOf(false) }
    var batteryLevel by remember { mutableIntStateOf(0) }
    var deleteAlert by remember { mutableStateOf(false) }
    var settingSheet by remember { mutableStateOf(false) }
    var earphoneDialog by remember { mutableStateOf(false) }

    Box(
        Modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MainColor)
            .padding(14.dp)
    ) {
        Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
            Spacer(Modifier.height(20.dp))
            AsyncImage(
                imgBuilder(if ((data.img ?: "").length > 2) data.img else HEADPHONE_TEMPS),
                data.name,
                Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(15.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(Modifier.height(15.dp))
            TextPoppins(data.name ?: "", true, size = 16, limit = 2)
            Spacer(Modifier.height(5.dp))
            TextPoppins(
                stringResource(if (isConnected) R.string.connected else R.string.disconnected),
                size = 15,
                limit = 1,
                color = if (isConnected) Color.Green else Color.Red
            )
            Spacer(Modifier.height(5.dp))

            Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                Row(Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black)
                    .clickable { earphoneDialog = true }
                    .padding(8.dp)) {
                    ImageIcon(R.drawable.ic_text_indent, 23)
                }

                Row(Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black)
                    .clickable { settingSheet = true }
                    .padding(8.dp)) {
                    ImageIcon(R.drawable.ic_setting, 23)
                }

                Row(Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(100))
                    .background(Color.Black)
                    .clickable { deleteAlert = true }
                    .padding(8.dp)) {
                    ImageIcon(R.drawable.ic_delete, 23)
                }
            }
        }

        if (isConnected) Row(
            Modifier.align(Alignment.TopEnd), Arrangement.Center, Alignment.CenterVertically
        ) {
            ImageIcon(R.drawable.ic_battery_full, size = 15)
            TextPoppins("$batteryLevel%", size = 14)
        }

        Spacer(Modifier.height(20.dp))

        if (deleteAlert) AlertDialogView(
            R.string.rm_device, R.string.rm_device_desc, R.string.remove
        ) {
            if (it) {
                data.address?.let { address ->
                    addOrRemoveBLEDevice(address, false)
                    viewModel.removeAllEarphones(address)
                }
            }
            deleteAlert = false
        }

        if (settingSheet) SettingsEarphoneView(data) {
            settingSheet = false
        }
        if (earphoneDialog) EarphoneUpdatesView(data) {
            earphoneDialog = false
        }

        LaunchedEffect(status) {
            isConnected = isBLEConnected(data.address ?: "")
            batteryLevel = getBatteryLevel(data.address ?: "")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsEarphoneView(data: BLEDeviceData, close: () -> Unit) {
    var connectionAlert by remember { mutableStateOf(true) }
    var disconnectionAlert by remember { mutableStateOf(true) }

    val coroutine = rememberCoroutineScope()

    ModalBottomSheet(close, containerColor = MainColor, contentColor = MainColor) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .padding(horizontal = 12.dp)
        ) {
            Spacer(Modifier.height(20.dp))
            TextPoppinsSemiBold(stringResource(R.string.settings), size = 18)
            Spacer(Modifier.height(30.dp))


            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextPoppins(stringResource(R.string.connection_alert), size = 16)
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = connectionAlert, onCheckedChange = {
                        connectionAlert = it
                        coroutine.launch {
                            setEarphoneConnection(data.address ?: "", it)
                        }
                    }, colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Gray,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.Gray,
                    )
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextPoppins(stringResource(R.string.disconnection_alert), size = 16)
                Spacer(Modifier.weight(1f))
                Switch(
                    checked = disconnectionAlert, onCheckedChange = {
                        disconnectionAlert = it
                        coroutine.launch {
                            setEarphoneDisconnection(data.address ?: "", it)
                        }
                    }, colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Gray,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.Gray,
                    )
                )
            }

            Spacer(Modifier.height(60.dp))
        }

        LaunchedEffect(Unit) {
            connectionAlert = getEarphoneConnection(data.address ?: "")
            disconnectionAlert = getEarphoneDisconnection(data.address ?: "")
        }
    }
}

@Composable
fun EarphoneUpdatesView(device: BLEDeviceData, close: () -> Unit) {
    val viewModel: ZeneViewModel = hiltViewModel()
    var devices: BLEDeviceData? by remember { mutableStateOf(null) }
    var page by remember { mutableIntStateOf(0) }

    Dialog(close, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
            ) {
                item {
                    Spacer(Modifier.height(30.dp))
                    TextPoppinsSemiBold(stringResource(R.string.updates), size = 28)
                    Spacer(Modifier.height(1.dp))
                    TextPoppins(devices?.name ?: "", size = 13)
                    Spacer(Modifier.height(20.dp))

                    if (viewModel.updateLists.size <= 0 && !viewModel.songHistoryIsLoading) {
                        Spacer(Modifier.height(50.dp))
                        TextPoppins(
                            stringResource(R.string.no_updates_found), true, Color.White, 16
                        )
                    }
                }

                itemsIndexed(viewModel.updateLists) { i, u ->
                    UpdatesItems(u) {
                        viewModel.removeUpdatesLists(i, u)
                    }
                }

                item {
                    Spacer(Modifier.height(30.dp))
                    Row(
                        Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically
                    ) {
                        if (viewModel.songHistoryIsLoading) {
                            LoadingView(Modifier.size(32.dp))
                        }

                        if (!viewModel.songHistoryIsLoading && viewModel.doShowMoreLoading) {
                            Box(
                                Modifier
                                    .padding(vertical = 15.dp, horizontal = 6.dp)
                                    .clip(RoundedCornerShape(100))
                                    .background(Color.Black)
                                    .clickable {
                                        page += 1
                                        device.address?.let { viewModel.updateLists(page, it) }
                                    }
                                    .border(1.dp, Color.White, RoundedCornerShape(100))
                                    .padding(vertical = 9.dp, horizontal = 18.dp)) {
                                TextPoppins(stringResource(R.string.load_more), size = 15)
                            }
                        }
                    }


                    Spacer(Modifier.height(90.dp))
                }
            }
        }

        LaunchedEffect(Unit) {
            device.address?.let { viewModel.updateLists(page, it) }
            devices = earphoneDevicesDB.firstOrNull()?.firstOrNull { it.address == device.address }
        }
    }
}

@Composable
fun UpdatesItems(update: UpdateData, remove: () -> Unit) {
    var dialog by remember { mutableStateOf(false) }
    Column(Modifier
        .padding(vertical = 15.dp, horizontal = 4.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .background(MainColor)
        .clickable { dialog = true }
        .padding(10.dp)) {
        Spacer(Modifier.height(8.dp))
        TextPoppins(update.types(), color = update.typesColor(), size = 22)
        if ((update.address?.length ?: 0) > 2) {
            Spacer(Modifier.height(7.dp))
            TextPoppins("${update.address}", size = 13)
        } else if (update.latitude == (-1).toDouble() && update.longitude == (-1).toDouble()) {
            Spacer(Modifier.height(7.dp))
            TextPoppins(stringResource(R.string.location_permission_app_disabled), size = 13)
        } else if (update.latitude == (-2).toDouble() && update.longitude == (-2).toDouble()) {
            Spacer(Modifier.height(7.dp))
            TextPoppins(stringResource(R.string.phone_gps_was_off), size = 13)
        }
        Spacer(Modifier.height(7.dp))
        TextPoppins(update.time(), size = 13)
        Spacer(Modifier.height(8.dp))
    }

    if (dialog) EarphonesUpdatedDialog(update) {
        if (it) remove()
        dialog = false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarphonesUpdatedDialog(update: UpdateData, close: (Boolean) -> Unit) {
    val context = LocalContext.current
    ModalBottomSheet(
        { close(false) },
        Modifier.fillMaxHeight(),
        containerColor = MainColor,
        contentColor = MainColor
    ) {
        Column(Modifier.fillMaxSize()) {
            Spacer(Modifier.height(40.dp))

            SheetDialogSheet(R.drawable.ic_gps, R.string.open_location) {
                if (update.latitude == 0.0 && update.longitude == 0.0)
                    context.resources.getString(R.string.no_location_found).toast()
                else if (update.latitude == -1.0 && update.longitude == -1.0)
                    context.resources.getString(R.string.location_permission_app_disabled).toast()
                else if (update.latitude == -2.0 && update.longitude == -2.0)
                    context.resources.getString(R.string.phone_gps_was_off).toast()
                else
                    openLocationOnMaps(update.latitude ?: 0.0, update.longitude ?: 0.0)

                close(false)
            }

            Spacer(Modifier.height(20.dp))

            SheetDialogSheet(R.drawable.ic_delete, R.string.delete) {
                close(true)
            }


            Spacer(Modifier.height(50.dp))
        }
    }
}