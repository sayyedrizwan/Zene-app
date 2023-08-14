package com.rizwansayyed.zene.utils

import android.content.Context
import android.util.Log
import com.rizwansayyed.zene.utils.Utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jsoup.Jsoup
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class DownloadURLPath(private val context: Context, private val id: String) {

    suspend fun path() {
        withContext(Dispatchers.IO) {
            val json = getJson()
            if (json?.isValidJson() == true) {

                val jsonObject = JSONObject(json)
                val contentsArray = jsonObject
                    .getJSONObject("contents")
                    .getJSONObject("twoColumnWatchNextResults")
                    .getJSONObject("results")
                    .getJSONObject("results")
                    .getJSONArray("contents")

                try {
                    val myFile = File(context.filesDir, "samples.json")
                    myFile.createNewFile()
                    val fOut = FileOutputStream(myFile)
                    val myOutWriter = OutputStreamWriter(fOut)
                    myOutWriter.append(contentsArray.toString())
                    myOutWriter.close()
                    fOut.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                "no a ".showToast()
            }
        }
    }

    private fun getJson(): String? {
        try {
            val doc = Jsoup.connect("https://www.youtube.com/watch?v=${id}").get()
            doc.getElementsByTag("script").forEach { script ->
                if (script.html().contains("var ytInitialData = ")) {
                    return extractJson(script.html().replace(";", " ").replace("\n", ""))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun extractJson(input: String): String {
        val variableName = "ytInitialData"
        val pattern = "$variableName\\s*=\\s*([^;]+)".toRegex()
        val matchResult = pattern.find(input)
        return matchResult?.groupValues?.get(1)?.trim() ?: ""
    }

    private fun String.isValidJson(): Boolean {
        return try {
            JSONObject(this)
            true
        } catch (e: Exception) {
            false
        }
    }
}