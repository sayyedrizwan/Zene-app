package com.rizwansayyed.zene.ui.earphonetracker.utils

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.rizwansayyed.zene.data.db.DataStoreManager.earphoneDevicesDB
import com.rizwansayyed.zene.data.db.model.BLEDeviceData
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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
}
