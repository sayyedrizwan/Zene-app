package com.rizwansayyed.zene.service.bluetoothlistener

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.DataStoreManagerObjects.TS_LAST_DATA
import com.rizwansayyed.zene.data.db.DataStoreManager.earphoneDevicesDB
import com.rizwansayyed.zene.data.db.DataStoreManager.getCustomTimestamp
import com.rizwansayyed.zene.data.db.DataStoreManager.getEarphoneConnection
import com.rizwansayyed.zene.data.db.DataStoreManager.getEarphoneDisconnection
import com.rizwansayyed.zene.data.db.DataStoreManager.setCustomTimestamp
import com.rizwansayyed.zene.data.db.model.BLEDeviceData
import com.rizwansayyed.zene.data.roomdb.implementation.UpdatesRoomDBImpl
import com.rizwansayyed.zene.data.roomdb.model.UPDATES_TYPE_CONNECT
import com.rizwansayyed.zene.data.roomdb.model.UPDATES_TYPE_DISCONNECT
import com.rizwansayyed.zene.data.roomdb.model.UpdateData
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.ui.earphonetracker.utils.Utils.INFO.NEW_CONNECTED_EARPHONE
import com.rizwansayyed.zene.utils.NotificationUtils
import com.rizwansayyed.zene.utils.Utils.timeDifferenceInMinutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.hours


class BluetoothListeners(private val roomDB: UpdatesRoomDBImpl) {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, i: Intent?) {
            c ?: return
            i ?: return

            val d: BluetoothDevice? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                i.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
            } else {
                i.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) as BluetoothDevice?
            }

            CoroutineScope(Dispatchers.IO).launch {
                if (i.action == BluetoothDevice.ACTION_ACL_CONNECTED) {
                    val device =
                        earphoneDevicesDB.firstOrNull()?.firstOrNull { it.address == d?.address }

                    if (device == null) {
                        checkIfANewEarphoneIsConnected(d)
                        return@launch
                    }

                    if (getEarphoneConnection(device.address ?: "")) connectedDevice(device)

                } else if (i.action == BluetoothDevice.ACTION_ACL_DISCONNECTED) {
                    val device =
                        earphoneDevicesDB.firstOrNull()?.firstOrNull { it.address == d?.address }
                            ?: return@launch

                    if (getEarphoneDisconnection(device.address ?: "")) disconnectedDevice(device)
                }
            }
        }
    }

    suspend fun connectedDevice(d: BLEDeviceData) {
        val body = context.getString(R.string.set_to_listen_enjoy_audio)
        val connected = context.getString(R.string.connected)
        NotificationUtils("${d.name} $connected!!", body, null)

        if (d.address == null) return
        roomDB.insertDB(d.address, UPDATES_TYPE_CONNECT).collect()
    }

    suspend fun disconnectedDevice(d: BLEDeviceData) {
        val body = context.getString(R.string.audio_will_play_now_on_speakers)
        val disconnected = context.getString(R.string.disconnected)
        NotificationUtils("${d.name} $disconnected!!", body, null)

        if (d.address == null) return
        roomDB.insertDB(d.address, UPDATES_TYPE_DISCONNECT).collect()
    }

    suspend fun checkIfANewEarphoneIsConnected(d: BluetoothDevice?) {
        if (d?.address == null) return

        val lastTS = getCustomTimestamp("${d.address}_new_earphone") ?: TS_LAST_DATA
        if (timeDifferenceInMinutes(lastTS) > 144.hours.inWholeMinutes) {
            val title = context.getString(R.string.new_earphones_detected)
            val body = context.getString(R.string.want_to_keep_track_of_earphones)
            NotificationUtils(title, body, null, NEW_CONNECTED_EARPHONE)
            setCustomTimestamp("${d.address}_new_earphone")
        }
    }

    fun start(context: Context) {
        val filter = IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        }
        context.registerReceiver(receiver, filter)
    }
}