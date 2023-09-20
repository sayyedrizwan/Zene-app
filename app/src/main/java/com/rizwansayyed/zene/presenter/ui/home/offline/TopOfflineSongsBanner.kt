package com.rizwansayyed.zene.presenter.ui.home.offline

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextBoldBig
import com.rizwansayyed.zene.presenter.ui.TextLight
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.viewmodel.OfflineSongsViewModel

val albums =
    "https://lh3.googleusercontent.com/OhxDTHQOQzSrcdgH9hzqzp1v22GYDE-QKnkryvCeq4ddx-3K3_c8oDXN0E6NvHlMn1q4XV59aHr0oL4f=w847-h847-l90-rj"

@Composable
fun TopBannerSuggestions() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Column(
        Modifier
            .fillMaxWidth()
            .clickable { "play offline".toast() },
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        AsyncImage(
            albums, "",
            Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(screenHeight / 2)
                .clip(RoundedCornerShape(13.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(2.dp))

        TextBoldBig("Cruel Summer")

        Spacer(Modifier.height(1.dp))

        TextLight("Taylor Swift")
    }
}