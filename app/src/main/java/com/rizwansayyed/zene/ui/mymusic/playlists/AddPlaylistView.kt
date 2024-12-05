package com.rizwansayyed.zene.ui.mymusic.playlists

import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.ui.view.ButtonWithImage
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.InputTypes
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.SearchScreenBar
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.TextPoppinsThin
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.ui.view.isScreenBig
import com.rizwansayyed.zene.utils.FirebaseLogEvents
import com.rizwansayyed.zene.utils.FirebaseLogEvents.logEvents
import com.rizwansayyed.zene.utils.Utils.URLS.IMG_PLAYLISTS
import com.rizwansayyed.zene.utils.Utils.toast
import com.rizwansayyed.zene.viewmodel.ZeneViewModel

@Composable
fun AddPlaylistDialog(viewModel: ZeneViewModel, onDismiss: () -> Unit) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false), onDismissRequest = onDismiss
    ) {
        val context = LocalContext.current
        val windowManager =
            remember { context.getSystemService(Context.WINDOW_SERVICE) as WindowManager }

        val metrics = DisplayMetrics().apply {
            windowManager.defaultDisplay.getRealMetrics(this)
        }
        val (width, height) = with(LocalDensity.current) {
            Pair(metrics.widthPixels.toDp(), metrics.heightPixels.toDp())
        }

        Column(
            modifier = Modifier
                .requiredSize(width, height)
                .background(color = Color.Black)
                .verticalScroll(rememberScrollState()),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            AddPlaylistView(viewModel, onDismiss)
        }
    }
}

@Composable
fun AddPlaylistView(viewModel: ZeneViewModel, onDismiss: () -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val isBig = isScreenBig()

    val enterAValidPlaylistName = stringResource(R.string.enter_a_valid_playlists)
    val errorCreatingNewPlaylists = stringResource(R.string.error_creating_a_new_playlists)

    var img by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var playlistName by remember { mutableStateOf("") }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) img = uri.toString()
        }

    TextPoppins(stringResource(id = R.string.new_playlists), size = 16)
    Spacer(Modifier.height(30.dp))

    AsyncImage(
        imgBuilder(img),
        "",
        Modifier
            .clip(RoundedCornerShape(12.dp))
            .size(if (isBig) (screenWidth / 2) else (screenWidth - 120.dp)),
        contentScale = ContentScale.Crop,
        onSuccess = {
            bitmap = it.result.drawable.toBitmap()
        }
    )

    Row(Modifier.padding(top = 30.dp), Arrangement.Center, Alignment.CenterVertically) {
        Row(Modifier.clickable { showSearch = !showSearch }) {
            ImageIcon(R.drawable.ic_search, 26)
        }
        Spacer(Modifier.width(30.dp))
        Row(Modifier.clickable {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }) {
            ImageIcon(R.drawable.ic_folder, 26)
        }
    }

    if (showSearch) {
        Spacer(Modifier.height(30.dp))
        SearchScreenBar(InputTypes.IMAGES, searchQuery, {
            searchQuery = it
        }) {
            showSearch = false
            viewModel.searchImages(it)
        }
    } else {
        when (val v = viewModel.searchImg) {
            APIResponse.Empty -> {}
            is APIResponse.Error -> {}
            APIResponse.Loading -> {
                Spacer(Modifier.height(30.dp))

                LoadingView(Modifier.size(32.dp))
            }

            is APIResponse.Success -> {
                if (v.data.isEmpty())
                    TextPoppinsThin(v = stringResource(R.string.no_img_found))
                else LazyRow {
                    items(v.data) {
                        AsyncImage(
                            imgBuilder(it),
                            "",
                            Modifier
                                .padding(vertical = 10.dp, horizontal = 10.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .size(120.dp, 200.dp)
                                .clickable {
                                    img = it
                                },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }

    Spacer(Modifier.height(40.dp))

    SearchScreenBar(InputTypes.PLAYLISTS, playlistName, {
        playlistName = it
        showSearch = false
        viewModel.searchImages("")
    }) {
        keyboardController?.hide()
    }

    Spacer(Modifier.height(30.dp))


    if (isLoading) LoadingView(Modifier.size(32.dp))
    else ButtonWithImage(R.drawable.ic_add_playlist, R.string.create_playlist) {
        if (playlistName.length <= 3) {
            enterAValidPlaylistName.toast()
            return@ButtonWithImage
        }

        val b = if (bitmap == null || img == IMG_PLAYLISTS) null
        else bitmap
        logEvents(FirebaseLogEvents.FirebaseEvents.CREATING_NEW_PLAYLISTS)
        viewModel.createNewPlaylist(playlistName, b, null)
    }

    LaunchedEffect(viewModel.createPlaylistInfo) {
        when (viewModel.createPlaylistInfo) {
            APIResponse.Empty -> isLoading = false
            is APIResponse.Error -> {
                errorCreatingNewPlaylists.toast()
                isLoading = false
            }
            APIResponse.Loading ->  isLoading = true
            is APIResponse.Success -> {
                viewModel.createPlaylistInfo = APIResponse.Empty
                onDismiss()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.searchImages("")
        img = IMG_PLAYLISTS
    }
}