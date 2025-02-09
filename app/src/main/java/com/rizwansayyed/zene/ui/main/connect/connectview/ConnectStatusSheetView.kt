package com.rizwansayyed.zene.ui.main.connect.connectview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.connect_status.ConnectStatusCallbackManager
import com.rizwansayyed.zene.ui.connect_status.ConnectStatusListener
import com.rizwansayyed.zene.ui.connect_status.view.ConnectVibeItemView
import com.rizwansayyed.zene.ui.main.connect.view.PhoneNumberVerificationView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.HorizontalCircleShimmerLoadingCard
import com.rizwansayyed.zene.ui.view.HorizontalShimmerLoadingCard
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@Composable
fun ConnectStatusView(connectViewModel: ConnectViewModel) {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val userInfo by DataStorageManager.userInfo.collectAsState(null)

    val coroutines = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }
    var page by remember { mutableIntStateOf(0) }
    val state = rememberLazyListState()
    var isBottomTriggered by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(
                max = (0.95f * androidx.compose.ui.platform.LocalConfiguration.current.screenHeightDp).dp
            )
    ) {
        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(MainColor), state
        ) {
            item {
                Box(Modifier.padding(horizontal = 9.dp)) {
                    TextViewBold(stringResource(R.string.songs_trending_near_you), 18)
                }
                Spacer(Modifier.height(12.dp))

                when (val v = homeViewModel.nearMusic) {
                    ResponseResult.Empty -> {}
                    is ResponseResult.Error -> {}
                    ResponseResult.Loading -> HorizontalShimmerLoadingCard()
                    is ResponseResult.Success -> LazyRow(Modifier.fillMaxWidth()) {
                        items(v.data) {
                            ItemCardView(it)
                        }
                    }
                }
            }

            item {
                if ((userInfo?.phoneNumber?.length ?: 0) < 4) PhoneNumberVerificationView()
            }

            if ((userInfo?.phoneNumber?.length ?: 0) > 4) {
                item {
                    ConnectRecentContactsView()
                }

                when (val v = connectViewModel.connectUserList) {
                    ResponseResult.Empty -> {}
                    is ResponseResult.Error -> {}
                    ResponseResult.Loading -> item {
                        HorizontalCircleShimmerLoadingCard()
                    }

                    is ResponseResult.Success -> {
                        if (v.data.isEmpty()) item {
                            TextViewNormal(
                                stringResource(R.string.you_have_no_friends),
                                15,
                                line = 1,
                                center = true
                            )
                        } else item {
                            LazyRow(Modifier.fillMaxWidth()) {
                                items(v.data) {
                                    ConnectFriendsLists(it)
                                }
                            }
                        }
                    }
                }

                when (val v = connectViewModel.connectUserFriendsList) {
                    ResponseResult.Empty -> {}
                    is ResponseResult.Error -> {}
                    ResponseResult.Loading -> {}
                    is ResponseResult.Success -> {
                        if (v.data.isNotEmpty()) item {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 5.dp)
                                    .padding(top = 50.dp, bottom = 10.dp)
                            ) {
                                TextViewBold(stringResource(R.string.friend_requests), 18)
                            }

                            LazyRow(Modifier.fillMaxWidth()) {
                                items(v.data) {
                                    ConnectFriendsRequestLists(it)
                                }
                            }
                        }
                    }
                }
            }

            item {
                ConnectStatusTopView()
            }

            items(connectViewModel.connectUserVibesFeeds) {
                ConnectVibeItemView(it)
            }


            item {
                if (connectViewModel.isLoadingVibeFeed) {
                    CircularLoadingView()
                }
                if (!connectViewModel.isLoadingVibeFeed && connectViewModel.connectUserVibesFeeds.isEmpty()) {
                    TextViewBold(stringResource(R.string.no_posts), 19, center = true)
                }
            }

            item {
                Spacer(Modifier.height(200.dp))
            }

        }
    }


    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo }
            .collect { layoutInfo ->
                val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                val totalItemsCount = layoutInfo.totalItemsCount

                if (lastVisibleItemIndex >= totalItemsCount - 1 && !isBottomTriggered) {
                    isBottomTriggered = true
                    connectViewModel.connectFriendsVibesList(page)
                    page += 1
                } else if (lastVisibleItemIndex < totalItemsCount - 1) {
                    isBottomTriggered = false
                }
            }
    }

    LaunchedEffect(Unit) {
        ConnectStatusCallbackManager.setCallback(object : ConnectStatusListener {
            override fun addedNewStatus() {
                page = 0
                connectViewModel.connectFriendsVibesList(page)
                page += 1
            }
        })
    }

    LifecycleResumeEffect(Unit) {
        homeViewModel.connectNearMusic()
        connectViewModel.connectFriendsRequestsList()
        onPauseOrDispose { }
    }

    LifecycleResumeEffect(Unit) {
        job?.cancel()
        job = coroutines.launch(Dispatchers.IO) {
            while (true) {
                connectViewModel.connectFriendsList()
                delay(15.seconds)
            }
        }
        onPauseOrDispose { job?.cancel() }
    }
}