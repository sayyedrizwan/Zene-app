package com.rizwansayyed.zene.ui.settings

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.rizwansayyed.zene.BuildConfig
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.db.DataStoreManager.musicAutoplaySettings
import com.rizwansayyed.zene.data.db.DataStoreManager.musicLoopSettings
import com.rizwansayyed.zene.data.db.DataStoreManager.musicPlayerDB
import com.rizwansayyed.zene.data.db.DataStoreManager.playingSongOnLockScreen
import com.rizwansayyed.zene.data.db.DataStoreManager.songQualityDB
import com.rizwansayyed.zene.service.MusicServiceUtils.sendWebViewCommand
import com.rizwansayyed.zene.ui.settings.model.SongQualityTypes
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.view.AlertDialogView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.shareUrl
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.URLS.PRIVACY_POLICY
import com.rizwansayyed.zene.utils.Utils.feedbackMail
import com.rizwansayyed.zene.utils.Utils.openBrowser
import com.rizwansayyed.zene.utils.Utils.openEqualizer
import com.rizwansayyed.zene.utils.Utils.shareTxtImage
import com.rizwansayyed.zene.utils.Utils.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds


data class SettingsData(val txt: Int, val value: Boolean)

val loopSong = listOf(SettingsData(R.string.enable, true), SettingsData(R.string.disabled, false))


@Composable
fun SettingsView() {
    val loopSettings by musicLoopSettings.collectAsState(initial = false)
    val autoplaySettings by musicAutoplaySettings.collectAsState(initial = true)
    val lockscreenSettings by playingSongOnLockScreen.collectAsState(initial = false)
    var clearCache by remember { mutableStateOf(false) }

    val context = LocalContext.current as Activity
    val needPermission = stringResource(R.string.need_overlay_permission_to_show_song)

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
            .padding(vertical = 10.dp, horizontal = 10.dp)
    ) {
        item {
            Spacer(Modifier.height(60.dp))
            TextPoppins(stringResource(R.string.settings), size = 40)
            Spacer(Modifier.height(30.dp))
        }

        item {
            Spacer(Modifier.height(30.dp))
            SongQualitySettings()
        }

        item {
            Spacer(Modifier.height(30.dp))
            SettingsCardViews(R.string.loop_same_song, loopSong, loopSettings) {
                logEvents(FirebaseLogEvents.FirebaseEvents.MUSIC_LOOP_SETTINGS)
                musicLoopSettings = flowOf(it)
            }
        }

        item {
            Spacer(Modifier.height(30.dp))
            SettingsCardViews(R.string.autoplay_next_song, loopSong, autoplaySettings) {
                logEvents(FirebaseLogEvents.FirebaseEvents.AUTOPLAY_NEXT_SONG_SETTINGS)
                musicAutoplaySettings = flowOf(it)
            }
        }

        item {
            Spacer(Modifier.height(30.dp))
            SettingsCardViews(
                R.string.show_play_song_on_lock_screen, loopSong, lockscreenSettings
            ) {
                if (!Settings.canDrawOverlays(context)) {
                    playingSongOnLockScreen = flowOf(false)

                    val packageName = "package:${context.packageName}"
                    val intent =
                        Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse(packageName))
                    intent.flags = FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent)

                    needPermission.toast()

                    return@SettingsCardViews
                }
                playingSongOnLockScreen = flowOf(it)
            }
        }

        item {
            Spacer(Modifier.height(30.dp))
            CardItems(R.string.equalizer) {
                logEvents(FirebaseLogEvents.FirebaseEvents.OPEN_EQUALIZER)
                openEqualizer()
            }
        }

        item {
            Spacer(Modifier.height(30.dp))
            CardItems(R.string.clear_cache) {
                clearCache = true
            }
        }

        item {
            Spacer(Modifier.height(30.dp))
            CardItems(R.string.feedback) {
                feedbackMail()
            }
        }

        item {
            Spacer(Modifier.height(30.dp))
            CardItems(R.string.privacy_policy) {
                shareTxtImage(PRIVACY_POLICY, "Open URL on Browser")
            }
        }

        item {
            Spacer(Modifier.height(60.dp))
            TextPoppins("version ${BuildConfig.VERSION_NAME}", size = 14)
        }

        item {
            Spacer(Modifier.height(210.dp))
        }
    }

    val clearCacheDone = stringResource(R.string.clear_cache_successfully)

    if (clearCache) AlertDialogView(
        R.string.clear_cache, R.string.clear_cache_desc, R.string.clear,
    ) {
        clearCache = false
        context.cacheDir.deleteRecursively()
        clearCacheDone.toast()
    }

    LaunchedEffect(Unit) {
        if (!Settings.canDrawOverlays(context)) playingSongOnLockScreen = flowOf(false)
    }
}

@Composable
fun SongQualitySettings() {
    val coroutines = rememberCoroutineScope()
    val songQuality by songQualityDB.collectAsState(initial = SongQualityTypes.HIGH_QUALITY)
    val songRestarted = stringResource(R.string.song_quality_updated_successfully)

    Spacer(Modifier.height(40.dp))
    TextPoppinsSemiBold(stringResource(R.string.song_quality), size = 18)
    Spacer(Modifier.height(16.dp))

    fun updatedQuality(songQualityTypes: SongQualityTypes) = coroutines.launch(Dispatchers.IO) {
        songQualityDB = flowOf(songQualityTypes)

        delay(1.seconds)
        val songInfo = musicPlayerDB.first()
        if (songInfo?.player?.id != null) {
            songRestarted.toast()
            sendWebViewCommand(songInfo.player, songInfo.list ?: listOf(songInfo.player))
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .padding(horizontal = 12.dp, vertical = 22.dp)
    ) {
        SongQualityTypes.entries.forEach {
            Row(
                Modifier
                    .padding(horizontal = 10.dp, vertical = 22.dp)
                    .clickable { updatedQuality(it) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextPoppins(stringResource(it.txt), size = 14)
                Spacer(Modifier.weight(1f))
                if (songQuality == it) ImageIcon(R.drawable.ic_tick, 24)
            }
        }
    }
}

@Composable
fun CardItems(txt: Int, click: () -> Unit) {
    Row(
        Modifier
            .padding(vertical = 22.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { click() }
            .background(Color.Black)
            .padding(horizontal = 19.dp, vertical = 22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextPoppins(stringResource(txt), size = 14)

        Spacer(Modifier.weight(1f))

        Box(Modifier.rotate(180f)) {
            ImageIcon(R.drawable.ic_arrow_left, 24)
        }
    }
}


@Composable
fun SettingsCardViews(
    txt: Int, list: List<SettingsData>, settings: Boolean, click: (Boolean) -> Unit
) {
    Spacer(Modifier.height(40.dp))
    TextPoppinsSemiBold(stringResource(txt), size = 18)
    Spacer(Modifier.height(16.dp))

    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black)
            .padding(horizontal = 12.dp, vertical = 22.dp)
    ) {
        list.forEach {
            Row(
                Modifier
                    .padding(horizontal = 10.dp, vertical = 22.dp)
                    .clickable { click(it.value) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextPoppins(stringResource(it.txt), size = 14)
                Spacer(Modifier.weight(1f))
                if (settings == it.value) ImageIcon(R.drawable.ic_tick, 24)
            }
        }
    }
}