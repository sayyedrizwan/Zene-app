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
}