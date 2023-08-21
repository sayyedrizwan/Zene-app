package com.rizwansayyed.zene.utils.downloader

import android.content.Context
import android.util.Log
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.rizwansayyed.zene.utils.Utils.PATH.filesSongDownloader
import com.rizwansayyed.zene.utils.Utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream


class DownloadFilesOkHttp {



    suspend fun downloadMP3File(songName: String, path: String): String? {
        val request = Request.Builder().url(path).build()
        val response = OkHttpClient.Builder().build().newCall(request).execute()
        if (response.isSuccessful) {
            try {
                val data = response.body?.byteStream() ?: return null

                val path = File(filesSongDownloader, "${songName.replace(" ", "-")}.mp3")

                return withContext(Dispatchers.IO) {
                    FileOutputStream(path).use { outputStream ->
                        val buffer = ByteArray(8096)
                        var bytesRead: Int
                        while (data.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                        }
                        return@withContext path.absolutePath
                    }
                }
            } catch (e: Exception) {
                return null
            }
        } else {
            return null
        }
    }
}