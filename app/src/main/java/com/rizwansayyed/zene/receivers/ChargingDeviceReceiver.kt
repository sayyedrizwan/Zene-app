package com.rizwansayyed.zene.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.standByModeSettings
import com.rizwansayyed.zene.presenter.ui.extra.standby.StandByModeActivity
import com.rizwansayyed.zene.presenter.ui.extra.standby.StandByModeActivity.Companion.standByInterface
import com.rizwansayyed.zene.utils.Utils.isDeviceLocked
import com.rizwansayyed.zene.utils.Utils.printStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ChargingDeviceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_POWER_DISCONNECTED) try {
            standByInterface.close()
        } catch (e: Exception) {
            e.printStack()
        }
        else if (intent?.action == Intent.ACTION_POWER_CONNECTED) CoroutineScope(Dispatchers.IO).launch {
            if (standByModeSettings.first() && isDeviceLocked())
                Intent(context, StandByModeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    context?.startActivity(this)
                }

            if (isActive) cancel()
        }
    }

}