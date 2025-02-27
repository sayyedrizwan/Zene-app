package com.rizwansayyed.zene.ui.connect_status.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.data.model.MusicDataTypes.SONGS
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.ButtonWithImageAndBorder
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.MainUtils.openGoogleMapLocation
import com.rizwansayyed.zene.utils.MainUtils.openGoogleMapNameLocation
import com.rizwansayyed.zene.utils.MediaContentUtils.startMedia


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSharingPlace(data: ConnectFeedDataResponse?, close: () -> Unit) {
    ModalBottomSheet(
        { close() }, contentColor = MainColor, containerColor = MainColor
    ) {
        Column(Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(10.dp))
            TextViewSemiBold(data?.location_address ?: "", 16, center = true)
            Spacer(Modifier.height(20.dp))

            ButtonWithImageAndBorder(
                R.drawable.ic_location, R.string.open_location, Color.White, Color.White
            ) {
                val lat = data?.latitude?.toDoubleOrNull()
                val lon = data?.longitude?.toDoubleOrNull()

                if (lat != null && lon != null) openGoogleMapLocation(
                    false, lat, lon, data.location_name ?: ""
                )
                else openGoogleMapNameLocation(data?.location_address ?: "")
            }
            Spacer(Modifier.height(50.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiValueSheet(data: ConnectFeedDataResponse?, close: () -> Unit) {
    ModalBottomSheet(
        { close() }, contentColor = MainColor, containerColor = MainColor
    ) {
        Column(Modifier.fillMaxWidth()) {
            Spacer(Modifier.height(10.dp))
            TextViewSemiBold(data?.emoji ?: "", 65, center = true)
            Spacer(Modifier.height(20.dp))
            TextViewSemiBold(stringResource(R.string.my_vibe_then), 15, center = true)
            Spacer(Modifier.height(50.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun MusicDataSheet(data: ConnectFeedDataResponse?, close: () -> Unit) {
    ModalBottomSheet(
        { close() }, contentColor = MainColor, containerColor = MainColor
    ) {
        Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally) {
            Spacer(Modifier.height(10.dp))
            GlideImage(
                data?.getMusicData()?.thumbnail,
                data?.getMusicData()?.name,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .size(100.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(20.dp))
            TextViewNormal(data?.getMusicData()?.name ?: "", 20, center = true)
            Spacer(Modifier.height(5.dp))
            TextViewNormal(data?.getMusicData()?.artists ?: "", 14, center = true)
            Spacer(Modifier.height(10.dp))
            if (data?.getMusicData()?.type() == SONGS) ButtonWithBorder(R.string.play) {
                startMedia(data.getMusicData())
            }
            else ButtonWithBorder(R.string.view) {
                startMedia(data?.getMusicData())
            }

            Spacer(Modifier.height(50.dp))
        }
    }
}
