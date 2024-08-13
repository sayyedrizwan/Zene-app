package com.rizwansayyed.zene.utils

import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import com.rizwansayyed.zene.utils.Utils.toast

enum class EarphoneType {
    BLUETOOTH, WIRED, NORMAL
}

object EarphoneTypeCheck {
    fun getAudioRoute(context: Context): EarphoneType {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        var type: EarphoneType = EarphoneType.NORMAL

        for (device in devices) {
            type = when (device.type) {
                AudioDeviceInfo.TYPE_BLUETOOTH_A2DP, AudioDeviceInfo.TYPE_BLUETOOTH_SCO, AudioDeviceInfo.TYPE_BLE_BROADCAST,
                AudioDeviceInfo.TYPE_BLE_HEADSET, AudioDeviceInfo.TYPE_BLE_SPEAKER ->
                    EarphoneType.BLUETOOTH

                AudioDeviceInfo.TYPE_WIRED_HEADPHONES, AudioDeviceInfo.TYPE_WIRED_HEADSET, AudioDeviceInfo.TYPE_AUX_LINE, AudioDeviceInfo.TYPE_BUILTIN_EARPIECE ->
                    EarphoneType.WIRED

                else -> EarphoneType.NORMAL
            }
        }

        return type
    }
}