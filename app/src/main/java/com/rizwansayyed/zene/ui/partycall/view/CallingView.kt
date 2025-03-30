package com.rizwansayyed.zene.ui.partycall.view

import android.content.ContentResolver
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.ImageWithBgRound
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal


@Composable
fun CallingView(modifier: Modifier = Modifier, name: String, isSpeaker: MutableState<Boolean>) {
    val context = LocalContext.current.applicationContext
    val activity = LocalActivity.current
    Column(
        modifier
            .padding(bottom = 100.dp)
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.Black)
            .padding(horizontal = 10.dp, vertical = 30.dp),
        Arrangement.Center, Alignment.CenterHorizontally
    ) {
        TextViewBold(name, 25)
        Spacer(Modifier.height(5.dp))
        TextViewNormal(stringResource(R.string.calling_), 15)
        Spacer(Modifier.height(20.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceAround, Alignment.CenterVertically) {
            ImageWithBgRound(
                if (isSpeaker.value) R.drawable.ic_speaker_full else R.drawable.ic_volume_off,
                Color.White, Color.Black
            ) {
                isSpeaker.value = !isSpeaker.value
                playRingtoneFromEarpiece(context, isSpeaker.value)
            }

            ImageWithBgRound(R.drawable.ic_call_end, Color.Red, Color.White) {
                stopRingtoneFromEarpiece()
                activity?.finish()
            }
        }
    }
}

val mediaPlayerEarpiece = MediaPlayer().apply {
    isLooping = true
}

fun stopRingtoneFromEarpiece() {
    mediaPlayerEarpiece.stop()
    mediaPlayerEarpiece.reset()
}

fun playRingtoneFromEarpiece(context: Context, isSpeaker: Boolean) {
    val ringtoneUri =
        (ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.ringing_call_song).toUri()

    val currentPosition = mediaPlayerEarpiece.currentPosition
    mediaPlayerEarpiece.stop()
    mediaPlayerEarpiece.reset()

    mediaPlayerEarpiece.setDataSource(context, ringtoneUri)

    if (isSpeaker)
        mediaPlayerEarpiece.setAudioStreamType(AudioManager.STREAM_MUSIC)
    else
        mediaPlayerEarpiece.setAudioStreamType(AudioManager.STREAM_VOICE_CALL)

    mediaPlayerEarpiece.prepare()
    mediaPlayerEarpiece.seekTo(currentPosition)
    mediaPlayerEarpiece.start()
    mediaPlayerEarpiece.isLooping = true
}