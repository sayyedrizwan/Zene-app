package com.rizwansayyed.zene.presenter.ui.home.views

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.content.Intent.ACTION_SENDTO
import android.content.Intent.EXTRA_EMAIL
import android.content.Intent.EXTRA_SUBJECT
import android.content.Intent.EXTRA_TEXT
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.PlaybackParameters
import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.alarmSongData
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.alarmTimeSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.autoplaySettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.doOfflineDownloadWifiSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.loopSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.offlineSongsSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.setWallpaperSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.showPlayingSongOnLockScreenSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.songQualitySettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.songSpeedSettings
import com.rizwansayyed.zene.data.db.datastore.DataStorageSettingsManager.standByModeSettings
import com.rizwansayyed.zene.data.db.datastore.OfflineSongsInfo
import com.rizwansayyed.zene.data.db.datastore.SetWallpaperInfo
import com.rizwansayyed.zene.data.db.datastore.SongSpeed
import com.rizwansayyed.zene.data.db.datastore.SongsQualityInfo
import com.rizwansayyed.zene.data.db.datastore.TIME_ALARM
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.GlobalNativeFullAds
import com.rizwansayyed.zene.presenter.ui.SearchEditTextView
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.home.online.LoadingAlbumsCards
import com.rizwansayyed.zene.presenter.ui.home.online.SongsExploreItems
import com.rizwansayyed.zene.presenter.ui.home.settings.SettingsItems
import com.rizwansayyed.zene.presenter.ui.home.settings.SettingsItemsCard
import com.rizwansayyed.zene.presenter.ui.home.settings.SettingsItemsText
import com.rizwansayyed.zene.presenter.ui.home.settings.SettingsLayout
import com.rizwansayyed.zene.presenter.util.UiUtils
import com.rizwansayyed.zene.presenter.util.UiUtils.formatSingleTimeToView
import com.rizwansayyed.zene.presenter.util.UiUtils.otherPermissionIntent
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.service.alarm.AlarmManagerToPlaySong
import com.rizwansayyed.zene.service.player.utils.Utils.openEqualizer
import com.rizwansayyed.zene.service.workmanager.StandByOnChargingWorkManager.Companion.startStandbyModeWorkManager
import com.rizwansayyed.zene.utils.Utils.OFFICIAL_EMAIL
import com.rizwansayyed.zene.utils.Utils.cacheSize
import com.rizwansayyed.zene.utils.Utils.isPermission
import com.rizwansayyed.zene.utils.Utils.restartApp
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import com.rizwansayyed.zene.viewmodel.RoomDbViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


