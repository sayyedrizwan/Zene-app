package com.rizwansayyed.zene.ui.settings.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager.autoPausePlayerSettings
import com.rizwansayyed.zene.datastore.DataStorageManager.pauseHistorySettings
import com.rizwansayyed.zene.datastore.DataStorageManager.smoothSongTransitionSettings
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun SettingsPlaybackView() {
    val autoPausePlayer by autoPausePlayerSettings.collectAsState(false)
    val smoothSongTransition by smoothSongTransitionSettings.collectAsState(false)
    val pauseHistory by pauseHistorySettings.collectAsState(false)

    val coroutine = rememberCoroutineScope()

    Box(Modifier.padding(horizontal = 6.dp)) {
        TextViewBold(stringResource(R.string.playback_control), 18)
    }

    Spacer(Modifier.height(13.dp))

    SettingsSwitchView(autoPausePlayer, R.string.auto_pause_song, R.string.auto_pause_song_desc) {
        coroutine.launch { autoPausePlayerSettings = flowOf(it) }
    }

    SettingsSwitchView(
        smoothSongTransition, R.string.smooth_song_transition, R.string.smooth_song_transition_desc
    ) {
        coroutine.launch { smoothSongTransitionSettings = flowOf(it) }
    }

    SettingsSwitchView(pauseHistory, R.string.pause_history, R.string.pause_history_desc) {
        coroutine.launch { pauseHistorySettings = flowOf(it) }
    }
}


@Composable
fun SettingsSwitchView(checked: Boolean, title: Int, desc: Int, change: (Boolean) -> Unit) {
    Row(Modifier
        .fillMaxWidth()
        .clickable(indication = null,
            interactionSource = remember { MutableInteractionSource() }) { }
        .padding(horizontal = 5.dp, vertical = 15.dp),
        Arrangement.Center,
        Alignment.CenterVertically) {
        Spacer(Modifier.width(5.dp))
        Column(Modifier.weight(1f)) {
            TextViewNormal(stringResource(title), 18, line = 1)
            TextViewLight(stringResource(desc), 14)
        }

        Switch(
            checked = checked, onCheckedChange = change, colors = SwitchDefaults.colors(
                checkedThumbColor = MainColor,
                checkedTrackColor = Color.White,
                uncheckedThumbColor = MainColor,
                uncheckedTrackColor = Color.Gray,
            )
        )
    }
}
