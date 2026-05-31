package com.rizwansayyed.zene.data

import android.util.Base64
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_BASE_URL_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_USER_REFRESH_TOKEN_API
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject

/**
 * Keeps the user logged in across long absences (Instagram-style) without ever
 * sending an expired access token.
 *
 * The access token is intentionally short-lived so a leaked token dies fast.
 * Before any authenticated request goes out, this interceptor inspects the
 * `token` header: if that JWT is expired (or about to expire), it spends the
 * long-lived refresh token at /refresh-token to mint a fresh access token,
 * persists the rotated pair, and swaps the header — all transparently. The user
 * only ever has to re-login if the refresh token itself is gone/expired (~1y of
 * total inactivity), in which case the server's normal isExpire/logout response
 * takes over.
 */
class AuthInterceptor : Interceptor {

    // A bare client (no auth interceptor) used only for the refresh call, to
    // avoid recursing into ourselves.
    private val refreshClient = OkHttpClient.Builder().build()

    // Refresh slightly before the real expiry to absorb clock skew / latency.
    private val expirySkewMs = 60_000L

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = original.header("token") ?: return chain.proceed(original)

        if (!isExpired(token)) return chain.proceed(original)

        val newAccess = synchronized(this) {
            // Re-check inside the lock: another thread may have just refreshed.
            val current = runBlocking { DataStorageManager.userInfo.firstOrNull() }?.authToken
            if (current != null && current != token && !isExpired(current)) {
                current
            } else {
                refreshTokens()
            }
        }

        val request = if (newAccess != null) {
            original.newBuilder().header("token", newAccess).build()
        } else {
            // Refresh failed — let the request go through with the old token so
            // the server returns isExpire/logout and the app's existing logout
            // flow handles it.
            original
        }

        return chain.proceed(request)
    }

    /** Performs the refresh round-trip, persists the rotated tokens, returns the new access token or null. */
    private fun refreshTokens(): String? {
        val info = runBlocking { DataStorageManager.userInfo.firstOrNull() } ?: return null
        val refreshToken = info.refreshToken ?: return null

        return try {
            val payload = JSONObject().apply { put("refreshToken", refreshToken) }
            val body = payload.toString().toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(ZENE_BASE_URL_API + ZENE_USER_REFRESH_TOKEN_API)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build()

            refreshClient.newCall(request).execute().use { response ->
                val text = response.body?.string() ?: return null
                val json = JSONObject(text)

                if (json.optBoolean("logout", false)) return null
                val newAccess = json.optString("authToken", "").ifEmpty { return null }
                val newRefresh = json.optString("refreshToken", refreshToken)

                runBlocking {
                    DataStorageManager.userInfo =
                        flowOf(info.copy(authToken = newAccess, refreshToken = newRefresh))
                }
                newAccess
            }
        } catch (e: Exception) {
            null
        }
    }

    /** Decodes the JWT `exp` claim (no signature check) and tells us if it is past. */
    private fun isExpired(jwt: String): Boolean {
        return try {
            val parts = jwt.split(".")
            if (parts.size < 2) return false
            val payloadJson = String(
                Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
            )
            val exp = JSONObject(payloadJson).optLong("exp", 0L)
            if (exp == 0L) return false
            (exp * 1000L) - expirySkewMs <= System.currentTimeMillis()
        } catch (_: Exception) {
            // Can't read it — let the server be the judge.
            false
        }
    }
}
