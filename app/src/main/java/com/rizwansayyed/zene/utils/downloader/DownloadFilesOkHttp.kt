package com.rizwansayyed.zene.utils.downloader

import android.graphics.Bitmap
import com.rizwansayyed.zene.utils.Utils.PATH.filesSongDownloader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


object DownloadFilesOkHttp {

    suspend fun downloadMP3File(songName: String, url: String): String? {
        val path = File(filesSongDownloader, "${songName.replace(" ", "-")}.mp3")

        return downloadFile(path, url)
    }

    suspend fun downloadImageFile(songName: String, bitmap: Bitmap) {
        val path = File(filesSongDownloader, "${songName.replace(" ", "-")}.png")
        withContext(Dispatchers.IO) {
            val os: OutputStream = BufferedOutputStream(FileOutputStream(path))
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            withContext(Dispatchers.IO) {
                os.close()
            }
        }
    }

    suspend fun downloadImageFile(songName: String, url: String): String? {
        val path = File(filesSongDownloader, "${songName.replace(" ", "-")}.png")
        return downloadFile(path, url)
    }


    private suspend fun downloadFile(path: File, url: String): String? {
        val request = Request.Builder().url(url).build()
        val response = OkHttpClient.Builder().build().newCall(request).execute()
        if (response.isSuccessful) {
            try {
                val data = response.body?.byteStream() ?: return null

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