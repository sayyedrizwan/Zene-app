package com.rizwansayyed.zene.ui.rewards

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.gson.JsonObject
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.rewardsWatchedAds
import com.rizwansayyed.zene.data.db.DataStoreManager.userInfoDB
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.utils.ShowAdsOnAppOpen
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.URLS.BASE_URL
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


@Composable
fun RewardsView() {
    val activity = LocalContext.current as Activity

    var currentProgress by remember { mutableFloatStateOf(0f) }
    var participate by remember { mutableIntStateOf(21) }
    val adsWatched by rewardsWatchedAds.collectAsState(initial = 0)

    val watchedAdsDone = stringResource(R.string.watch_ad_done)
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {
        item {
            Spacer(Modifier.height(60.dp))
            TextPoppins(stringResource(R.string.rewards), size = 40)
            Spacer(Modifier.height(5.dp))
            TextPoppins("(Beta)", size = 17)
        }

        item {
            Spacer(Modifier.height(50.dp))

            TextPoppins(stringResource(R.string.zene_rewards_desc), true, size = 16)
            Spacer(Modifier.height(10.dp))

            Spacer(Modifier.height(60.dp))
        }

        item {
            TextPoppins(stringResource(R.string.rewards), size = 16)

            Spacer(Modifier.height(10.dp))

            Row(
                Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.Black)
                    .clickable {
                        Utils.openBrowser("https://amzn.in/d/fPglXaV")
                    },
                Arrangement.Center, Alignment.CenterVertically
            ) {
                AsyncImage(
                    "https://m.media-amazon.com/images/I/51h7CQTRJ1L._SL1500_.jpg",
                    "",
                    Modifier
                        .padding(10.dp)
                        .size(85.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .padding(10.dp)
                )

                Column(Modifier.weight(1f)) {
                    TextPoppins("OnePlus Buds 3", size = 17)
                    Spacer(Modifier.height(5.dp))
                    TextPoppinsThin("by OnePlus ", size = 15)
                }
            }

            Spacer(Modifier.height(10.dp))

            Row(
                Modifier
                    .padding(horizontal = 10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.Black)
                    .clickable {
                        Utils.openBrowser("https://amzn.in/d/0yGGaeV")
                    },
                Arrangement.Center, Alignment.CenterVertically
            ) {
                AsyncImage(
                    "https://m.media-amazon.com/images/I/41k17nu+wAL._SL1200_.jpg",
                    "",
                    Modifier
                        .padding(10.dp)
                        .size(85.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .padding(10.dp)
                )

                Column(Modifier.weight(1f)) {
                    TextPoppins("Sony Wf-C500 Truly Wireless Bluetooth", size = 17)
                    Spacer(Modifier.height(5.dp))
                    TextPoppinsThin("by Sony ", size = 15)
                }
            }
        }

        item {
            Spacer(Modifier.height(50.dp))
            TextPoppins(stringResource(R.string.zene_rewards_desc_1), true, size = 16)
            Spacer(Modifier.height(30.dp))

            LinearProgressIndicator(
                progress = { currentProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
                color = MainColor,
                trackColor = Color.White,
            )
            Spacer(Modifier.height(10.dp))
            TextPoppins("Watched ${adsWatched}/50", true, size = 16)
        }


        item {
            Spacer(Modifier.height(10.dp))
            TextPoppinsThin("$participate people have participated", true, size = 14)

            Row(
                Modifier
                    .padding(vertical = 20.dp, horizontal = 10.dp)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(10))
                    .background(MainColor)
                    .clickable {
                        if (adsWatched >= 50) {
                            watchedAdsDone.toast()
                            return@clickable
                        }
                        ShowAdsOnAppOpen(activity).showRewardsAds()
                    }
                    .padding(vertical = 15.dp, horizontal = 12.dp),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                TextPoppins(stringResource(R.string.watch_ad), true, size = 16)
            }

            TextPoppinsThin(
                "If ads are not loading after clicking on watch ads for 5 seconds. That means the ads are not available in your area. Try again after some time.",
                true,
                size = 14
            )
        }


        item {
            Spacer(Modifier.height(300.dp))
        }
    }


    LaunchedEffect(Unit) {
        currentProgress = adsWatched.toFloat() / 100
        participate = getParticipated()
    }
}

suspend fun getParticipated() = withContext(Dispatchers.IO) {
    var num: Int
    try {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("${BASE_URL}participates.txt")
            .get()
            .build()

        val response = client.newCall(request).execute()
        val int = response.body?.string()
        num = int?.trim()?.toInt() ?: 0
    } catch (e: Exception) {
        num = 21
    }

    return@withContext num
}

fun updateTheAdsLogs() = CoroutineScope(Dispatchers.IO).launch {
    try {
        val email = userInfoDB.firstOrNull()?.email
        val client = OkHttpClient().newBuilder()
            .build()
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject().apply {
            put("email", email)
        }
        val body = jsonObject.toString().toRequestBody(mediaType)
        val request = Request.Builder()
            .url("${BASE_URL}zuser/add_ads_logs")
            .method("POST", body)
            .addHeader("auth", BuildConfig.AUTH_HEADER)
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).execute()
    } catch (e: Exception) {
        e.message
    }

}