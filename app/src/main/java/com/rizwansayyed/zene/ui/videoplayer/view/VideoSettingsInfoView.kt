package com.rizwansayyed.zene.ui.videoplayer.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.videoQualityDB
import com.rizwansayyed.zene.ui.settings.model.SongQualityTypes
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.videoplayer.webview.WebAppInterface
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import kotlinx.coroutines.flow.flowOf

@Composable
fun VideoSettingsInfoView(webApp: WebAppInterface) {
    val songQuality by videoQualityDB.collectAsState(initial = SongQualityTypes.HIGH_QUALITY)
    var settingsAlert by remember { mutableStateOf(false) }

    ImageIcon(R.drawable.ic_setting, 27) {
        settingsAlert = true
    }

    if (settingsAlert) Dialog({ settingsAlert = false }) {
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
                Row(Modifier.padding(start = 10.dp, bottom = 20.dp)) {
                    TextPoppins(
                        stringResource(R.string.available_video_quality),
                        false, size = 16, limit = 1
                    )
                }
                SongQualityTypes.entries.forEach {
                    Row(
                        Modifier
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                            .clickable {
                                videoQualityDB = flowOf(it)
                                webApp.playLastDuration()
                                settingsAlert = false
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextPoppins("${it.value}p", size = 14)
                        Spacer(Modifier.weight(1f))
                        if (songQuality == it) ImageIcon(R.drawable.ic_tick, 24)
                    }
                }
            }
        }
    }

}