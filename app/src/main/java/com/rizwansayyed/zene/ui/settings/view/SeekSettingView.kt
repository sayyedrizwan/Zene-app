package com.rizwansayyed.zene.ui.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.datastore.DataStoreMusicEnum
import com.rizwansayyed.zene.domain.datastore.SeekMusicButtonEnum
import com.rizwansayyed.zene.ui.settings.ViewLocalSongs
import com.rizwansayyed.zene.ui.theme.BlackLight
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

@Composable
fun SeekSettings() {
    val seekButton by dataStoreManager.seekButton.collectAsState(runBlocking { dataStoreManager.seekButton.first() })

    QuickSandSemiBold(
        stringResource(id = R.string.seek_btn),
        size = 16,
        modifier = Modifier.padding(top = 35.dp, start = 15.dp)
    )

    Column(
        Modifier
            .padding(vertical = 15.dp, horizontal = 9.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BlackLight)
            .padding(5.dp)
    ) {
        ViewLocalSongs(
            stringResource(id = R.string.hide_seek_button),
            seekButton == SeekMusicButtonEnum.OFF.v
        ) {
            dataStoreManager.seekButton = flowOf(SeekMusicButtonEnum.OFF.v)
        }


        ViewLocalSongs(
            stringResource(id = R.string.five_s_seek_button),
            seekButton == SeekMusicButtonEnum.FIVE.v
        ) {
            dataStoreManager.seekButton = flowOf(SeekMusicButtonEnum.FIVE.v)
        }


        ViewLocalSongs(
            stringResource(id = R.string.ten_s_seek_button),
            seekButton == SeekMusicButtonEnum.TEN.v
        ) {
            dataStoreManager.seekButton = flowOf(SeekMusicButtonEnum.TEN.v)
        }

        ViewLocalSongs(
            stringResource(id = R.string.fifteen_s_seek_button),
            seekButton == SeekMusicButtonEnum.FIFTEEN.v
        ) {
            dataStoreManager.seekButton = flowOf(SeekMusicButtonEnum.FIFTEEN.v)
        }
    }
}