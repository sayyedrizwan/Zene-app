package com.rizwansayyed.zene.utils.downloader

import android.content.Context
import android.util.SparseArray
import com.rizwansayyed.zene.utils.Utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.jsoup.Jsoup
import java.net.URLDecoder
import java.util.regex.Pattern

class DownloadURLPath(private val context: Context, private val id: String) {


    private val patPlayerResponse =
        Pattern.compile("var ytInitialPlayerResponse\\s*=\\s*(\\{.+?\\})\\s*;")


    private val patSigEncUrl = Pattern.compile("url=(.+?)(\\u0026|$)")
    private val patSignature = Pattern.compile("s=(.+?)(\\u0026|$)")

    private var playerPath = ""


    private val FORMAT_MAP = SparseArray<Format>()

    suspend fun path() {
        withContext(Dispatchers.IO) {
            val json = getJson()
            if (json != null) {
//                val download = moshi.adapter(YTDownloadJSON::class.java).fromJson(json.toString())
//
//                val highAudioQuality = download?.adaptiveFormats?.let { findMaxAudioValueData(it) }
//
//
//                Log.d(
//                    "TAG",
//                    "path: the download ${simplifyURL(url = highAudioQuality?.signatureCipher ?: "")} ${
//                        bz(
//                            "AAOq0QJ8wRAIgSIxQ7t2RUzMoj3zAaEtW7ikspgK2y44VrZFQkQHtIlMCIGELnJOndlvk8ZJ8z2sRHyXLi83bRhRgw49bKuVXBl5J"
//                        )
//                    }"
//                )
//
//                Log.d(
//                    "TAG",
//                    "path: the download ==== ${bz("AAOq0QJ8wRAIgSIxQ7t2RUzMoj3zAaEtW7ikspgK2y44VrZFQkQHtIlMCIGELnJOndlvk8ZJ8z2sRHyXLi83bRhRgw49bKuVXBl5J")}"
//                )
//
//
//                decryptSignatureCipher("")
//                try {
//                    val myFile = File(context.filesDir, "samples.txt")
//                    myFile.createNewFile()
//                    val fOut = FileOutputStream(myFile)
//                    val myOutWriter = OutputStreamWriter(fOut)
//                    myOutWriter.append(bz("wOAOq0QJ8sRQIhAOYgcGnvo5SUqYFTSTFIh7ckKpQpGoW91KQMjKVAI0w4AiBD6CrLnxSmPKGqL6D-uRO7w1Gz1cuBiaywITVJ8FJCFQ=="))
//                    myOutWriter.close()
//                    fOut.close()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
            } else {
                "no a ".showToast()
            }
        }
    }

    private fun getJson(): JSONObject? {
        var response: JSONObject? = null
        try {
            val doc = Jsoup.connect("https://www.youtube.com/watch?v=${id}").get()
            doc.getElementsByTag("script").forEach { script ->
                if (script.html().contains("var ytInitialPlayerResponse = ")) {
                    response = extractJson(script.html())
                }
                if (script.attr("src").contains("/s/player/")) {
                    playerPath = script.attr("src")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return response
    }

    private fun extractJson(input: String): JSONObject? {
        try {
            val mat = patPlayerResponse.matcher(input)
            if (mat.find()) {
                val ytPlayerResponse = mat.group(1)?.let { JSONObject(it) }
                return ytPlayerResponse?.getJSONObject("streamingData")
            }
        } catch (e: Exception) {
            e.message
        }
        return null
    }

    private fun simplifyURL(url: String): String {
        val decode = try {
            URLDecoder.decode(url, "UTF-8")
        } catch (e: Exception) {
            ""
        }
        return decode
    }

    private fun findMaxAudioValueData(dataList: List<AdaptiveFormat>): AdaptiveFormat? {
        if (dataList.isEmpty()) {
            return null
        }

        var maxData: AdaptiveFormat? = null
        var maxValue = Long.MIN_VALUE

        for (data in dataList) {
            if (data.mimeType.contains("audio"))
                if (data.bitrate > maxValue) {
                    maxData = data
                    maxValue = data.bitrate
                }
        }

        return maxData
    }

    private fun decryptSignatureCipher(signatureCipher: String): String? {
        val baseJsUrl = "https://www.youtube.com/embed/video_id.js"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(baseJsUrl)
            .build()

        val response = runBlocking { client.newCall(request).execute() }

        if (response.isSuccessful) {
            val baseJs = response.body?.string() ?: return null

            return baseJs
//            val decryptionFunction = re.findall(
//                Regex("""function decryptSignatureCipher\((.*)\)"""),
//                baseJs
//            )[0]
//
//            return eval(decryptionFunction)(signatureCipher)
        } else {
            "error ${response.body?.string()}".showToast()
        }

        return ""
    }
}

data class YTDownloadJSON(
    val expiresInSeconds: String,
    val formats: List<Format>,
    val adaptiveFormats: List<AdaptiveFormat>,
)

data class Format(
    val itag: Long,
    val mimeType: String,
    val bitrate: Long,
    val width: Long,
    val height: Long,
    val lastModified: String,
    val quality: String,
    val xtags: String,
    val fps: Long,
    val qualityLabel: String,
    val projectionType: String,
    val audioQuality: String,
    val approxDurationMs: String,
    val audioSampleRate: String,
    val audioChannels: Long,
    val signatureCipher: String,
)


data class AdaptiveFormat(
    val itag: Long,
    val mimeType: String,
    val bitrate: Long,
    val width: Long?,
    val height: Long?,
    val initRange: InitRange,
    val indexRange: IndexRange,
    val lastModified: String,
    val contentLength: String,
    val quality: String,
    val fps: Long?,
    val qualityLabel: String?,
    val projectionType: String,
    val averageBitrate: Long,
    val approxDurationMs: String,
    val signatureCipher: String,
    val colorInfo: ColorInfo?,
    val highReplication: Boolean?,
    val audioQuality: String?,
    val audioSampleRate: String?,
    val audioChannels: Long?,
    val loudnessDb: Double?,
)

data class InitRange(
    val start: String,
    val end: String,
)

data class IndexRange(
    val start: String,
    val end: String,
)

data class ColorInfo(
    val primaries: String,
    val transferCharacteristics: String,
    val matrixCoefficients: String,
)