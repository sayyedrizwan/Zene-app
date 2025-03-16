package com.rizwansayyed.zene.ui.settings.view

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.audiofx.AudioEffect
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_FAQ_URL
import com.rizwansayyed.zene.utils.share.MediaContentUtils


@Composable
fun SettingsExtraView() {
    val context = LocalActivity.current
    var showStorageSheet by remember { mutableStateOf(false) }

    Spacer(Modifier.height(13.dp))

    SettingsExtraView(R.string.earphone_tracker_finder, R.drawable.ic_headphones) {

    }

    SettingsExtraView(R.string.equalizer, R.drawable.ic_setting_equlizer) {
        val audioManager = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val sessionId = audioManager.generateAudioSessionId()
        if (sessionId == AudioEffect.ERROR_BAD_VALUE) {
            "No Session Id".toast()
        } else {
            try {
                val effects = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
                effects.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, "your app package name")
                effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, sessionId)
                effects.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
                context.startActivityForResult(effects, 0)
            } catch (notFound: ActivityNotFoundException) {
                "There is no equalizer".toast()
            }
        }
    }

    Spacer(Modifier.height(25.dp))

    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
        Spacer(
            Modifier
                .fillMaxWidth(0.7f)
                .height(0.5.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White)

        )
    }


    Spacer(Modifier.height(25.dp))

    SettingsExtraView(R.string.help_faq, R.drawable.ic_help_circle) {
        MediaContentUtils.openCustomBrowser(ZENE_FAQ_URL)
    }

    SettingsExtraView(R.string.storage, R.drawable.ic_folder) {
        showStorageSheet = true
    }

    SettingsExtraView(R.string.feedback, R.drawable.ic_mailbox) {

    }

    SettingsExtraView(R.string.share_app, R.drawable.ic_share) {

    }

    SettingsExtraView(R.string.rate_us, R.drawable.ic_star) {

    }

    SettingsExtraView(R.string.log_out, R.drawable.ic_logout) {

    }


    if (showStorageSheet) SettingsStorageSheetView {
        showStorageSheet = false
    }
}


@Composable
fun SettingsExtraView(top: Int, img: Int, click: (() -> Unit)?) {
    Row(Modifier
        .fillMaxWidth()
        .clickable(
            indication = null,
            interactionSource = remember { MutableInteractionSource() }) { if (click != null) click() }
        .padding(horizontal = 5.dp, vertical = 20.dp),
        Arrangement.Center,
        Alignment.CenterVertically) {
        Spacer(Modifier.width(5.dp))
        ImageIcon(img, 24)

        Spacer(Modifier.width(10.dp))
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 1.dp)
        ) {
            TextViewNormal(stringResource(top), 16, line = 1)
        }

        if (click != null) Row(Modifier.rotate(-90f)) {
            ImageIcon(R.drawable.ic_arrow_down, 26)
        }
    }
}
