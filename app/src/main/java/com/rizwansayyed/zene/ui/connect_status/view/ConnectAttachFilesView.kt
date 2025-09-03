@file:kotlin.OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)

package com.rizwansayyed.zene.ui.connect_status.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.lifecycle.compose.LifecycleResumeEffect
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
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.cropVideoFile
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.isFileExtensionVideo
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.vibeImageFile
import com.rizwansayyed.zene.ui.connect_status.utils.CameraUtils.Companion.vibeVideoFile
import com.rizwansayyed.zene.ui.main.connect.profile.SettingsViewSimpleItems
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.io.File
import kotlin.time.Duration.Companion.seconds

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
    var exoplayerDurationJob by remember { mutableStateOf<Job?>(null) }
    var start by remember { mutableFloatStateOf(0f) }
    var end by remember { mutableFloatStateOf(0f) }
    var isLoading by remember { mutableStateOf(false) }

    val exoPlayer = ExoPlayer.Builder(context).build()

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    useController = false
                    exoPlayer.repeatMode = REPEAT_MODE_ONE
                    exoPlayer.volume = 1f
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                    val mediaItem = MediaItem.fromUri(vibeVideoFile.toUri())
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.play()
                    player = exoPlayer
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
                .weight(6f)
        )

        LifecycleResumeEffect(Unit) {
            exoPlayer.play()
            onPauseOrDispose { exoPlayer.pause() }
        }

        Column(
            Modifier
                .padding(horizontal = 6.dp)
                .fillMaxWidth()
                .weight(4f),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            Box(Modifier.fillMaxWidth()) {
                Row(
                    Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .background(MainColor),
                    Arrangement.Center,
                    Alignment.CenterVertically
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

                ConnectVideoCropperSliderView(
                    Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                ) { s, e ->
                    start = s
                    end = e
                }
            }

            if (isLoading) CircularLoadingView()

            Row(
                Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                ButtonWithBorder(R.string.cancel) {
                    close()
                }

                ButtonWithBorder(R.string.attach) {
                    if (isLoading) return@ButtonWithBorder
                    isLoading = true
                    cropVideoFile(vibeVideoFile, start, end) {
                        isLoading = false
                        viewModel.updateVibeFileInfo(it, false)
                        close()
                    }

                }
            }

        }
    }

    LaunchedEffect(Unit) {
        val count = 25
        coroutines.safeLaunch {
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

    DisposableEffect(Unit) {
        exoplayerDurationJob?.cancel()
        exoplayerDurationJob = coroutines.safeLaunch(Dispatchers.Main) {
            while (true) {
                if (exoPlayer.currentPosition > end || exoPlayer.currentPosition < start) {
                    exoPlayer.seekTo(start.toLong())
                }
                delay(1.seconds)
            }
        }

        onDispose { exoplayerDurationJob?.cancel() }
    }
}

fun saveFileToAppDirectory(uri: Uri, reduceSize: Boolean = false): File {
    vibeVideoFile.delete()
    vibeImageFile.delete()

    val contentResolver = context.contentResolver
    val inputStream = contentResolver.openInputStream(uri)
    val mimeType = contentResolver.getType(uri)
    val file = if (isFileExtensionVideo(mimeType)) vibeVideoFile else vibeImageFile

    if (reduceSize && file == vibeImageFile) {
        val bitmap = contentResolver.openInputStream(uri)?.use { input ->
            BitmapFactory.decodeStream(input)
        }

        bitmap?.let {
            var quality = 70
            file.outputStream().use { output ->
                it.compress(Bitmap.CompressFormat.JPEG, quality, output)
            }

            if (file.length() > 300 * 1024) {
                quality = 40
                file.outputStream().use { output ->
                    it.compress(Bitmap.CompressFormat.JPEG, quality, output)
                }
            }

            if (file.length() > 300 * 1024) {
                quality = 20
                file.outputStream().use { output ->
                    it.compress(Bitmap.CompressFormat.JPEG, quality, output)
                }
            }
        }

    } else {
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    }
    return file
}

