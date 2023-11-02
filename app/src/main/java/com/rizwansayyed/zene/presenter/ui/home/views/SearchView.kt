package com.rizwansayyed.zene.presenter.ui.home.views

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.DataResponse
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.searchHistoryList
import com.rizwansayyed.zene.di.ApplicationModule.Companion.context
import com.rizwansayyed.zene.presenter.theme.DarkGreyColor
import com.rizwansayyed.zene.presenter.ui.SearchEditTextView
import com.rizwansayyed.zene.presenter.ui.SmallIcons
import com.rizwansayyed.zene.presenter.ui.TextBold
import com.rizwansayyed.zene.presenter.ui.TextRegular
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.shimmerBrush
import com.rizwansayyed.zene.presenter.util.UiUtils.GridSpan.TOTAL_ITEMS_GRID
import com.rizwansayyed.zene.viewmodel.HomeApiViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Locale
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchView() {
    val homeApiViewModel: HomeApiViewModel = hiltViewModel()

    val keyboard = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf("") }
    var searchJob by remember { mutableStateOf<Job?>(null) }
    var searchInfoText by remember { mutableStateOf("") }
    val sMusic = stringResource(id = R.string.search_music_artists_album)

    val searchHistory by searchHistoryList
        .collectAsState(initial = runBlocking(Dispatchers.IO) { searchHistoryList.first() })

    val listener =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val result = it.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                text = ""
                result?.forEach { n ->
                    text += n
                }
            }
        }

    LazyVerticalGrid(
        GridCells.Fixed(TOTAL_ITEMS_GRID), Modifier
            .fillMaxSize()
            .background(DarkGreyColor)
    ) {
        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Box(
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 16.dp,
                    top = 54.dp
                ), contentAlignment = Alignment.Center
            ) {
                SearchEditTextView(sMusic, text, listener, {
                    text = it
                    if (it.length <= 2) return@SearchEditTextView

                    searchJob?.cancel()
                    searchJob = CoroutineScope(Dispatchers.IO).launch {
                        delay(1.seconds)
                        homeApiViewModel.searchTextSuggestions(it)
                    }
                }, {
                    searchInfoText = text
                    keyboard?.hide()
                })
            }
        }

        when (val v = homeApiViewModel.suggestionsSearchText) {
            DataResponse.Empty -> if (searchHistory?.isNotEmpty() == true) {
                item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                    Column(Modifier.padding(start = 12.dp)) {
                        Spacer(Modifier.height(30.dp))

                        TextBold(stringResource(R.string.search_history), size = 35)

                        Spacer(Modifier.height(30.dp))
                    }
                }

                items(searchHistory ?: emptyArray(), span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                    SearchItemHistory(it, homeApiViewModel) { s ->
                        searchInfoText = s
                        keyboard?.hide()
                    }
                }
            } else
                item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                    Column(
                        Modifier
                            .padding(top = (LocalConfiguration.current.screenHeightDp / 3).dp)
                            .padding(horizontal = 11.dp)
                    ) {
                        TextRegular(
                            stringResource(R.string.no_search_history),
                            color = Color.LightGray, doCenter = true
                        )
                    }
                }

            is DataResponse.Error -> item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                Column(
                    Modifier
                        .padding(top = (LocalConfiguration.current.screenHeightDp / 3.5).dp)
                        .padding(horizontal = 11.dp)
                ) {
                    TextRegular(
                        stringResource(R.string.error_searching_try_again),
                        color = Color.LightGray, doCenter = true
                    )
                }
            }

            DataResponse.Loading -> items(60, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                SearchItemLoading()
            }

            is DataResponse.Success -> {
                items(1, span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                    Spacer(Modifier.height(20.dp))
                }

                items(v.item ?: emptyList(), span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
                    it.name?.let { t ->
                        SearchTextHistory(t, homeApiViewModel) { s ->
                            searchInfoText = s
                            keyboard?.hide()
                        }
                    }
                }
            }
        }

        item(span = { GridItemSpan(TOTAL_ITEMS_GRID) }) {
            Column {
                Spacer(Modifier.height(180.dp))
            }
        }
    }

    if (searchInfoText.isNotEmpty()) SearchViewInfo(searchInfoText) {
        searchInfoText = ""
        text = ""
        homeApiViewModel.emptySearchText()
    }


    LaunchedEffect(Unit) {
        homeApiViewModel.emptySearchText()
    }
}

@Composable
fun SearchItemLoading() {
    Spacer(
        Modifier
            .padding(horizontal = 3.dp)
            .padding(start = 12.dp, bottom = 27.dp)
            .padding(end = 20.dp)
            .fillMaxWidth()
            .height(13.dp)
            .background(shimmerBrush())
    )
}

@Composable
fun SearchItemHistory(s: String, homeApiViewModel: HomeApiViewModel, search: (String) -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 3.dp)
            .padding(start = 12.dp, bottom = 20.dp)
            .fillMaxWidth()
            .clickable {
                search(s.trim())
                homeApiViewModel.searchTextSuggestions(s.trim())
            }, Arrangement.Center, Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.ic_history), "",
            Modifier
                .padding(end = 8.dp)
                .size(25.dp),
            colorFilter = ColorFilter.tint(Color.Gray)
        )

        TextSemiBold(s, Modifier.weight(1f), color = Color.Gray, size = 18)

        Image(
            painterResource(R.drawable.ic_arrow_up_right), "",
            Modifier
                .padding(end = 5.dp)
                .size(25.dp),
            colorFilter = ColorFilter.tint(Color.Gray)
        )
    }
}

@Composable
fun SearchTextHistory(s: String, homeApiViewModel: HomeApiViewModel, search: (String) -> Unit) {
    Row(
        Modifier
            .padding(horizontal = 3.dp)
            .padding(start = 12.dp, bottom = 20.dp)
            .fillMaxWidth()
            .clickable {
                search(s.trim())
                homeApiViewModel.searchTextSuggestions(s.trim())
            }, Arrangement.Center, Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.ic_search), "",
            Modifier
                .padding(end = 8.dp)
                .size(25.dp),
            colorFilter = ColorFilter.tint(Color.Gray)
        )

        TextSemiBold(s, Modifier.weight(1f), color = Color.Gray, size = 18)

        Image(
            painterResource(R.drawable.ic_arrow_up_right), "",
            Modifier
                .padding(end = 5.dp)
                .size(25.dp),
            colorFilter = ColorFilter.tint(Color.Gray)
        )
    }
}

fun startSpeech(): Intent {
    return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            context.getString(R.string.speak_music_artists_album)
        )
    }
}
