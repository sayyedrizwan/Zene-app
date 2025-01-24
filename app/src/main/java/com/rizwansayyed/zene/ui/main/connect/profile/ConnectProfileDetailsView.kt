package com.rizwansayyed.zene.ui.main.connect.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.ImageWithBorder
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold

@Composable
fun ConnectProfileDetailsView(data: ConnectUserInfoResponse) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
    ) {
        item {
            TopSheetView(data)
            Spacer(Modifier.height(30.dp))
        }
        item {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceEvenly, Alignment.CenterVertically) {
                ImageWithBorder(R.drawable.ic_message_multiple) {

                }

                ImageWithBorder(R.drawable.ic_music_note) {

                }

                ImageWithBorder(R.drawable.ic_location) {

                }
            }
            Spacer(Modifier.height(50.dp))
        }
        item {
            SongListeningTo()
            Spacer(Modifier.height(50.dp))
        }
        item {
            ConnectUserMapView(data.user)
            Spacer(Modifier.height(50.dp))
        }
        item {
            ConnectTopListenedView()
            Spacer(Modifier.height(60.dp))
        }
        item {
            ConnectSettingsView()
            Spacer(Modifier.height(150.dp))
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SongListeningTo() {
    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        GlideImage(
            "https://lh3.googleusercontent.com/jEiIvzkHEko1PzjIxydaWgsunfstrLQQ66ghNl-mUsKPDHYCRnCEb9t5QI-DUygtbf2EePLYXBXdHF0i",
            "ss",
            Modifier.size(120.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .padding(horizontal = 5.dp)
                .weight(1f)
        ) {
            TextViewBold(stringResource(R.string.listening_to), 13)
            TextViewNormal("Into Your Arms ", 18, line = 2)
            TextViewLight("Witt Lowry", 13, line = 3)
        }
        GlideImage(
            R.raw.wave_animiation, "wave", Modifier.size(70.dp), contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun TopSheetView(data: ConnectUserInfoResponse) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp), Arrangement.Center, Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            TextViewSemiBold(data.user?.name ?: "", 25)
            TextViewNormal("@${data.user?.username}", 14)
            TextViewNormal(data.user?.country ?: "", 14)
        }
        ButtonWithBorder(R.string.friends) {

        }
    }
}
