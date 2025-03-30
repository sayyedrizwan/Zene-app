package com.rizwansayyed.zene.ui.partycall.view

import android.content.ContentResolver
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.view.ImageWithBgRound
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal

@Composable
fun CallingView(modifier: Modifier = Modifier, name: String) {
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
            ImageWithBgRound(R.drawable.ic_speaker_full, Color.White, Color.Black) {

            }

            ImageWithBgRound(R.drawable.ic_call_end, Color.Red, Color.White) {

            }
        }
    }
}

fun playRingtoneFromEarpiece(context: Context) {

    val ringtoneUri =
        (ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.ringing_call_song).toUri()

    val mediaPlayer = MediaPlayer()
    mediaPlayer.setAudioAttributes(
        AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)

//            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
    )
    mediaPlayer.setDataSource(context, ringtoneUri)
    mediaPlayer.prepare()
    mediaPlayer.start()

    mediaPlayer.setOnCompletionListener { it.release() }
}