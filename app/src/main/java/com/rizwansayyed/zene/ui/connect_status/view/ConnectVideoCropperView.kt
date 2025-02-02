package com.rizwansayyed.zene.ui.connect_status.view

import android.media.MediaMetadataRetriever
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.vibeVideoFile
import com.rizwansayyed.zene.ui.theme.BlackTransparent
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectVideoCropperSliderView(
    modifier: Modifier, duration: (start: Float, end: Float) -> Unit
) {
    val videoDuration = getVideoDuration(vibeVideoFile).toFloat()
    val minSelectableRange = 3f
    val maxSelectableRange = minOf(videoDuration, 15f * 1000L)
    var selectedRange by remember { mutableStateOf(0f..maxSelectableRange) }

    val rangeSliderState = remember {
        RangeSliderState(selectedRange.start,
            selectedRange.endInclusive,
            valueRange = 0f..videoDuration,
            onValueChangeFinished = {})
    }

    val startInteractionSource = remember { MutableInteractionSource() }
    val endInteractionSource = remember { MutableInteractionSource() }
    val startThumbAndTrackColors = SliderDefaults.colors(
        thumbColor = Color.Red,
        activeTrackColor = Color.Transparent,
        inactiveTrackColor = BlackTransparent,
        disabledThumbColor = BlackTransparent
    )

    Box(modifier.fillMaxWidth()) {
        RangeSlider(state = rangeSliderState,
            modifier = Modifier.height(100.dp),
            startInteractionSource = startInteractionSource,
            endInteractionSource = endInteractionSource,
            startThumb = {
                SliderDefaults.Thumb(
                    interactionSource = startInteractionSource, colors = startThumbAndTrackColors
                )
            },
            endThumb = {
                SliderDefaults.Thumb(
                    interactionSource = endInteractionSource, colors = startThumbAndTrackColors
                )
            },
            track = { rangeSliderState ->
                SliderDefaults.Track(
                    colors = startThumbAndTrackColors,
                    rangeSliderState = rangeSliderState,
                    modifier = Modifier
                        .height(100.dp)
                        .border(1.dp, Color.White)
                )
            })
    }

    LaunchedEffect(rangeSliderState.activeRangeStart, rangeSliderState.activeRangeEnd) {
        val start = rangeSliderState.activeRangeStart
        val end = rangeSliderState.activeRangeEnd
        val diff = end - start

        if (diff > maxSelectableRange) {
            if (start == selectedRange.start) {
                rangeSliderState.activeRangeStart = end - maxSelectableRange
            } else {
                rangeSliderState.activeRangeEnd = start + maxSelectableRange
            }
        } else if (diff < minSelectableRange) {
            if (start == selectedRange.start) {
                rangeSliderState.activeRangeStart = end - minSelectableRange
            } else {
                rangeSliderState.activeRangeEnd = start + minSelectableRange
            }
        } else {
            selectedRange = start..end
        }
        selectedRange = rangeSliderState.activeRangeStart..rangeSliderState.activeRangeEnd
        duration(rangeSliderState.activeRangeStart, rangeSliderState.activeRangeEnd)
    }
}

fun getVideoDuration(videoPath: File): Long {
    val retriever = MediaMetadataRetriever()
    try {
        retriever.setDataSource(videoPath.absolutePath)
        val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        return durationString?.toLong() ?: 0L
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        retriever.release()
    }
    return 0L
}
