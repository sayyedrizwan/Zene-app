package com.rizwansayyed.zene.ui.settings.dialog

import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.safeLaunch
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsStorageSheetView(close: () -> Unit) {
    ModalBottomSheet(
        close, Modifier.fillMaxWidth(), contentColor = MainColor, containerColor = MainColor
    ) {
        val context = LocalContext.current
        val storageInfo = remember { mutableStateOf<StorageInfo?>(null) }
        val coroutine = rememberCoroutineScope()

        Column(Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(30.dp))

            storageInfo.value?.let { storageInfo ->
                val total = storageInfo.totalSpaceBytes.toDouble()
                val free = storageInfo.freeSpaceBytes.toDouble()
                val used = total - free
                val appUsed =
                    storageInfo.appSizeBytes.toDouble() + storageInfo.appCacheBytes.toDouble()

                val usedPercent = (used / total) * 100
                val freePercent = (free / total) * 100
                val appPercent = (appUsed / total) * 100

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        Arrangement.Start, Alignment.CenterVertically
                    ) {
                        Spacer(
                            Modifier
                                .padding(horizontal = 7.dp)
                                .size(12.dp)
                                .clip(RoundedCornerShape(100))
                                .background(Color.Gray)
                        )
                        TextViewNormal(stringResource(R.string.free_space))
                        TextViewNormal(formatSize(storageInfo.freeSpaceBytes))
                        TextViewNormal(
                            " (${
                                String.format(Locale.getDefault(), "%.2f", freePercent)
                            }%)"
                        )
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        Arrangement.Start, Alignment.CenterVertically
                    ) {
                        Spacer(
                            Modifier
                                .padding(horizontal = 7.dp)
                                .size(12.dp)
                                .clip(RoundedCornerShape(100))
                                .background(Color.Green)
                        )
                        TextViewNormal(stringResource(R.string.used_space))
                        TextViewNormal(formatSize(used.toLong()))
                        TextViewNormal(
                            " (${
                                String.format(Locale.getDefault(), "%.2f", usedPercent)
                            }%)"
                        )
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        Arrangement.Start, Alignment.CenterVertically
                    ) {
                        Spacer(
                            Modifier
                                .padding(horizontal = 7.dp)
                                .size(12.dp)
                                .clip(RoundedCornerShape(100))
                                .background(Color.Cyan)
                        )
                        TextViewNormal(stringResource(R.string.zene_storage_used))
                        TextViewNormal(formatSize(appUsed.toLong()))
                        TextViewNormal(
                            " (${
                                String.format(Locale.getDefault(), "%.2f", appPercent)
                            }%)"
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(26.dp)
                            .clip(RoundedCornerShape(13.dp))
                            .background(Color.Gray.copy(alpha = 0.3f))
                    ) {
                        Row(Modifier.fillMaxHeight()) {
                            Box(
                                modifier = Modifier
                                    .weight(usedPercent.toFloat())
                                    .fillMaxHeight()
                                    .background(Color.Green)
                            )

                            Box(
                                modifier = Modifier
                                    .weight(freePercent.toFloat())
                                    .fillMaxHeight()
                                    .background(Color.Gray)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(appPercent.toFloat() / 100f)
                                .background(Color.Cyan)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        Arrangement.Start, Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        TextViewNormal(stringResource(R.string.total_storage))
                        Spacer(modifier = Modifier.width(5.dp))
                        TextViewNormal(formatSize(storageInfo.totalSpaceBytes))
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp),
                        Arrangement.Start, Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(10.dp))
                        TextViewNormal(stringResource(R.string.clear_app_cache))
                        Spacer(modifier = Modifier.weight(1f))
                        ButtonWithBorder(R.string.clear) {
                            coroutine.safeLaunch {
                                context.cacheDir.deleteRecursively()
                                delay(1.seconds)
                                close()
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }
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