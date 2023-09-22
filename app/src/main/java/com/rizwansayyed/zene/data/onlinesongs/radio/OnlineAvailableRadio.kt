package com.rizwansayyed.zene.data.onlinesongs.radio

import com.rizwansayyed.zene.data.utils.RadioOnlineAPI.RADIO_BASE_URLS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL

private fun isAPIAvailable(urlString: String): Boolean {
    return try {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 6000
        connection.readTimeout = 6000
        val responseCode = connection.responseCode
        responseCode in 200..299
    } catch (e: Exception) {
        false
    }
}

private suspend fun allRadioBaseURLS(): List<String> {
    val ipv4Addresses = withContext(Dispatchers.IO) {
        InetAddress.getAllByName(RADIO_BASE_URLS)
    }.map { it.hostAddress }

    val reverseJobs = ipv4Addresses.map { ipAddress ->
        InetAddress.getByName(ipAddress).canonicalHostName
    }
    return reverseJobs.map { "https://$it" }
}

 suspend fun activeRadioBaseURL(): String {
    var path = ""
    for (url in allRadioBaseURLS()) {
        if (isAPIAvailable(url) && path.isEmpty()) path = url
    }
    return path
}