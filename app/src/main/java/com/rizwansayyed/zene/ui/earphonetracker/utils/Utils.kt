package com.rizwansayyed.zene.ui.earphonetracker.utils

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.earphoneDevicesDB
import com.rizwansayyed.zene.data.db.model.BLEDeviceData
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.ui.earphonetracker.EarphoneTrackerActivity
import com.rizwansayyed.zene.utils.Utils.isPermissionDisabled
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


object Utils {

    object IMG {
        const val HEADPHONE_TEMPS = "https://www.zenemusic.co/headphone_temp.png"
    }

    object INFO {
        const val PLAYER_PLAYER = "PLAYER"
        const val NEW_CONNECTED_EARPHONE = "NEW_CONNECTED_EARPHONE"
    }

    @SuppressLint("MissingPermission")
    fun getConnectedBluetoothDevices(context: Context): List<BluetoothDevice> {
        try {
            val bManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter = bManager.adapter ?: return emptyList()
            val pairedDevices = bluetoothAdapter.bondedDevices
            return pairedDevices?.toList() ?: emptyList()
        } catch (e: Exception) {
            return emptyList()
        }
    }

    @SuppressLint("MissingPermission")
    fun isBLEConnected(macAddress: String): Boolean {
        try {
            val m = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val device = m.adapter.getRemoteDevice(macAddress)
            return isConnected(device)
        } catch (e: Exception) {
            return false
        }
    }

    fun getBatteryLevel(address: String): Int {
        val m = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val device = m.adapter.getRemoteDevice(address)

        return device?.let { bluetoothDevice ->
            (bluetoothDevice.javaClass.getMethod("getBatteryLevel")).invoke(device) as Int
        } ?: 0
    }

    private fun isConnected(device: BluetoothDevice): Boolean {
        return try {
            val m = device.javaClass.getMethod("isConnected")
            m.invoke(device) as Boolean
        } catch (e: Exception) {
            return false
        }
    }

    fun addOrRemoveBLEDevice(address: String, add: Boolean) =
        CoroutineScope(Dispatchers.IO).launch {
            val m = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
            val device = m.adapter.getRemoteDevice(address)

            val deviceData = ArrayList<BLEDeviceData>()
            earphoneDevicesDB.firstOrNull()?.map {
                if (device?.address != device?.address) deviceData.add(it)
            }
            if (add) deviceData.add(0, BLEDeviceData(device.name, device.address, ""))
            earphoneDevicesDB = flowOf(deviceData.toTypedArray())
        }

    fun isGPSEnabled(): Boolean {
        try {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            return false
        }
    }

    fun openGPSActivity() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.flags = FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        context.resources.getString(R.string.enable_gps_to_track_devices).toast()
    }

    fun isLocationDisabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) isPermissionDisabled(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        else isPermissionDisabled(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun isBluetoothEnabled(): Boolean {
        val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter = bluetoothManager.adapter ?: return false
        return bluetoothAdapter.isEnabled
    }

    fun openEarphoneTrackerActivity() {
        Intent(context, EarphoneTrackerActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
    }

    fun openLocationOnMaps(lat: Double, lon: Double) {
        Intent(ACTION_VIEW).apply {
            data = "https://www.google.com/maps/search/?api=1&query=$lat%2C$lon".toUri()
            flags = FLAG_ACTIVITY_NEW_TASK
            context.startActivity(this)
        }
    }
}