@Composable
fun SettingsView(player: ExoPlayer, alarmManagerToPlaySong: AlarmManagerToPlaySong) {
    val context = LocalContext.current.applicationContext
    var cacheDialog by remember { mutableStateOf(false) }

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

        SongSpeedSettings(player)

        LoopSettings()

        AutoPlaySettings()

        OfflineDownloadSettings()

        SongOnLockScreenSettings()

        SetWallpaperSettings()

        GlobalNativeFullAds()

        SetStandbySettings()

        PlaySongWhenAlarmSettings(alarmManagerToPlaySong)

        Spacer(Modifier.height(20.dp))

        SettingsItemsCard(R.string.equalizer) {
            openEqualizer()
        }

        SettingsItemsCard(R.string.clear_cache, cacheSize()) {
            cacheDialog = true
        }

        SettingsItemsCard(R.string.feedback) {
            val selectorIntent = Intent(ACTION_SENDTO)
                .setData("mailto:$OFFICIAL_EMAIL".toUri())

            val emailIntent = Intent(ACTION_SEND).apply {
                putExtra(EXTRA_EMAIL, arrayOf(OFFICIAL_EMAIL))
                putExtra(EXTRA_SUBJECT, "Zene Feedback")
                putExtra(EXTRA_TEXT, "<<<<<< Please write feedback >>>>>>")
                selector = selectorIntent
            }
            context.startActivity(Intent.createChooser(emailIntent, "Send feedback from").apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        }

        SettingsItemsCard(R.string.privacy_policy) {
        }

        Spacer(Modifier.height(35.dp))

        TextThin("Version ${BuildConfig.VERSION_NAME}", Modifier.padding(horizontal = 24.dp))

        GlobalNativeFullAds()

        Spacer(Modifier.height(150.dp))
    }

    if (cacheDialog)
        ClearCacheDialog {
            if (it) CoroutineScope(Dispatchers.IO).launch {
                context.cacheDir.deleteRecursively()

                delay(2.seconds)
                restartApp()
            }

            cacheDialog = false
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
fun SongSpeedSettings(player: ExoPlayer) {
    val v by songSpeedSettings.collectAsState(initial = SongSpeed.ONE.v)
    SettingsLayout(R.string.song_playback_speed) {
        SettingsItems(R.string.zero_two_five, v == SongSpeed.ZERO_TWO_FIVE.v) {
            songSpeedSettings = flowOf(SongSpeed.ZERO_TWO_FIVE.v)
            player.playbackParameters = PlaybackParameters(0.25f)
        }
        SettingsItems(R.string.zero_five, v == SongSpeed.ZERO_FIVE.v) {
            songSpeedSettings = flowOf(SongSpeed.ZERO_FIVE.v)
            player.playbackParameters = PlaybackParameters(0.5f)
        }
        SettingsItems(R.string.zero_seven_five, v == SongSpeed.ZERO_SEVEN_FIVE.v) {
            songSpeedSettings = flowOf(SongSpeed.ZERO_SEVEN_FIVE.v)
            player.playbackParameters = PlaybackParameters(0.75f)
        }
        SettingsItems(R.string.one_zero, v == SongSpeed.ONE.v) {
            songSpeedSettings = flowOf(SongSpeed.ONE.v)
            player.playbackParameters = PlaybackParameters(1f)
        }
        SettingsItems(R.string.one_two_five, v == SongSpeed.ONE_TWO_FIVE.v) {
            songSpeedSettings = flowOf(SongSpeed.ONE_TWO_FIVE.v)
            player.playbackParameters = PlaybackParameters(1.25f)
        }
        SettingsItems(R.string.one_five, v == SongSpeed.ONE_FIVE.v) {
            songSpeedSettings = flowOf(SongSpeed.ONE_FIVE.v)
            player.playbackParameters = PlaybackParameters(1.5f)
        }
        SettingsItems(R.string.one_seven_five, v == SongSpeed.ONE_SEVEN_FIVE.v) {
            songSpeedSettings = flowOf(SongSpeed.ONE_SEVEN_FIVE.v)
            player.playbackParameters = PlaybackParameters(1.75f)
        }
        SettingsItems(R.string.two_zero, v == SongSpeed.TWO.v) {
            songSpeedSettings = flowOf(SongSpeed.TWO.v)
            player.playbackParameters = PlaybackParameters(2.0f)
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
    val roomDb: RoomDbViewModel = hiltViewModel()
    val v by doOfflineDownloadWifiSettings.collectAsState(initial = false)
    SettingsLayout(R.string.offline_download) {
        SettingsItems(R.string.wifi_mobile_data, !v) {
            doOfflineDownloadWifiSettings = flowOf(false)
            roomDb.downloadIfNotDownloaded()
        }
        SettingsItems(R.string.only_connect_to_wifi, v) {
            doOfflineDownloadWifiSettings = flowOf(true)
            roomDb.downloadIfNotDownloaded()
        }
    }
}

@Composable
fun SongOnLockScreenSettings() {
    val context = LocalContext.current.applicationContext

    val permissionString = stringResource(R.string.lock_screen_permission_to_show)

    val permission =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Settings.canDrawOverlays(context)) {
                CoroutineScope(Dispatchers.IO).launch {
                    delay(1.seconds)
                    otherPermissionIntent()
                }
                showPlayingSongOnLockScreenSettings = flowOf(true)
            }
        }

    val v by showPlayingSongOnLockScreenSettings.collectAsState(initial = false)
    SettingsLayout(R.string.show_playing_song_on_lock_screen) {
        SettingsItems(R.string.enable, v) {
            if (Settings.canDrawOverlays(context))
                showPlayingSongOnLockScreenSettings = flowOf(true)
            else {
                permissionString.toast()
                permission.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
            }
        }
        SettingsItems(R.string.disable, !v) {
            showPlayingSongOnLockScreenSettings = flowOf(false)
        }
    }

    LaunchedEffect(Unit) {
        if (!Settings.canDrawOverlays(context))
            showPlayingSongOnLockScreenSettings = flowOf(false)
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
fun SetStandbySettings() {
    val v by standByModeSettings.collectAsState(initial = false)

    val context = LocalContext.current.applicationContext

    val permissionString = stringResource(R.string.stand_by_permission_to_show)


    val permission =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Settings.canDrawOverlays(context)) {
                CoroutineScope(Dispatchers.IO).launch {
                    delay(1.seconds)
                    otherPermissionIntent()
                }

                standByModeSettings = flowOf(true)
            }
        }

    SettingsLayout(R.string.stand_by_mode_while_charging) {
        SettingsItems(R.string.enable, v) {
            if (Settings.canDrawOverlays(context))
                standByModeSettings = flowOf(true)
            else {
                permissionString.toast()
                permission.launch(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION))
            }
        }
        SettingsItems(R.string.disable, !v) {
            standByModeSettings = flowOf(false)
        }
    }


    LaunchedEffect(Unit) {
        if (!Settings.canDrawOverlays(context))
            standByModeSettings = flowOf(false)
    }
}

@Composable
fun PlaySongWhenAlarmSettings(alarmManagerToPlaySong: AlarmManagerToPlaySong) {
    var showDialog by remember { mutableStateOf(false) }
    var songSelectDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    val noneText = stringResource(id = R.string.none)
    val currentSongInfo by alarmSongData.collectAsState(initial = null)

    val permission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                alarmManagerToPlaySong.startAlarmIfThere()
            }
        }

    val v by alarmTimeSettings.collectAsState(initial = TIME_ALARM)
    SettingsLayout(R.string.set_alarm_to_play_song) {
        SettingsItems(R.string.disable, v == TIME_ALARM) {
            alarmTimeSettings = flowOf(TIME_ALARM)
        }
        SettingsItemsText(R.string.select_alarm_time, formatSingleTimeToView(v)) {
            showDialog = true
        }
        SettingsItemsText(R.string.alarm_song, name) {
            songSelectDialog = true
        }
    }

    if (showDialog) TimePickerDialog {
        showDialog = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            if (!isPermission(Manifest.permission.SCHEDULE_EXACT_ALARM))
                permission.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
            else
                alarmManagerToPlaySong.startAlarmIfThere()
        else
            alarmManagerToPlaySong.startAlarmIfThere()
    }

    if (songSelectDialog)
        SearchSongBottomSheet(currentSongInfo) {
            songSelectDialog = false
        }

    LaunchedEffect(currentSongInfo?.name) {
        name = if ((currentSongInfo?.name?.trim()?.length ?: 0) > 2)
            if ((currentSongInfo?.name?.trim()?.length ?: 0) > 12)
                currentSongInfo?.name?.substring(0, 12) ?: "" else currentSongInfo?.name ?: ""
        else
            noneText
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            if (isPermission(Manifest.permission.SCHEDULE_EXACT_ALARM))
                alarmTimeSettings = flowOf(TIME_ALARM)

    }
}


