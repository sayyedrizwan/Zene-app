package com.rizwansayyed.zene.service.player.utils

import com.rizwansayyed.zene.datastore.DataStorageManager.ipDB
import com.rizwansayyed.zene.datastore.DataStorageManager.musicPlayerDB
import com.rizwansayyed.zene.utils.safeLaunch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.Normalizer
import kotlin.collections.maxByOrNull
import kotlin.math.max
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds

class GetLatestYTMusicIDAPI(private val click: (String) -> Unit) {

    companion object {
        private val lastRunMap = mutableMapOf<String, Long>()
        private val COOLDOWN_MS = 3.seconds.inWholeMilliseconds

        suspend fun runOnce(name: String, block: suspend () -> Unit) {
            val now = System.currentTimeMillis()
            val lastRun = lastRunMap[name] ?: 0L

            if (now - lastRun >= COOLDOWN_MS) {
                block()
                lastRunMap[name] = now
            }
        }
    }

    private val ytMusicURL = "https://music.youtube.com/youtubei/v1/search?prettyPrint=false"
    private val ytBaseURL = "https://music.youtube.com"

    fun readLocalID() = CoroutineScope(Dispatchers.IO).safeLaunch {
        val playerInfo = musicPlayerDB.firstOrNull()
        runOnce(playerInfo?.data?.id ?: "") {
            val result = getSongDetailsList()

            val candidates = extractCandidates(result)
            if (candidates.isEmpty()) return@runOnce

            val get = findBestVideoId(playerInfo?.data?.name.orEmpty(), candidates)
            if (get != null) click(get.videoId)
        }
    }

    private suspend fun searchName(): String {
        val playerInfo = musicPlayerDB.firstOrNull()
        val a = playerInfo?.data?.artists?.substringBefore(", ")?.substringBefore(" and ")
        return "${playerInfo?.data?.name} - $a".replace("\"", "")
    }

    private suspend fun getSongDetailsList() = withContext(Dispatchers.IO) {
        val ip = ipDB.firstOrNull()

        val client = OkHttpClient()
        val jsonBody = JSONObject().apply {
            put("context", JSONObject().apply {
                put("client", JSONObject().apply {
                    put("hl", "en-GB")
                    put("gl", ip?.countryCode?.uppercase())
                    put("clientName", "WEB_REMIX")
                    put("clientVersion", "1.20260209.03.00")
                })
            })
            put("query", searchName())
        }

        val mediaType = "application/json".toMediaType()
        val requestBody = jsonBody.toString().toRequestBody(mediaType)

        val request = Request.Builder().url(ytMusicURL).post(requestBody).addHeader("accept", "*/*")
            .addHeader("content-type", "application/json").addHeader("origin", ytBaseURL)
            .addHeader("x-origin", ytBaseURL).addHeader("cookie", "GPS=1;").build()


        try {
            val response = client.newCall(request).execute()
            return@withContext response.body.string()
        } catch (_: Exception) {
            return@withContext ""
        }

    }

    data class MatchResult(val videoId: String, val title: String, val score: Double)

    fun normalizeText(s: String): String {
        var t = s.lowercase().trim()
        t = t.replace("“", "\"").replace("”", "\"").replace("’", "'").replace("‘", "'")
        t = Normalizer.normalize(t, Normalizer.Form.NFD)
            .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        t = t.replace(Regex("[^a-z0-9\\s\"']"), " ")
        t = t.replace(Regex("\\s+"), " ").trim()
        return t
    }

    fun stripParentheses(s: String): String {
        return s.replace(Regex("\\(.*?\\)"), " ").replace(Regex("\\s+"), " ").trim()
    }

    fun levenshtein(a: String, b: String): Int {
        if (a == b) return 0
        if (a.isEmpty()) return b.length
        if (b.isEmpty()) return a.length
        val la = a.length
        val lb = b.length
        val dp = Array(la + 1) { IntArray(lb + 1) }
        for (i in 0..la) dp[i][0] = i
        for (j in 0..lb) dp[0][j] = j
        for (i in 1..la) {
            for (j in 1..lb) {
                val cost = if (a[i - 1] == b[j - 1]) 0 else 1
                dp[i][j] = min(
                    min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                    dp[i - 1][j - 1] + cost
                )
            }
        }
        return dp[la][lb]
    }
    fun normalizedLevenshteinSimilarity(a: String, b: String): Double {
        val maxLen = max(a.length, b.length).coerceAtLeast(1)
        val d = levenshtein(a, b)
        return 1.0 - d.toDouble() / maxLen.toDouble()
    }

