package com.rizwansayyed.zene.data.api

import android.os.Build
import com.google.firebase.messaging.FirebaseMessaging
import com.rizwansayyed.zene.data.api.ip.IpAPIService
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.data.api.zene.ZeneAPIService
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject


class ZeneAPIImpl @Inject constructor(
    val zeneAPI: ZeneAPIService, val ipAPI: IpAPIService
) : ZeneAPIInterface {

    override suspend fun updateUser() = flow {
        val ip = ipAPI.ip()
        val users = userInfoDB.firstOrNull()
        val fcm = FirebaseMessaging.getInstance().token.await()

        val song = JSONArray().apply {

        }
        val artists = JSONArray().apply {

        }
        val json = JSONObject().apply {
            put("name", users?.name)
            put("email", users?.email)
            put("photo", users?.profilePhoto)
            put("playtime", users?.totalPlayTime ?: 0)
            put("songs", song)
            put("ip", ip.query)
            put("fcm", fcm)
            put("device", "${Build.MANUFACTURER} ${Build.MODEL} ${Build.VERSION.RELEASE}")
            put("country", "${ip.city}, ${ip.regionName}, ${ip.country}")
            put("review", users?.isReviewDone ?: false)
            put("artists", artists)
        }

        val body = RequestBody.create(MediaType.parse("application/json"), json.toString())
        emit(zeneAPI.updateUser(body))
    }

    override suspend fun getUser(email: String) = flow {
        emit(zeneAPI.getUser(email))
    }
}