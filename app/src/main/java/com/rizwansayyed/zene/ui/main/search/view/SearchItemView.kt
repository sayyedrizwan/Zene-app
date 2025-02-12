package com.rizwansayyed.zene.ui.main.search.view

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL
import android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.toast
import java.util.Locale


@Composable
fun TrendingItemView(data: ZeneMusicData?, click: () -> Unit) {
    data?.name?.let {
        Row(Modifier
            .padding(horizontal = 6.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Black)
            .clickable { click() }
            .padding(horizontal = 14.dp, vertical = 6.dp),
            Arrangement.Center,
            Alignment.CenterVertically) {
            ImageIcon(R.drawable.ic_chart_line, 15)
            Spacer(Modifier.width(5.dp))
            TextViewNormal(it, 14)
        }
    }
}

@Composable
fun SearchKeywordsItemView(text: String, click: (Boolean) -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 14.dp, vertical = 6.dp)
            .clickable { click(true) },
        Arrangement.Center,
        Alignment.CenterVertically
    ) {
        Box(Modifier.weight(1f)) {
            TextViewNormal(text, 16, line = 1)
        }
        Box(Modifier
            .rotate(-50f)
            .clickable { click(false) }) {
            ImageIcon(R.drawable.ic_arrow_right, 17)
        }
    }
}

@Composable
fun SearchTopView(search: (String) -> Unit) {
    val configuration = LocalConfiguration.current.screenHeightDp.dp

    val speakPrompt = stringResource(R.string.speak_what_you_want_to_search)
    val errorRecognizingSpeech = stringResource(R.string.error_recognizing_speech)
    val s = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val data = it.data
            val matches = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            search(matches?.firstOrNull() ?: "")
        } else errorRecognizingSpeech.toast()
    }

    var songRecognitionAlert by remember { mutableStateOf(false) }

    Spacer(Modifier.height(70.dp))

    Row(
        Modifier.padding(horizontal = 9.dp), Arrangement.Center, Alignment.CenterVertically
    ) {
        TextViewBold(stringResource(R.string.search), 24)
        Spacer(Modifier.weight(1f))
        Box(Modifier.clickable { songRecognitionAlert = true }) {
            ImageIcon(R.drawable.ic_voice_id, 25)
        }
        Spacer(Modifier.width(9.dp))
        Box(Modifier.clickable {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(EXTRA_LANGUAGE_MODEL, LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_PROMPT, speakPrompt)
            }
            s.launch(intent)
        }) {
            ImageIcon(R.drawable.ic_mic, 25)
        }
    }

    DisposableEffect(Unit) {
        onDispose { }
    }
    if (songRecognitionAlert) Dialog(
        { songRecognitionAlert = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .height(configuration / 2)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black)
        ) {
            SearchSongRecognition()
        }
    }

}

