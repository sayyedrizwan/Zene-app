package com.rizwansayyed.zene.ui.main.connect.connectchat

import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectedUserStatus.FRIENDS
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.ChatTempDataUtils.clearAMessage
import com.rizwansayyed.zene.utils.ChatTempDataUtils.currentOpenedChatProfile
import com.rizwansayyed.zene.utils.NotificationUtils.Companion.clearConversationNotification
import com.rizwansayyed.zene.viewmodel.ConnectChatViewModel
import com.rizwansayyed.zene.viewmodel.ConnectSocketChatViewModel
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ConnectProfileMessagingView(user: ConnectUserInfoResponse, close: () -> Unit) {
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        val homeViewModel: HomeViewModel = hiltViewModel()
        val viewModel: ConnectChatViewModel = hiltViewModel(key = user.user?.name)

        val window = LocalActivity.current?.window
        val connectChatSocket: ConnectSocketChatViewModel = hiltViewModel()
        val state = rememberLazyListState()
        val userInfo by DataStorageManager.userInfo.collectAsState(null)

        val lastVisibleItemIndex by remember {
            derivedStateOf { state.firstVisibleItemIndex + state.layoutInfo.visibleItemsInfo.size - 1 }
        }

        Scaffold(
            Modifier
                .fillMaxWidth()
                .background(MainColor),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MainColor,
                        titleContentColor = MainColor,
                    ),
                    title = {
                        ConnectTopProfileView(user, connectChatSocket, close)
                    }
                )

            },
            bottomBar = {
                if (user.isConnected() == FRIENDS) ConnectProfileMessageView(viewModel, user)
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MainColor)
                    .consumeWindowInsets(innerPadding),
                state = state,
                reverseLayout = true,
                verticalArrangement = Arrangement.Bottom
            ) {
                item(key = "bottom") {
                    Spacer(Modifier.height(80.dp))
                }

                items(viewModel.recentChatItems) {
                    ConnectChatItemView(it, user.user, userInfo)
                }

                if (viewModel.isRecentChatLoading) item {
                    Spacer(Modifier.height(120.dp))
                    CircularLoadingView()
                }

                item(key = "top1") {
                    LaunchedEffect(Unit) {
                        if (!viewModel.isRecentChatLoading)
                            viewModel.getChatConnectRecentMessage(user.user?.email, false)
                    }
                }

                item(key = "top2") {
                    Spacer(Modifier.height(90.dp))
                }


                if (!viewModel.isRecentChatLoading && viewModel.recentChatItems.isEmpty()) item {
                    TextViewSemiBold(
                        stringResource(R.string.break_the_ice_start_conversation),
                        center = true
                    )
                }
            }
        }

        LaunchedEffect(viewModel.sendConnectMessageLoading) {
            if (!viewModel.sendConnectMessageLoading)
                state.animateScrollToItem(0)
        }

        LaunchedEffect(lastVisibleItemIndex) {
            if ((lastVisibleItemIndex == viewModel.recentChatItems.size - 3) && viewModel.recentChatItems.isNotEmpty()) {
                viewModel.getChatConnectRecentMessage(user.user?.email, false)
            }
        }

        LaunchedEffect(viewModel.recentChatItemsToSend) {
            if (viewModel.recentChatItemsToSend != null) {
                connectChatSocket.sendMessage(viewModel.recentChatItemsToSend!!)
                viewModel.clearChatSendItem()
            }
        }

        LaunchedEffect(connectChatSocket.newIncomingMessage) {
            if (connectChatSocket.newIncomingMessage != null) {
                viewModel.addANewItemChat(connectChatSocket.newIncomingMessage!!)
                if (state.firstVisibleItemIndex < 5) state.animateScrollToItem(0)

                viewModel.markConnectMessageToRead(user.user?.email)
                connectChatSocket.clearChatSendItem()
            }
        }

        @Suppress("DEPRECATION") LifecycleResumeEffect(Unit) {
            window?.statusBarColor = MainColor.toArgb()
            window?.navigationBarColor = MainColor.toArgb()
            currentOpenedChatProfile = user.user?.email

            clearAMessage(user.user?.email ?: "")
            clearConversationNotification(user.user?.email ?: "")
            viewModel.getChatConnectRecentMessage(user.user?.email, true)

            viewModel.markConnectMessageToRead(user.user?.email)
            user.user?.email?.let { connectChatSocket.connect(it) }

            onPauseOrDispose {
                window?.statusBarColor = Color.Transparent.toArgb()
                window?.navigationBarColor = Color.Transparent.toArgb()
                currentOpenedChatProfile = null
                viewModel.markConnectMessageToRead(user.user?.email)
                connectChatSocket.disconnect()
                homeViewModel.userInfo()
            }
        }
    }
}