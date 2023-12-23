package com.rizwansayyed.zene.utils


import android.util.Log
import com.rizwansayyed.zene.data.utils.CacheFiles.demoRingtonePath
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile

class FileDownloaderInChunks(
    private val url: String, private val done: (progress: Int?, status: Boolean?) -> Unit
) {

    private val client = OkHttpClient()

    fun startDownloadingRingtone() = CoroutineScope(Dispatchers.IO).launch {
        demoRingtonePath.deleteRecursively()
        downloadFile(url, demoRingtonePath)
    }

    private suspend fun downloadFile(url: String, destinationPath: File, numChunks: Int = 15) =
        runBlocking(Dispatchers.IO) {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                done(null, false)
                return@runBlocking
            }

            val totalFileSize = response.body?.contentLength() ?: 0
            val chunkSize = totalFileSize / numChunks

            val tasks = mutableListOf<Deferred<Unit>>()

            try {
                for (i in 0 until numChunks) {
                    val startByte = i * chunkSize
                    val endByte =
                        if (i == numChunks - 1) totalFileSize - 1 else startByte + chunkSize - 1

                    updateProgressBar(destinationPath, totalFileSize)

                    tasks.add(async(Dispatchers.IO) {
                        downloadChunk(url, startByte, endByte, destinationPath, totalFileSize)
                    })
                }

                runBlocking {
                    tasks.awaitAll()
                }

            } finally {
                response.body?.close()
            }
        }

    private fun updateProgressBar(destinationPath: File, totalFileSize: Long) {
        val progress = ((destinationPath.length().toFloat() / totalFileSize) * 100).toInt()
        done(progress, null)
    }

    private fun downloadChunk(
        url: String, startByte: Long, endByte: Long, destinationPath: File, totalFileSize: Long
    ) {
        val request = Request.Builder()
            .url(url)
            .header("Range", "bytes=$startByte-$endByte")
            .build()

        val response = client.newCall(request).execute()

        if (!response.isSuccessful) {
            done(null, false)
            return
        }

        try {
            val randomAccessFile = RandomAccessFile(destinationPath, "rw")
            randomAccessFile.seek(startByte)

            response.body?.byteStream()?.use { input ->
                val buffer = ByteArray(8 * 1024)
                var bytesRead: Int

                while (input.read(buffer).also { bytesRead = it } != -1) {
                    randomAccessFile.write(buffer, 0, bytesRead)
                }
            }
            updateProgressBar(destinationPath, totalFileSize)
        } finally {
            response.body?.close()
        }
    }
}