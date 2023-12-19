package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.facebook


import android.util.Log
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.jsoupResponseData
import com.rizwansayyed.zene.data.utils.FacebookURL.facebookPage
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.jsoup.Jsoup
import javax.inject.Inject


class FacebookScrapsImpl @Inject constructor() : FacebookScrapsImplInterface {
    override suspend fun pageCreds(a: String) = flow {
        val jsoup = Jsoup.parse(jsoupResponseData(facebookPage(a))!!)

        var cursorCode = ""
        jsoup.html().split("\"end_cursor\":").forEach { endCursor ->
            val cursor = endCursor.substringBefore("}},").replace("\"", "")
            if (cursor.length > 100) cursorCode = cursor
            Log.d("TAG", "pageCreds: $cursorCode")
        }

        emit("")
    }.flowOn(Dispatchers.IO)

}