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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
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
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.service.notification.NavigationUtils.NAV_MAIN_PAGE
import com.rizwansayyed.zene.service.notification.NavigationUtils.triggerHomeNav
import com.rizwansayyed.zene.ui.settings.dialog.SettingsShareSheetView
import com.rizwansayyed.zene.ui.settings.dialog.SettingsStorageSheetView
import com.rizwansayyed.zene.ui.settings.importplaylists.ImportPlaylistsActivity
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonHeavy
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.clearImagesCache
import com.rizwansayyed.zene.utils.MainUtils.openFeedbackMail
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_FAQ_URL
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_HOME
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_PRIVACY_POLICY_URL
import com.rizwansayyed.zene.utils.share.MediaContentUtils
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch


@Composable
fun SettingsExtraView() {
    val context = LocalActivity.current
    var showStorageSheet by remember { mutableStateOf(false) }
    var feedbackSheet by remember { mutableStateOf(false) }
    var rateUsSheet by remember { mutableStateOf(false) }
    var logoutSheet by remember { mutableStateOf(false) }

    Spacer(Modifier.height(13.dp))

    SettingsExtraView(R.string.earphone_tracker_finder, R.drawable.ic_headphones) {

    }

    SettingsExtraView(R.string.import_playlists, R.drawable.ic_playlist) {
        Intent(context, ImportPlaylistsActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(this)
        }
    }

    SettingsExtraView(R.string.equalizer, R.drawable.ic_setting_equlizer) {
        val audioManager = context!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val sessionId = audioManager.generateAudioSessionId()
        try {
            val effects = Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL)
            effects.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, context.packageName)
            effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, sessionId)
            effects.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC)
            context.startActivityForResult(effects, 0)
        } catch (notFound: ActivityNotFoundException) {
            "There is no default equalizer".toast()
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
        feedbackSheet = true
    }

    val shareDesc = stringResource(R.string.zene_share_desc)
    SettingsExtraView(R.string.share_app, R.drawable.ic_share) {
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "$shareDesc \n${ZENE_HOME}")
            type = "text/plain"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(this)
        }
    }

    SettingsExtraView(R.string.rate_us, R.drawable.ic_star) {
        rateUsSheet = true
    }

    SettingsExtraView(R.string.privacy_policy, R.drawable.ic_policy) {
        MediaContentUtils.openCustomBrowser(ZENE_PRIVACY_POLICY_URL)
    }

    SettingsExtraView(R.string.log_out, R.drawable.ic_logout) {
        logoutSheet = true
    }

    if (showStorageSheet) SettingsStorageSheetView {
        showStorageSheet = false
    }

    if (feedbackSheet) FeedbackAlertSheetView {
        feedbackSheet = false
    }

    if (rateUsSheet) SettingsShareSheetView {
        rateUsSheet = false
    }

    if (logoutSheet) LogoutSheetView {
        logoutSheet = false
    }
}


@Composable
fun SettingsExtraView(top: Int, img: Int, click: (() -> Unit)?) {
    Row(
        Modifier
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackAlertSheetView(close: () -> Unit) {
    ModalBottomSheet(
        close, Modifier.fillMaxWidth(), contentColor = MainColor, containerColor = MainColor
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 9.dp), Arrangement.Center, Alignment.Start
        ) {
            Spacer(Modifier.height(30.dp))
            TextViewBold(stringResource(R.string.have_suggestions_or_issues), 19)
            Spacer(Modifier.height(5.dp))
            TextViewNormal(stringResource(R.string.have_suggestions_or_issues_desc), 16)
            Spacer(Modifier.height(50.dp))

            ButtonHeavy(stringResource(R.string.send_feedback), Color.Black) {
                openFeedbackMail()
                close()
            }


            Spacer(Modifier.height(100.dp))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogoutSheetView(close: () -> Unit) {
    ModalBottomSheet(
        close, Modifier.fillMaxWidth(), contentColor = MainColor, containerColor = MainColor
    ) {
        val context = LocalContext.current
        val coroutine = rememberCoroutineScope()
        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 9.dp), Arrangement.Center, Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(30.dp))
            TextViewBold(stringResource(R.string.are_you_sure_want_to_log_out), 19)
            Spacer(Modifier.height(50.dp))

            ButtonHeavy(stringResource(R.string.log_out), Color.Black) {
                context.cacheDir.deleteRecursively()
                clearImagesCache()
                close()
                coroutine.launch {
                    triggerHomeNav(NAV_MAIN_PAGE)
                    DataStorageManager.userInfo = flowOf(null)
                }
            }


            Spacer(Modifier.height(100.dp))
        }
    }
}


