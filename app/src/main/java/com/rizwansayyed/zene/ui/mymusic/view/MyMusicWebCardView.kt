package com.rizwansayyed.zene.ui.mymusic.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.Share.WEB_BASE_URL
import com.rizwansayyed.zene.utils.Utils.shareTxtImage
import com.rizwansayyed.zene.utils.Utils.toast

@Composable
fun MyMusicWebCardView() {
    val openURLOnBrowser = stringResource(R.string.open_url_on_browser)

    Column(
        Modifier
            .padding(bottom = 38.dp)
            .padding(18.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(13.dp))
            .background(MainColor)
            .clip(RoundedCornerShape(13.dp))
            .padding(5.dp),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(15.dp))
        ImageView(R.drawable.ic_boy_relaxation, Modifier.size(140.dp))
        Spacer(Modifier.height(10.dp))
        TextPoppins(stringResource(R.string.free_music_with_zene_web), true, size = 15)

        Row(
            Modifier
                .padding(vertical = 17.dp, horizontal = 14.dp)
                .clickable {
                    logEvents(FirebaseLogEvents.FirebaseEvents.OPEN_ZENE_WEB_APP_FROM_APP)
                    shareTxtImage(WEB_BASE_URL, openURLOnBrowser)
                    openURLOnBrowser.toast()
                }
                .fillMaxWidth()
                .clip(RoundedCornerShape(13.dp))
                .background(Color.Black)
                .padding(vertical = 15.dp),
            Arrangement.Center, Alignment.CenterVertically
        ) {
            TextPoppinsSemiBold(stringResource(R.string.open_zene_web), true, size = 14)
        }
    }
}