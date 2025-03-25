package com.rizwansayyed.zene.utils

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val downloadViewMap = HashMap<String, DownloadInformation>()

class DownloadInformation(private val url: String, private val name: String) {
    private val downloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    var downloadProgress by mutableIntStateOf(0)
        private set
    var isDownloadComplete by mutableStateOf(false)
        private set

    private var downloadID: Long? = null

    init {
        startDownload()
    }

    private fun startDownload() {
        if (downloadID != null && !isDownloadComplete) return
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("${name} ${context}")
            .setDescription(context.resources.getString(R.string.downloading_file_please_wait))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name)

        downloadID = downloadManager.enqueue(request)
        monitorDownloadProgress()
    }

    fun openDownloadFolder() {
        val intent = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun monitorDownloadProgress() {
        downloadID?.let { id ->
            CoroutineScope(Dispatchers.IO).launch {
                var isDownloading = true
                while (isDownloading) {
                    val query = DownloadManager.Query().setFilterById(id)
                    val cursor = downloadManager.query(query)

                    if (cursor.moveToFirst()) {
                        val bytesDownloaded =
                            cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                        val totalBytes =
                            cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                        if (totalBytes > 0) {
                            downloadProgress = ((bytesDownloaded * 100) / totalBytes)
                        }

                        val status =
                            cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                        if (status == DownloadManager.STATUS_SUCCESSFUL) {
                            isDownloading = false
                            isDownloadComplete = true
                        } else if (status == DownloadManager.STATUS_FAILED) {
                            isDownloading = false
                        }
                    }
                    cursor.close()
                    delay(500)
                }
            }
        }
    }
}
