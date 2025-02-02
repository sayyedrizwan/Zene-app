@file:kotlin.OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)

package com.rizwansayyed.zene.ui.connect_status.view

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.RangeSliderState
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.isFileExtensionVideo
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.vibeImageFile
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.vibeVideoCroppedFile
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.vibeVideoFile
import com.rizwansayyed.zene.ui.main.connect.profile.SettingsViewSimpleItems
import com.rizwansayyed.zene.ui.theme.BlackTransparent
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun ConnectAttachFiles(viewModel: ConnectViewModel) {
    var showAlert by remember { mutableStateOf(false) }
    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val savedPath = saveFileToAppDirectory(uri)
                if (savedPath == vibeImageFile) viewModel.updateVibeFileInfo(savedPath, false)
                else showAlert = true
            }
        }

    Spacer(Modifier.height(30.dp))
    SettingsViewSimpleItems(R.drawable.ic_folder, R.string.attach_photo_video) {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
    }

    if (showAlert) Dialog(
        { showAlert = false }, DialogProperties(usePlatformDefaultWidth = false)
    ) {
        VideoEditorDialog(viewModel) {
            showAlert = false
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoEditorDialog(viewModel: ConnectViewModel, close: () -> Unit) {
    val context = LocalContext.current.applicationContext
    val coroutines = rememberCoroutineScope()
    val thumbnailsList = remember { mutableStateListOf<Bitmap>() }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val exoPlayer = ExoPlayer.Builder(context).build()
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    exoPlayer.repeatMode = REPEAT_MODE_ONE
                    exoPlayer.volume = 0f
                    player = exoPlayer
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    val mediaItem = MediaItem.fromUri(vibeVideoFile.toUri())
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.play()
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
                .weight(7f)
        )

        Box(
            Modifier
                .fillMaxWidth()
                .weight(3f), Alignment.Center
        ) {
            Row(
                Modifier
                    .align(Alignment.Center)
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .background(MainColor), Arrangement.Center, Alignment.CenterVertically
            ) {
                thumbnailsList.forEach {
                    GlideImage(
                        it,
                        "",
                        modifier = Modifier.size(15.dp, 100.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            RangeCropperSliderVideo(
                Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }
    }

    LaunchedEffect(Unit) {
        val count = 35
        coroutines.launch(Dispatchers.IO) {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(vibeVideoFile.absolutePath)

            val duration =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
                    ?: 0L
            val interval = duration / count

            for (i in 0 until count) {
                val frameTime = i * interval * 1000
                val bitmap =
                    retriever.getFrameAtTime(frameTime, MediaMetadataRetriever.OPTION_CLOSEST)
                if (bitmap != null) {
                    thumbnailsList.add(bitmap)
                }
            }
            retriever.release()
        }
    }

//    LaunchedEffect(Unit) {
//        val cmd =  "-ss 00:00:05.00 -t 00:00:10.00 -noaccurate_seek -i $vibeVideoFile -codec copy -avoid_negative_ts 1 $vibeVideoCroppedFile"
//        val session = FFmpegKit.execute(cmd)
//        if (ReturnCode.isSuccess(session.returnCode)) "done".toast()
//        else {
//            "nooo".toast()
//        }
//    }
}

@Composable
fun RangeCropperSliderVideo(modifier: Modifier) {
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

    Box(Modifier.fillMaxWidth()) {

        RangeSlider(state = rangeSliderState,
            modifier = modifier.height(100.dp),
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

fun extractThumbnails(videoPath: File, count: Int): List<Bitmap> {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(videoPath.absolutePath)

    val duration =
        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
    val interval = duration / count
    val bitmaps = mutableListOf<Bitmap>()

    for (i in 0 until count) {
        val frameTime = i * interval * 1000
        val bitmap = retriever.getFrameAtTime(frameTime, MediaMetadataRetriever.OPTION_CLOSEST)
        if (bitmap != null) {
            bitmaps.add(bitmap)
        }
    }
    retriever.release()
    return bitmaps
}

private fun saveFileToAppDirectory(uri: Uri): File {
    vibeVideoCroppedFile.delete()
    vibeVideoFile.delete()
    vibeImageFile.delete()

    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)
    val mimeType = contentResolver.getType(uri)
    val file = if (isFileExtensionVideo(mimeType)) vibeVideoFile else vibeImageFile
    inputStream?.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }
    return file
}