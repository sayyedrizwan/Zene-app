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
import com.rizwansayyed.zene.BaseApplication
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.domain.datastore.MusicSpeedEnum
import com.rizwansayyed.zene.domain.datastore.SeekMusicButtonEnum
import com.rizwansayyed.zene.ui.settings.ViewLocalSongs
import com.rizwansayyed.zene.ui.theme.BlackLight
import com.rizwansayyed.zene.utils.QuickSandSemiBold
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

@Composable
fun SettingsPlayingSpeed() {
    val seekButton by dataStoreManager.musicPlaySpeed.collectAsState(runBlocking { dataStoreManager.musicPlaySpeed.first() })

    QuickSandSemiBold(
        stringResource(id = R.string.song_play_speed),
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
            stringResource(id = R.string.zero_five_x),
            seekButton ==  MusicSpeedEnum.ZERO_FIVE.v
        ) {
            dataStoreManager.musicPlaySpeed = flowOf(MusicSpeedEnum.ZERO_FIVE.v)
        }


        ViewLocalSongs(
            stringResource(id = R.string.one_x),
            seekButton == MusicSpeedEnum.ONE.v
        ) {
            dataStoreManager.musicPlaySpeed = flowOf(MusicSpeedEnum.ONE.v)
        }


        ViewLocalSongs(
            stringResource(id = R.string.one_five_x),
            seekButton == MusicSpeedEnum.ONE_FIVE.v
        ) {
            dataStoreManager.musicPlaySpeed = flowOf(MusicSpeedEnum.ONE_FIVE.v)
        }

        ViewLocalSongs(
            stringResource(id = R.string.two_x),
            seekButton == MusicSpeedEnum.TWO.v
        ) {
            dataStoreManager.musicPlaySpeed = flowOf(MusicSpeedEnum.TWO.v)
        }

        ViewLocalSongs(
            stringResource(id = R.string.two_five_x),
            seekButton == MusicSpeedEnum.TWO_FIVE.v
        ) {
            dataStoreManager.musicPlaySpeed = flowOf(MusicSpeedEnum.TWO_FIVE.v)
        }

        ViewLocalSongs(
            stringResource(id = R.string.three_x),
            seekButton == MusicSpeedEnum.THREE.v
        ) {
            dataStoreManager.musicPlaySpeed = flowOf(MusicSpeedEnum.THREE.v)
        }
    }
}
