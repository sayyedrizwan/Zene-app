package com.rizwansayyed.zene.ui.earphonetracker.view

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.model.BLEDeviceData
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.IMG.HEADPHONE_TEMPS
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.addOrRemoveBLEDevice
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.getBatteryLevel
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.isBLEConnected
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.AlertDialogView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.imgBuilder

@Composable
fun EarphonesDeviceItemsView(data: BLEDeviceData, status: Boolean) {
    var isConnected by remember { mutableStateOf(false) }
    var batteryLevel by remember { mutableIntStateOf(0) }
    var deleteAlert by remember { mutableStateOf(false) }

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
                Row(
                    Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.Black)
                        .clickable {
                        }
                        .padding(8.dp)
                ) {
                    ImageIcon(R.drawable.ic_text_indent, 23)
                }

                Row(
                    Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.Black)
                        .clickable {
                        }
                        .padding(8.dp)
                ) {
                    ImageIcon(R.drawable.ic_setting, 23)
                }

                Row(
                    Modifier
                        .padding(5.dp)
                        .clip(RoundedCornerShape(100))
                        .background(Color.Black)
                        .clickable {
                            deleteAlert = true
                        }
                        .padding(8.dp)
                ) {
                    ImageIcon(R.drawable.ic_delete, 23)
                }
            }
        }

        Row(Modifier.align(Alignment.TopEnd), Arrangement.Center, Alignment.CenterVertically) {
            ImageIcon(R.drawable.ic_battery_full, size = 15)
            TextPoppins("$batteryLevel%", size = 14)
        }


        Spacer(Modifier.height(20.dp))

        if (deleteAlert) AlertDialogView(
            R.string.rm_device, R.string.rm_device_desc, R.string.remove
        ) {
            if (it) {
                data.address?.let { it1 -> addOrRemoveBLEDevice(it1, false) }
            }
            deleteAlert = false
        }

        LaunchedEffect(status) {
            isConnected = isBLEConnected(data.address ?: "")
            batteryLevel = getBatteryLevel(data.address ?: "")
        }
    }
}
