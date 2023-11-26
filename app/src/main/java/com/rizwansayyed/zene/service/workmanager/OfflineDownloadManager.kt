package com.rizwansayyed.zene.service.workmanager

import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context

class OfflineDownloadManager(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val songId = inputData.getString(Intent.EXTRA_TEXT) ?: return Result.success()


        return Result.success()
    }


    companion object {
        fun startOfflineDownloadWorkManager(id: String) {
            val builder = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)

            val data = Data.Builder().apply {
                putString(Intent.EXTRA_TEXT, id)
            }
            val uploadWorkRequest =
                OneTimeWorkRequestBuilder<OfflineDownloadManager>().setInputData(data.build())
                    .setInputData(data.build()).setConstraints(builder.build()).build()
            WorkManager.getInstance(context).enqueue(uploadWorkRequest)
        }
    }
}