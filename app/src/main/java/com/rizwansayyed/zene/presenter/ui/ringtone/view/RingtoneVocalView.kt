package com.rizwansayyed.zene.presenter.ui.ringtone.view


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle.Event.*
import androidx.lifecycle.LifecycleEventObserver
import com.linc.audiowaveform.AudioWaveform
import com.linc.audiowaveform.model.AmplitudeType
import com.linc.audiowaveform.model.WaveformAlignment
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.utils.CacheFiles.demoRingtonePath
import com.rizwansayyed.zene.presenter.theme.GreyColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.presenter.ui.ringtone.util.Utils.ifSongGoingOutOfSlider
import com.rizwansayyed.zene.presenter.ui.ringtone.util.Utils.isRingtoneSongPlaying
import com.rizwansayyed.zene.presenter.ui.ringtone.util.Utils.pauseRingtoneSong
import com.rizwansayyed.zene.presenter.ui.ringtone.util.Utils.playOrPauseRingtoneSong
import com.rizwansayyed.zene.presenter.ui.ringtone.util.Utils.progressRingtoneSong
import com.rizwansayyed.zene.presenter.ui.ringtone.util.Utils.ringtonePlayer
import com.rizwansayyed.zene.presenter.ui.ringtone.util.Utils.setPlayerDurationDependOnSlider
import com.rizwansayyed.zene.presenter.ui.ringtone.util.Utils.startPlayingRingtoneSong
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import linc.com.amplituda.Amplituda
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RingtoneVocalView() {
    val context = LocalContext.current.applicationContext
    val coroutine = rememberCoroutineScope()
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    val width = LocalConfiguration.current.screenWidthDp.dp

    var playbackDuration by remember { mutableStateOf<Job?>(null) }

    val amplitudesList = remember { mutableStateListOf(0) }
    var progress by remember { mutableFloatStateOf(0F) }
    var isPlaying by remember { mutableStateOf(false) }
    var isStartThumb by remember { mutableStateOf(false) }

    var ringtoneSlider by remember { mutableStateOf(0f..30f) }

    Box(
        Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(vertical = 20.dp)
    ) {
        AudioWaveform(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 13.dp, vertical = 9.dp)
                .fillMaxWidth(),
            style = Fill,
            waveformAlignment = WaveformAlignment.Center,
            amplitudeType = AmplitudeType.Min,
            progressBrush = SolidColor(GreyColor),
            waveformBrush = SolidColor(MainColor),
            spikeWidth = 1.dp,
            spikePadding = 4.dp,
            spikeRadius = 4.dp,
            progress = progress,
            amplitudes = amplitudesList,
            onProgressChange = { progress = it },
            onProgressChangeFinished = {}
        )


        RangeSlider(
            ringtoneSlider,
            { range ->
                val startThumbActive = range.start != ringtoneSlider.start
                val endThumbActive = range.endInclusive != ringtoneSlider.endInclusive
                if (startThumbActive) {
                    Log.d("TAG", "RingtoneVocalView: run startThumb")
//                    if (range.endInclusive - range.start > 29) {
//                        val newEndInclusive = range.endInclusive - 1f
//                        ringtoneSlider = range.start..newEndInclusive
//                    }
                }

                if (endThumbActive) {
                    if (range.endInclusive - range.start > 29) {
                        val newStartInclusive = range.start + 1f
                        ringtoneSlider = newStartInclusive..range.endInclusive
                    }
                }
                if (range.endInclusive - range.start >= 30 || range.endInclusive - range.start <= 10) return@RangeSlider
                ringtoneSlider = range
            },
            Modifier
                .align(Alignment.Center)
                .width(width),
            true, 0f..100f,
            {
                setPlayerDurationDependOnSlider(ringtoneSlider)
            },
            SliderDefaults.colors(Color.Black),
            startThumb = {
                Image(
                    painterResource(R.drawable.ic_keyframe), "",
                    Modifier
                        .padding(0.dp)
                        .size(25.dp),
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            },
            endThumb = {
                Image(
                    painterResource(R.drawable.ic_keyframe), "",
                    Modifier
                        .padding(0.dp)
                        .size(25.dp),
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            },
            track = {
                SliderDefaults.Track(
                    it, Modifier.scale(scaleX = 1f, scaleY = 12f),
                    SliderDefaults.colors(
                        thumbColor = Color.Black,
                        activeTrackColor = Color.Gray.copy(0.2f),
                        inactiveTrackColor = Color.Transparent
                    )
                )
            },
        )
    }


    TextThin(v = ringtoneSlider.toString())

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 9.dp)
    ) {
        SmallIcons(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play) {
            playOrPauseRingtoneSong()
        }

        Spacer(Modifier.weight(1f))

        SmallIcons(R.drawable.ic_tick)
    }

    DisposableEffect(Unit) {
        coroutine.launch {
            val a = Amplituda(context).processAudio(demoRingtonePath).get()
            amplitudesList.addAll(a.amplitudesAsList())
            delay(1.seconds)
            startPlayingRingtoneSong()
        }

        val lifecycleValue = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                ON_PAUSE -> pauseRingtoneSong()
                else -> {}
            }
        }

        playbackDuration = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                progress = progressRingtoneSong()
                isPlaying = isRingtoneSongPlaying()
                ifSongGoingOutOfSlider(ringtoneSlider)
                delay(1.seconds)
            }
        }

        lifecycleValue.addObserver(observer)
        onDispose {
            pauseRingtoneSong()
            playbackDuration?.cancel()
            lifecycleValue.removeObserver(observer)
        }

    }
}
