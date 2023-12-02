package com.rizwansayyed.zene.presenter.ui.home.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.autoplaySettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.doOfflineDownloadWifiSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.loopSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.offlineSongsSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.pauseMusicOnHeadphoneDetachSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.seekButtonSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.setWallpaperSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.showPlayingSongOnLockScreenSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.songQualitySettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.songSpeedSettings
import com.rizwansayyed.zene.data.db.datastore.OfflineSongsInfo
import com.rizwansayyed.zene.data.db.datastore.SeekButton
import com.rizwansayyed.zene.data.db.datastore.SetWallpaperInfo
import com.rizwansayyed.zene.data.db.datastore.SongSpeed
import com.rizwansayyed.zene.data.db.datastore.SongsQualityInfo
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.home.settings.SettingsItems
import com.rizwansayyed.zene.presenter.ui.home.settings.SettingsItemsCard
import com.rizwansayyed.zene.presenter.ui.home.settings.SettingsLayout
import com.rizwansayyed.zene.service.player.utils.Utils.openEqualizer
import com.rizwansayyed.zene.utils.Utils.cacheSize
import kotlinx.coroutines.flow.flowOf

@Composable
fun SettingsView(player: ExoPlayer) {
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

        SeekButtonSettings()
        SongSpeedSettings()
        LoopSettings()
        AutoPlaySettings()
        OfflineDownloadSettings()
        SongOnLockScreenSettings()
        SetWallpaperSettings()
        PlaySongWhenAlarmSettings()
        PauseMusicOnHeadphoneDetachSettings(player)

        Spacer(Modifier.height(20.dp))

        SettingsItemsCard(R.string.equalizer) {
            openEqualizer()
        }

        SettingsItemsCard(R.string.clear_cache, cacheSize()) {

        }

        SettingsItemsCard(R.string.feedback) {
        }

        SettingsItemsCard(R.string.privacy_policy) {
        }

        Spacer(Modifier.height(35.dp))

        TextThin("Version ${BuildConfig.VERSION_NAME}", Modifier.padding(horizontal = 24.dp))

        Spacer(Modifier.height(150.dp))
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
fun LoopSettings() {
    val v by loopSettings.collectAsState(initial = false)
    SettingsLayout(R.string.play_song_on_loop) {
        SettingsItems(R.string.enable, v) {
            loopSettings = flowOf(true)
        }
        SettingsItems(R.string.disable, !v) {
            loopSettings = flowOf(false)
        }
    }
}

@Composable
fun AutoPlaySettings() {
    val v by autoplaySettings.collectAsState(initial = true)
    SettingsLayout(R.string.autoplay_next_song) {
        SettingsItems(R.string.enable, v) {
            autoplaySettings = flowOf(true)
        }
        SettingsItems(R.string.disable, !v) {
            autoplaySettings = flowOf(false)
        }
    }
}

@Composable
fun OfflineDownloadSettings() {
    val v by doOfflineDownloadWifiSettings.collectAsState(initial = false)
    SettingsLayout(R.string.offline_download) {
        SettingsItems(R.string.wifi_mobile_data, !v) {
            doOfflineDownloadWifiSettings = flowOf(false)
        }
        SettingsItems(R.string.only_connect_to_wifi, v) {
            doOfflineDownloadWifiSettings = flowOf(true)
        }
    }
}

@Composable
fun SongOnLockScreenSettings() {
    val v by showPlayingSongOnLockScreenSettings.collectAsState(initial = false)
    SettingsLayout(R.string.show_playing_song_on_lock_screen) {
        SettingsItems(R.string.enable, v) {
            showPlayingSongOnLockScreenSettings = flowOf(true)
        }
        SettingsItems(R.string.disable, !v) {
            showPlayingSongOnLockScreenSettings = flowOf(false)
        }
    }
}


@Composable
fun SetWallpaperSettings() {
    val v by setWallpaperSettings.collectAsState(initial = SetWallpaperInfo.NONE.v)
    SettingsLayout(R.string.set_wallpaper_on_song_playing) {
        SettingsItems(R.string.none, v == SetWallpaperInfo.NONE.v) {
            setWallpaperSettings = flowOf(SetWallpaperInfo.NONE.v)
        }
        SettingsItems(R.string.artist_image, v == SetWallpaperInfo.ARTIST_IMAGE.v) {
            setWallpaperSettings = flowOf(SetWallpaperInfo.ARTIST_IMAGE.v)
        }
        SettingsItems(R.string.song_thumbnail, v == SetWallpaperInfo.SONG_THUMBNAIL.v) {
            setWallpaperSettings = flowOf(SetWallpaperInfo.SONG_THUMBNAIL.v)
        }
    }
}

@Composable
fun PlaySongWhenAlarmSettings() {
    val v by setWallpaperSettings.collectAsState(initial = SetWallpaperInfo.NONE.v)
    SettingsLayout(R.string.set_alarm_to_play_song) {
        SettingsItems(R.string.none, v == SetWallpaperInfo.NONE.v) {
            setWallpaperSettings = flowOf(SetWallpaperInfo.NONE.v)
        }
        SettingsItems(R.string.artist_image, v == SetWallpaperInfo.ARTIST_IMAGE.v) {
            setWallpaperSettings = flowOf(SetWallpaperInfo.ARTIST_IMAGE.v)
        }
        SettingsItems(R.string.song_thumbnail, v == SetWallpaperInfo.SONG_THUMBNAIL.v) {
            setWallpaperSettings = flowOf(SetWallpaperInfo.SONG_THUMBNAIL.v)
        }
    }
}

@Composable
fun PauseMusicOnHeadphoneDetachSettings(player: ExoPlayer) {
    val v by pauseMusicOnHeadphoneDetachSettings.collectAsState(initial = false)
    SettingsLayout(R.string.pause_on_headphone_detach) {
        SettingsItems(R.string.enable, v) {
            player.setHandleAudioBecomingNoisy(true)
            pauseMusicOnHeadphoneDetachSettings = flowOf(true)
        }
        SettingsItems(R.string.disable, !v) {
            player.setHandleAudioBecomingNoisy(false)
            pauseMusicOnHeadphoneDetachSettings = flowOf(false)
        }
    }
}

//create a video that superman is drinking coffie in a cafe, then he comes out any fly in the sky and 3 seconds his phone ring and he get an alert 'Dolphin tracker: You forgot you wallet' on his phone. The he fly back in that cafe and pick up his wallet.