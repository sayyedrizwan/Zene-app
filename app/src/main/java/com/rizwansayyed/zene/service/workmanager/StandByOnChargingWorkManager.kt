package com.rizwansayyed.zene.service.workmanager

import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.ui.extra.standby.StandByModeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds


class StandByOnChargingWorkManager(
    appContext: Context, workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private var batteryManager = appContext.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            delay(1.seconds)
            if (batteryManager.isCharging) {
                Intent(applicationContext, StandByModeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    applicationContext.startActivity(this)
                }
            }

            if (batteryManager.isCharging) Result.success() else Result.failure()
        }
    }


    companion object {
        private const val WORKER_NAME = "stand_by_charging_mode"
        fun startStandbyModeWorkManager() = CoroutineScope(Dispatchers.IO).launch {
            val builder = Constraints.Builder().setRequiresCharging(true)

            val uploadWorkRequest =
                OneTimeWorkRequestBuilder<StandByOnChargingWorkManager>()
                    .addTag(WORKER_NAME).setConstraints(builder.build()).build()

            WorkManager.getInstance(context)
                .enqueueUniqueWork(WORKER_NAME, ExistingWorkPolicy.REPLACE, uploadWorkRequest)
        }
    }

}