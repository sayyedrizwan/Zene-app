package com.rizwansayyed.zene.ui.earphonetracker.utils

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.earphoneDevicesDB
import com.rizwansayyed.zene.data.db.model.BLEDeviceData
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import com.rizwansayyed.zene.utils.Utils.isPermissionDisabled
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


object Utils {

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
            val bluetoothAdapter = m.adapter

            if (bluetoothAdapter != null && bluetoothAdapter.isEnabled) {
                val device = bluetoothAdapter.bondedDevices.first { it.address == macAddress }
                    ?: return false

                return isConnected(device)
            }
            return false
        } catch (e: Exception) {
            return false
        }
    }

    private fun isConnected(device: BluetoothDevice): Boolean {
        return try {
            val m = device.javaClass.getMethod("isConnected")
            m.invoke(device) as Boolean
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }
    }

    fun addOrRemoveBLEDevice(device: BluetoothDevice, add: Boolean) =
        CoroutineScope(Dispatchers.IO).launch {
            val deviceData = ArrayList<BLEDeviceData>()
            earphoneDevicesDB.firstOrNull()?.map {
                if (device.address != device.address) deviceData.add(it)
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
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
        context.resources.getString(R.string.enable_gps_to_track_devices).toast()
    }

    fun isLocationDisabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            isPermissionDisabled(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        else
            isPermissionDisabled(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    fun isBluetoothEnabled(): Boolean {
        val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter = bluetoothManager.adapter ?: return false
        return bluetoothAdapter.isEnabled
    }
}