@Composable
fun ClearCacheDialog(run: (Boolean) -> Unit) {
    AlertDialog(
        title = {
            TextRegular(stringResource(R.string.clear_cache))
        },
        text = {
            TextThin(stringResource(R.string.clear_cache_sure))
        },
        onDismissRequest = {
            run(false)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    run(true)
                }
            ) {
                TextSemiBold(stringResource(R.string.clear))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    run(false)
                }
            ) {
                TextSemiBold(stringResource(R.string.cancel))
            }
        },
        containerColor = MainColor
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(close: () -> Unit) {
    val selectedHour by remember { mutableIntStateOf(0) }
    val selectedMinute by remember { mutableIntStateOf(0) }
    val timePickerState =
        rememberTimePickerState(selectedHour, selectedMinute, true)

    AlertDialog(
        close, Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(size = 12.dp))
    ) {
        Column(
            Modifier
                .background(androidx.compose.ui.graphics.Color.LightGray.copy(alpha = 0.3f))
                .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 12.dp),
            Arrangement.Center, Alignment.CenterHorizontally
        ) {
            TimePicker(timePickerState)

            Row(
                Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(), Arrangement.End
            ) {
                TextButton(onClick = close) {
                    TextRegular(stringResource(R.string.close))
                }

                TextButton({
                    alarmTimeSettings =
                        flowOf("${timePickerState.hour} : ${timePickerState.minute}")
                    close()
                }) {
                    TextRegular(stringResource(R.string.confirm))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchSongBottomSheet(currentSongInfo: MusicData?, close: () -> Unit) {
    val homeApiViewModel: HomeApiViewModel = hiltViewModel()
    val sheetState = rememberModalBottomSheetState(true)
    val searchSong = stringResource(id = R.string.search_the_song_you_want_to_play)
    val currentSongSetDesc = stringResource(id = R.string.song_is_already_set_as_alarm)
    var text by remember { mutableStateOf("") }
    var searchJob by remember { mutableStateOf<Job?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current


    ModalBottomSheet(
        close, Modifier, sheetState,
        containerColor = MainColor, contentColor = BlackColor
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchEditTextView(searchSong, text, null, {
                text = it
                searchJob?.cancel()
                if (it.length <= 3) return@SearchEditTextView

                searchJob = CoroutineScope(Dispatchers.IO).launch {
                    delay(1.seconds)
                    homeApiViewModel.searchData(text)
                }
            }, {
                keyboardController?.hide()
            })

            Spacer(Modifier.height(40.dp))

            if (text.trim().isEmpty()) {
                Spacer(Modifier.height(80.dp))

                if ((currentSongInfo?.name?.trim()?.length ?: 0) > 2) {
                    TextRegular(
                        v = String.format(currentSongSetDesc, currentSongInfo?.name),
                        doCenter = true
                    )

                    Spacer(Modifier.height(20.dp))

                    Button(onClick = {
                        alarmSongData = flowOf(null)
                        close()
                    }, colors = ButtonDefaults.buttonColors(BlackColor)) {
                        TextThin(v = stringResource(R.string.remove_current_song))
                    }

                } else
                    TextRegular(v = stringResource(R.string.no_alarm_is_set_alarm), doCenter = true)

                Spacer(Modifier.height(100.dp))
            }

            LazyRow(Modifier.fillMaxWidth()) {
                when (val v = homeApiViewModel.searchData) {
                    DataResponse.Empty -> {}
                    is DataResponse.Error -> {}
                    DataResponse.Loading -> items(30) {
                        LoadingAlbumsCards()
                    }

                    is DataResponse.Success ->
                        if ((v.item?.songs ?: emptyList()).isEmpty())
                            item {
                                TextRegular(v = stringResource(id = R.string.no_song_found))
                            }
                        else
                            items(v.item?.songs ?: emptyList()) {
                                SongsExploreItems(it) {
                                    alarmSongData = flowOf(it)
                                    close()
                                }
                            }
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}