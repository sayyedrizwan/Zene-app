package com.rizwansayyed.zene.ui.connect_status.view

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.ui.connect_status.ConnectCommentListener
import com.rizwansayyed.zene.ui.connect_status.ConnectCommentListenerManager
import com.rizwansayyed.zene.ui.main.connect.connectview.openConnectUserProfile
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.safeLaunch
import com.rizwansayyed.zene.viewmodel.GifViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalGlideComposeApi::class
)
@Composable
fun ConnectVibeCommentsView(data: ConnectFeedDataResponse?, close: () -> Unit) {
    val viewModel: GifViewModel = hiltViewModel(key = data?.id?.toString())
    var page by remember { mutableIntStateOf(0) }
    var showGifAlert by remember { mutableStateOf(false) }
    val state = rememberLazyListState()
    var isBottomTriggered by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        close, sheetState = sheetState, contentColor = Color.Black, containerColor = Color.Black
    ) {
        LazyColumn(Modifier.fillMaxSize(), state) {
            stickyHeader {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(vertical = 8.dp),
                    Arrangement.Center,
                    Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(20.dp))
                    TextViewBold(stringResource(R.string.gif_spress), 18, center = true)
                    Spacer(Modifier.height(2.dp))
                    TextViewNormal(
                        stringResource(R.string.comment_express_yourself_with_gif),
                        14, center = true
                    )
                    Spacer(Modifier.height(10.dp))

                    ButtonWithBorder(R.string.post_a_gif) { showGifAlert = true }

                    Spacer(Modifier.height(20.dp))
                }
            }

            items(viewModel.commentLists) {
                Column(
                    Modifier
                        .padding(top = 35.dp)
                        .padding(horizontal = 9.dp),
                    Arrangement.Center,
                    Alignment.Start
                ) {
                    Row(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterVertically) {
                        GlideImage(it.profile_photo,
                            it.name,
                            Modifier
                                .padding(end = 6.dp)
                                .size(45.dp)
                                .clickable { it.email?.let { openConnectUserProfile(it) } }
                                .clip(RoundedCornerShape(14.dp)),
                            contentScale = ContentScale.Crop)

                        TextViewSemiBold(it.name ?: "", 13, line = 1)
                        Spacer(Modifier.weight(1f))
                        TextViewNormal(it.ts(), 12, line = 1)
                    }
                }

                GlideImage(
                    it.gif, it.name,
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .heightIn(min = 100.dp),
                    contentScale = ContentScale.FillWidth
                )
            }


            if (!viewModel.isLoading && viewModel.commentLists.isEmpty()) item {
                TextViewBold(stringResource(R.string.no_gif_posted), 19, center = true)
            }

            if (viewModel.isLoading) item {
                CircularLoadingView()
            }

            item {
                Spacer(Modifier.height(200.dp))
            }
        }


        LaunchedEffect(Unit) {
            ConnectCommentListenerManager.setCallback(object : ConnectCommentListener {
                override fun addedNewComment() {
                    page = 0
                    viewModel.commentGifs(data?.id, page)
                    page += 1
                }
            })
            sheetState.expand()
        }

        LaunchedEffect(state) {
            snapshotFlow { state.layoutInfo }
                .collect { layoutInfo ->
                    val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                    val totalItemsCount = layoutInfo.totalItemsCount

                    if (lastVisibleItemIndex >= totalItemsCount - 1 && !isBottomTriggered) {
                        isBottomTriggered = true
                        viewModel.commentGifs(data?.id, page)
                        page += 1
                    } else if (lastVisibleItemIndex < totalItemsCount - 1) {
                        isBottomTriggered = false
                    }
                }
        }

        if (showGifAlert) GifAlert(data?.id, viewModel) {
            showGifAlert = false
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GifAlert(id: Int?, viewModel: GifViewModel, close: (String?) -> Unit) {
    Dialog({ close(null) }, DialogProperties(usePlatformDefaultWidth = false)) {
        var search by remember { mutableStateOf("") }
        val focusManager = LocalFocusManager.current

        var addAGif by remember { mutableStateOf<String?>(null) }

        val coroutines = rememberCoroutineScope()
        var job by remember { mutableStateOf<Job?>(null) }

        @Composable
        fun ShowGIFView(gif: ResponseResult<List<String>>) {
            when (gif) {
                ResponseResult.Empty -> {}
                is ResponseResult.Error -> {}
                ResponseResult.Loading -> {
                    Row(Modifier.padding(top = 100.dp)) {
                        CircularLoadingView()
                    }
                }

                is ResponseResult.Success -> {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        verticalItemSpacing = 4.dp,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        content = {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                Spacer(Modifier.height(90.dp))
                            }
                            items(gif.data) {
                                GlideImage(it, "gif", Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        if (id == null) close(it)
                                        else addAGif = it
                                    })
                            }

                            item(span = StaggeredGridItemSpan.FullLine) {
                                Spacer(Modifier.height(40.dp))
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Box(
            Modifier
                .background(DarkCharcoal)
                .fillMaxSize()
        ) {
            if (viewModel.searchGif is ResponseResult.Empty) ShowGIFView(viewModel.trendingGif)

            ShowGIFView(viewModel.searchGif)

            TextField(
                search,
                { if (it.length <= 20) search = it },
                Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions {
                    focusManager.clearFocus()
                },
                placeholder = {
                    TextViewNormal(stringResource(R.string.search_gif), 14)
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MainColor,
                    unfocusedContainerColor = MainColor,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.White
                ),
                singleLine = true
            )
        }


        LaunchedEffect(search) {
            job?.cancel()
            job = coroutines.safeLaunch {
                delay(1.seconds)
                viewModel.searchGif(search)
            }
        }

        LaunchedEffect(Unit) {
            viewModel.trendingGif()
        }

        if (addAGif != null) AlertDialogWithImage(addAGif, {
            addAGif = null
            close(null)
        }) {
            addAGif?.let { viewModel.postAGif(it, id) }
            addAGif = null
            close(null)
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AlertDialogWithImage(
    thumbnail: String?, dismiss: () -> Unit, click: () -> Unit
) {
    AlertDialog(icon = {
        GlideImage(
            thumbnail, "gif", Modifier.size(120.dp), contentScale = ContentScale.Crop
        )
    }, title = {
        TextViewNormal(stringResource(R.string.post_a_gif), 17, line = 2, center = true)
    }, text = {
        TextViewNormal(stringResource(R.string.post_a_gif_desc), 16, center = true)
    }, onDismissRequest = {
        dismiss()
    }, confirmButton = {
        TextButton(onClick = {
            click()
        }) {
            TextViewLight(stringResource(R.string.post), 13)
        }
    }, dismissButton = {
        TextButton(onClick = {
            dismiss()
        }) {
            TextViewLight(stringResource(R.string.cancel), 13)
        }
    })
}