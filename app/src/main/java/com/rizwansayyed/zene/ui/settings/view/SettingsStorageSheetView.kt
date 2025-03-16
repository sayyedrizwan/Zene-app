package com.rizwansayyed.zene.ui.settings.view

import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewNormal
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsStorageSheetView(close: () -> Unit) {
    ModalBottomSheet(
        close, Modifier.fillMaxWidth(), contentColor = MainColor, containerColor = MainColor
    ) {
        val context = LocalContext.current
        var storageInfo = remember { mutableStateOf<StorageInfo?>(null) }

        Column(Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(30.dp))

            storageInfo.value?.let { info ->
                val total = info.totalSpaceBytes.toDouble()
                val used = total - info.freeSpaceBytes
                val usedPercent = (used / total) * 100

                Column(modifier = Modifier.padding(16.dp)) {
                    TextViewNormal("Total: ${formatSize(info.totalSpaceBytes)}")
                    TextViewNormal("Free: ${formatSize(info.freeSpaceBytes)}")
                    TextViewNormal(
                        "Used: ${formatSize((total - info.freeSpaceBytes).toLong())} (${
                            String.format(
                                "%.2f",
                                usedPercent
                            )
                        }%)"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextViewNormal("App Size: ${formatSize(info.appSizeBytes)}")
                    TextViewNormal("App Data: ${formatSize(info.appDataBytes)}")
                    TextViewNormal("App Cache: ${formatSize(info.appCacheBytes)}")

                    // Example: Cache % Usage
                    val cachePercent = (info.appCacheBytes.toDouble() / total) * 100
                    TextViewNormal("App Cache Usage: ${String.format("%.2f", cachePercent)}%")
                }
            }

            Spacer(Modifier.height(70.dp))
        }

        LaunchedEffect(Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                storageInfo.value = getStorageInfo(context)
            }
        }
    }
}

data class StorageInfo(
    val totalSpaceBytes: Long,
    val freeSpaceBytes: Long,
    val appSizeBytes: Long,
    val appDataBytes: Long,
    val appCacheBytes: Long
)

@RequiresApi(Build.VERSION_CODES.O)
fun getStorageInfo(context: Context): StorageInfo {
    val stat = StatFs(Environment.getDataDirectory().path)
    val blockSize = stat.blockSizeLong
    val totalBlocks = stat.blockCountLong
    val availableBlocks = stat.availableBlocksLong

    val totalSpace = totalBlocks * blockSize
    val freeSpace = availableBlocks * blockSize

    var appBytes = 0L
    var dataBytes = 0L
    var cacheBytes = 0L

    try {
        val storageStatsManager =
            context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
        val packageManager = context.packageManager
        val uuid = StorageManager.UUID_DEFAULT
        val packageName = context.packageName
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        val storageStats = storageStatsManager.queryStatsForUid(uuid, appInfo.uid)

        appBytes = storageStats.appBytes
        dataBytes = storageStats.dataBytes
        cacheBytes = storageStats.cacheBytes
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return StorageInfo(
        totalSpaceBytes = totalSpace,
        freeSpaceBytes = freeSpace,
        appSizeBytes = appBytes,
        appDataBytes = dataBytes,
        appCacheBytes = cacheBytes
    )
}

fun formatSize(size: Long): String {
    val mb = size.toDouble() / (1024 * 1024)
    val gb = size.toDouble() / (1024 * 1024 * 1024)
    return if (gb >= 1) {
        String.format(Locale.getDefault(), "%.2f GB", gb)
    } else {
        String.format(Locale.getDefault(), "%.2f MB", mb)
    }
}