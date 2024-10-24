package com.rizwansayyed.zene.ui.videoplayer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.db.DataStoreManager.videoSpeedSettingsDB
import com.rizwansayyed.zene.data.db.model.MusicSpeed
import com.rizwansayyed.zene.service.MusicServiceUtils.openVideoPlayer
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.videoplayer.webview.WebAppInterface
import com.rizwansayyed.zene.ui.view.BorderButtons
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.ui.view.openSpecificIntent
import com.rizwansayyed.zene.ui.view.shareUrl
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.openYoutubeInfo
import com.rizwansayyed.zene.utils.Utils.shareTxtImage
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.flow.flowOf

@Composable
fun VideoInfoPlay(webApp: WebAppInterface) {
    var infoAlert by remember { mutableStateOf(false) }

    ImageIcon(R.drawable.ic_information_circle, 24) {
        infoAlert = true
    }

    if (infoAlert) Dialog({ infoAlert = false }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardColors(MainColor, MainColor, MainColor, MainColor)
        ) {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(horizontal = 7.dp)
            ) {
                Spacer(Modifier.height(30.dp))

                AsyncImage(
                    imgBuilder(webApp.songInfo?.thumbnail),
                    webApp.songInfo?.name,
                    Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.DarkGray),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.height(10.dp))
                TextPoppins(webApp.songInfo?.name ?: "", true, size = 16)
                Spacer(Modifier.height(3.dp))
                TextPoppins(webApp.songInfo?.artists ?: "", true, size = 14)
                Spacer(Modifier.height(15.dp))

                BorderButtons(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            webApp.songInfo?.id?.let { openYoutubeInfo(it) }
                        }, R.drawable.ic_flim_slate, R.string.view_on_youtube
                )
                Spacer(Modifier.height(5.dp))
                BorderButtons(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            webApp.songInfo?.let { shareTxtImage(shareUrl(it)) }
                        }, R.drawable.ic_share, R.string.share
                )
            }
        }
    }
}