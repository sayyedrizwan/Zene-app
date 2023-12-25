package com.rizwansayyed.zene.utils

import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

var latestUrl = ""

fun cropAudio(inputFile: File, outputFile: File) =
    CoroutineScope(Dispatchers.IO).launch {
        val startTimeMs = 30000L
        val endTimeMs = 60000L
        try {
            FileInputStream(inputFile).use { inputStream ->
                FileOutputStream(outputFile).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var bytesRead: Int
                    var totalBytesRead = 0

                    val startByte = (startTimeMs * inputFile.length() / inputFile.duration()).toInt()
                    val endByte = (endTimeMs * inputFile.length() / inputFile.duration()).toInt()

                    while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                        if (totalBytesRead + bytesRead > endByte) {
                            outputStream.write(buffer, 0, (endByte - totalBytesRead))
                            break
                        } else if (totalBytesRead >= startByte) {
                            outputStream.write(buffer, 0, bytesRead)
                        }

                        totalBytesRead += bytesRead
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


private fun File.duration(): Double {
    val bitRate = 128.0 // Assume a common bit rate for simplicity (128 kbps)
    return (length() * 8.0) / (bitRate * 1000.0)
}