    fun tokenize(s: String): Set<String> =
        s.split(Regex("\\s+")).filter { it.isNotBlank() && it.length > 0 }.toSet()

    fun jaccard(a: String, b: String): Double {
        val ta = tokenize(a)
        val tb = tokenize(b)
        if (ta.isEmpty() && tb.isEmpty()) return 1.0
        val inter = ta.intersect(tb).size.toDouble()
        val union = ta.union(tb).size.toDouble()
        return if (union == 0.0) 0.0 else inter / union
    }

    fun trigrams(s: String): Set<String> {
        val t = s.replace(" ", "_")
        if (t.length < 3) return setOf(t)
        val out = mutableSetOf<String>()
        for (i in 0..t.length - 3) out.add(t.substring(i, i + 3))
        return out
    }
    fun trigramSimilarity(a: String, b: String): Double {
        val A = trigrams(a)
        val B = trigrams(b)
        if (A.isEmpty() && B.isEmpty()) return 1.0
        val inter = A.intersect(B).size.toDouble()
        val denom = (A.size + B.size).toDouble()
        return if (denom == 0.0) 0.0 else (2.0 * inter) / denom
    }

    fun scoreCandidate(target: String, candidate: String): Double {
        val tNorm = normalizeText(target)
        val cNorm = normalizeText(candidate)
        val tStrip = stripParentheses(tNorm)
        val cStrip = stripParentheses(cNorm)

        val levens = max(
            normalizedLevenshteinSimilarity(tNorm, cNorm),
            normalizedLevenshteinSimilarity(tStrip, cStrip)
        )
        val jac = max(jaccard(tNorm, cNorm), jaccard(tStrip, cStrip))
        val tri = max(trigramSimilarity(tNorm, cNorm), trigramSimilarity(tStrip, cStrip))

        val wLev = 0.50
        val wJac = 0.30
        val wTri = 0.20

        return wLev * levens + wJac * jac + wTri * tri
    }

    fun findBestVideoId(targetTitle: String, candidates: List<Pair<String, String>>, minScore: Double = 0.58): MatchResult? {
        if (candidates.isEmpty()) return null
        val scored = candidates.map { (title, vid) ->
            val s = scoreCandidate(targetTitle, title)
            MatchResult(vid, title, s)
        }
        val best = scored.maxByOrNull { it.score }!!
        return if (best.score >= minScore) best else best
    }
    fun extractCandidates(json: String): List<Pair<String, String>> {
        val result = mutableListOf<Pair<String, String>>()
        val root = JSONObject(json)

        fun extractTitleFromRenderer(renderer: JSONObject): String? {
            return try {
                val flexColumns = renderer
                    .getJSONArray("flexColumns")

                val firstColumn = flexColumns
                    .getJSONObject(0)
                    .getJSONObject("musicResponsiveListItemFlexColumnRenderer")

                val runs = firstColumn
                    .getJSONObject("text")
                    .getJSONArray("runs")

                runs.getJSONObject(0).getString("text")
            } catch (_: Exception) {
                null
            }
        }

        fun walk(obj: Any?) {
            when (obj) {
                is JSONObject -> {
                    if (obj.has("musicResponsiveListItemRenderer")) {
                        val renderer =
                            obj.getJSONObject("musicResponsiveListItemRenderer")

                        val title = extractTitleFromRenderer(renderer)
                        val videoId =
                            renderer.optJSONObject("playlistItemData")
                                ?.optString("videoId")
                                ?: renderer
                                    .optJSONObject("navigationEndpoint")
                                    ?.optJSONObject("watchEndpoint")
                                    ?.optString("videoId")

                        if (!title.isNullOrBlank() && !videoId.isNullOrBlank()) {
                            result.add(title to videoId)
                        }
                    }

                    obj.keys().forEach { key ->
                        walk(obj.get(key))
                    }
                }

                is JSONArray -> {
                    for (i in 0 until obj.length()) {
                        walk(obj.get(i))
                    }
                }
            }
        }
        walk(root)
        return result
    }
}