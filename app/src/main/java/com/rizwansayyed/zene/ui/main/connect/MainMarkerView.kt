package com.rizwansayyed.zene.ui.main.connect

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberMarkerState
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.utils.MainUtils.getBitmapFromURL
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@Composable
fun MainMarkerView(currentLatLng: LatLng) {
    val userInfo by userInfo.collectAsState(initial = null)
    val mainUserMarker = rememberMarkerState(position = currentLatLng)

    if (userInfo != null) MarkerComposable(
        keys = arrayOf(1), state = mainUserMarker, title = userInfo?.name
    ) {
        MapMarkerUI(userInfo?.photo, true)
    }
}

@Composable
fun UsersMainMarkerView(user: ConnectUserResponse) {
    var latLon by remember { mutableStateOf<LatLng?>(null) }
    if (latLon != null) {
        val mainUserMarker = rememberMarkerState(position = latLon!!)

        MarkerComposable(
            keys = arrayOf(user.email ?: ""), state = mainUserMarker, title = user.name
        ) {
            MapMarkerUI(user.profile_photo, false)
        }
    }
    LaunchedEffect(Unit) {
        delay((3..7).random().seconds)
        try {
            val lat = user.location?.substringBefore(",")?.trim()?.toDouble() ?: 0.0
            val lon = user.location?.substringAfter(",")?.trim()?.toDouble() ?: 0.0
            latLon = LatLng(lat, lon)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Composable
fun MapMarkerUI(img: String?, isUser: Boolean = false) {
    Box(Modifier.size(26.dp, 35.dp)) {
        Row(
            Modifier
                .align(Alignment.BottomCenter)
                .rotate(90f)
        ) {
            ImageIcon(R.drawable.ic_play_filled, 9, if (isUser) MainColor else Color.Black)
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .size(26.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isUser) MainColor else Color.Black)
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            getBitmapFromURL(img)?.asImageBitmap()?.let {
                Image(
                    bitmap = it, "", modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(13.dp))
                )
            }
        }
    }
}