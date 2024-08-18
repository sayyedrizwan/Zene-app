package com.rizwansayyed.zene.ui.home.view

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.service.MusicServiceUtils.openVideoPlayer
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.AlertDialogView
import com.rizwansayyed.zene.ui.view.BorderButtons
import com.rizwansayyed.zene.ui.view.CardsViewDesc
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ImageView
import com.rizwansayyed.zene.ui.view.LoadingCardView
import com.rizwansayyed.zene.ui.view.LoadingLinearView
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.TextAntroVenctra
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsSemiBold
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.ui.view.shareUrl
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_FEED
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_MY_MUSIC
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SEARCH
import com.rizwansayyed.zene.utils.NavigationUtils.NAV_SETTINGS
import com.rizwansayyed.zene.utils.NavigationUtils.sendNavCommand
import com.rizwansayyed.zene.utils.Utils.shareTxtImage
import com.rizwansayyed.zene.utils.Utils.startAppSettings
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.ZeneViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local
import kotlin.time.Duration.Companion.seconds

@Composable
fun HomeHeaderView() {
    var findSongDialog by remember { mutableStateOf(false) }
    var microphonePermissionDialog by remember { mutableStateOf(false) }

    val permission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) findSongDialog = true
            else microphonePermissionDialog = true
        }

    Spacer(Modifier.height(70.dp))

    Row(Modifier.padding(horizontal = 7.dp), Arrangement.Center) {
        Column {
            Row {
                Image(painterResource(R.mipmap.logor), "", Modifier.size(65.dp))

                Row(Modifier.padding(top = 7.dp, start = 12.dp)) {
                    TextAntroVenctra(stringResource(R.string.app_name), size = 36)
                }
            }
//            JoinPremiumCommunity()
        }
        Spacer(Modifier.weight(1f))

        Row(Modifier.height(65.dp), Arrangement.Center, Alignment.CenterVertically) {
            ImageIcon(R.drawable.ic_setting) {
                sendNavCommand(NAV_SETTINGS)
            }

            Spacer(Modifier.width(15.dp))

            ImageIcon(R.drawable.ic_voice) {
                permission.launch(Manifest.permission.RECORD_AUDIO)
            }

            Spacer(Modifier.width(15.dp))
        }
    }

    Spacer(Modifier.height(30.dp))

    if (findSongDialog) SearchSongDialog {
        findSongDialog = false
    }

    if (microphonePermissionDialog) AlertDialogView(
        R.string.need_microphone_permission,
        R.string.need_microphone_permission_desc,
        R.string.grant
    ) {
        if (it) startAppSettings()
        microphonePermissionDialog = false
    }
}


@Composable
fun SearchSongDialog(close: () -> Unit) {
    val zeneViewModel: ZeneViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current

    var coroutine by remember { mutableStateOf<Job?>(null) }
    val songNotFound = stringResource(R.string.no_song_found_try_again)

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    var isLoading by remember { mutableStateOf(true) }
    var isFindingSong by remember { mutableStateOf(false) }

    var startSearch by remember { mutableStateOf<String?>(null) }


    var webView: SongDetectWebView? = null

    fun notFound(showToast: Boolean) {
        webView?.destroyView()
        webView = null
        close()
        if (showToast) songNotFound.toast()
    }

    Dialog(
        { notFound(false) },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .requiredSize(screenWidth - 10.dp, (screenHeight.value / 1.3).dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black),
            Arrangement.Center, Alignment.CenterHorizontally
        ) {
            if (startSearch == null) {
                Box(Modifier.size(0.dp)) {
                    AndroidView({ ctx ->
                        SongDetectWebView(ctx, {
                            logEvents(FirebaseLogEvents.FirebaseEvents.NOT_FOUND_SONG_DETECT_VIEW)
                            coroutine?.cancel()
                            coroutine = null
                            notFound(true)
                        }, {
                            isLoading = false
                            isFindingSong = true
                        }, {
                            logEvents(FirebaseLogEvents.FirebaseEvents.FOUND_SONG_DETECT_VIEW)
                            coroutine?.cancel()
                            coroutine = null
                            startSearch = it
                        }).also {
                            webView = it
                        }
                    }, Modifier.fillMaxSize())
                }
                ImageView(R.drawable.zene_logo_round, Modifier.size(190.dp))

                Spacer(Modifier.height(30.dp))

                if (isLoading) {
                    LoadingView(Modifier.size(24.dp))
                    Spacer(Modifier.height(10.dp))
                    TextPoppins(
                        v = stringResource(R.string.loading_make_sure_your_device_can_hear_song_properly),
                        true, size = 14
                    )
                }

                if (isFindingSong) {
                    LoadingLinearView()
                    Spacer(Modifier.height(10.dp))
                    TextPoppins(v = stringResource(R.string.listening___))
                }
            } else {
                when (val v = zeneViewModel.searchFindSong) {
                    APIResponse.Empty -> {}
                    is APIResponse.Error -> {
                        notFound(true)
                    }

                    APIResponse.Loading -> {
                        LoadingCardView()
                    }

                    is APIResponse.Success ->
                        CardsViewDesc(v.data, listOf(v.data))

                }
                LaunchedEffect(Unit) {
                    zeneViewModel.searchFindSong(startSearch!!)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        val observer = LifecycleEventObserver { source, event ->
            if (event == Lifecycle.Event.ON_PAUSE) notFound(false)
        }

        coroutine = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(1.seconds)
                webView?.checkFunctions()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            coroutine?.cancel()
            coroutine = null
        }
    }
}
