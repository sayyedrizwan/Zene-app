package com.rizwansayyed.zene.presenter.ui.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.offlineSongsSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.seekButtonSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.songQualitySettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.songSpeedSettings
import com.rizwansayyed.zene.data.db.datastore.OfflineSongsInfo
import com.rizwansayyed.zene.data.db.datastore.SeekButton
import com.rizwansayyed.zene.data.db.datastore.SongSpeed
import com.rizwansayyed.zene.data.db.datastore.SongsQualityInfo
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.util.UiUtils
import com.rizwansayyed.zene.service.player.utils.Utils.openEqualizer
import kotlinx.coroutines.flow.flowOf

@Composable
fun SettingsView() {
    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(DarkGreyColor)
    ) {
        Spacer(Modifier.height(50.dp))

        TextBold(
            stringResource(R.string.settings), Modifier.padding(start = 12.dp), size = 42
        )

        Spacer(Modifier.height(70.dp))

        OfflineModesSettings()
        SongQualitySettings()

        SettingsItemsCard(R.string.equalizer) {
            openEqualizer()
        }

        SeekButtonSettings()
        SongSpeedSettings()

        Spacer(Modifier.height(120.dp))
    }
}

@Composable
fun OfflineModesSettings() {
    val v by offlineSongsSettings.collectAsState(initial = OfflineSongsInfo.LOCAL_SONGS.v)
    SettingsLayout(R.string.offline_mode) {
        SettingsItems(R.string.show_local_songs, v == OfflineSongsInfo.LOCAL_SONGS.v) {
            offlineSongsSettings = flowOf(OfflineSongsInfo.LOCAL_SONGS.v)
        }
        SettingsItems(R.string.suggested_songs, v == OfflineSongsInfo.SUGGESTED_SONGS.v) {
            offlineSongsSettings = flowOf(OfflineSongsInfo.SUGGESTED_SONGS.v)
        }
        SettingsItems(
            R.string.offline_downloaded_songs, v == OfflineSongsInfo.OFFLINE_DOWNLOAD.v
        ) {
            offlineSongsSettings = flowOf(OfflineSongsInfo.OFFLINE_DOWNLOAD.v)
        }
    }
}

@Composable
fun SeekButtonSettings() {
    val v by seekButtonSettings.collectAsState(initial = SeekButton.FIVE.v)
    SettingsLayout(R.string.seek_button_timer) {
        SettingsItems(R.string.hide_seek_button, v == SeekButton.HIDE.v) {
            seekButtonSettings = flowOf(SeekButton.HIDE.v)
        }
        SettingsItems(R.string.five_s_seek_button, v == SeekButton.FIVE.v) {
            seekButtonSettings = flowOf(SeekButton.FIVE.v)
        }
        SettingsItems(R.string.ten_s_seek_button, v == SeekButton.TEN.v) {
            seekButtonSettings = flowOf(SeekButton.TEN.v)
        }
        SettingsItems(R.string.ten_fifteen_seek_button, v == SeekButton.FIFTEEN.v) {
            seekButtonSettings = flowOf(SeekButton.FIFTEEN.v)
        }
    }
}

@Composable
fun SongSpeedSettings() {
    val v by songSpeedSettings.collectAsState(initial = SongSpeed.ONE.v)
    SettingsLayout(R.string.song_playback_speed) {
        SettingsItems(R.string.zero_two_five, v == SongSpeed.ZERO_TWO_FIVE.v) {
            songSpeedSettings = flowOf(SongSpeed.ZERO_TWO_FIVE.v)
        }
        SettingsItems(R.string.zero_five, v == SongSpeed.ZERO_FIVE.v) {
            songSpeedSettings = flowOf(SongSpeed.ZERO_FIVE.v)
        }
        SettingsItems(R.string.zero_seven_five, v == SongSpeed.ZERO_SEVEN_FIVE.v) {
            songSpeedSettings = flowOf(SongSpeed.ZERO_SEVEN_FIVE.v)
        }
        SettingsItems(R.string.one_zero, v == SongSpeed.ONE.v) {
            songSpeedSettings = flowOf(SongSpeed.ONE.v)
        }
        SettingsItems(R.string.one_two_five, v == SongSpeed.ONE_TWO_FIVE.v) {
            songSpeedSettings = flowOf(SongSpeed.ONE_TWO_FIVE.v)
        }
        SettingsItems(R.string.one_five, v == SongSpeed.ONE_FIVE.v) {
            songSpeedSettings = flowOf(SongSpeed.ONE_FIVE.v)
        }
        SettingsItems(R.string.one_seven_five, v == SongSpeed.ONE_SEVEN_FIVE.v) {
            songSpeedSettings = flowOf(SongSpeed.ONE_SEVEN_FIVE.v)
        }
        SettingsItems(R.string.two_zero, v == SongSpeed.TWO.v) {
            songSpeedSettings = flowOf(SongSpeed.TWO.v)
        }
    }
}

@Composable
fun SongQualitySettings() {
    val v by songQualitySettings.collectAsState(initial = SongsQualityInfo.HIGH_QUALITY.v)
    SettingsLayout(R.string.song_quality) {
        SettingsItems(R.string.low_quality, v == SongsQualityInfo.LOW_QUALITY.v) {
            songQualitySettings = flowOf(SongsQualityInfo.LOW_QUALITY.v)
        }
        SettingsItems(R.string.high_quality, v == SongsQualityInfo.HIGH_QUALITY.v) {
            songQualitySettings = flowOf(SongsQualityInfo.HIGH_QUALITY.v)
        }
        SettingsItems(
            R.string.high_quality_on_wifi, v == SongsQualityInfo.HIGH_QUALITY_WIFI.v
        ) {
            songQualitySettings = flowOf(SongsQualityInfo.HIGH_QUALITY_WIFI.v)
        }
    }
}

@Composable
fun SettingsItems(text: Int, showMarker: Boolean, click: () -> Unit) {
    Row(
        Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable {
                click()
            }, Arrangement.Center, Alignment.CenterVertically
    ) {
        TextRegular(
            stringResource(text),
            Modifier
                .padding(vertical = 16.dp, horizontal = 14.dp)
                .weight(1f),
            size = 14
        )
        if (showMarker) SmallIcons(R.drawable.ic_tick)
    }
}

@Composable
fun SettingsItemsCard(text: Int, click: () -> Unit) {

    Row(
        Modifier
            .padding(5.dp)
            .padding(horizontal = 7.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BlackColor)
            .clickable {
                click()
            }, Arrangement.Center, Alignment.CenterVertically
    ) {
        TextRegular(
            stringResource(text),
            Modifier
                .padding(vertical = 16.dp, horizontal = 14.dp)
                .weight(1f),
            size = 14
        )

        SmallIcons(R.drawable.ic_arrow_right)

        Spacer(Modifier.width(5.dp))
    }


    Spacer(Modifier.height(95.dp))
}


@Composable
fun SettingsLayout(title: Int, content: @Composable ColumnScope.() -> Unit) {
    TextBold(stringResource(title), Modifier.padding(horizontal = 14.dp), size = 19)

    Spacer(Modifier.height(9.dp))

    Column(
        Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BlackColor)
    ) {
        content()
    }


    Spacer(Modifier.height(95.dp))
}