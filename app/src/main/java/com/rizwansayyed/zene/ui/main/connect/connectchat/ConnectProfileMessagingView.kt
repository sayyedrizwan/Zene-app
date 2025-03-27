package com.rizwansayyed.zene.ui.main.connect.connectchat

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectedUserStatus
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.utils.ChatTempDataUtils.clearAMessage
import com.rizwansayyed.zene.utils.ChatTempDataUtils.currentOpenedChatProfile
import com.rizwansayyed.zene.utils.NotificationUtils.Companion.clearConversationNotification
import com.rizwansayyed.zene.viewmodel.ConnectChatViewModel
import com.rizwansayyed.zene.viewmodel.ConnectSocketChatViewModel
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import com.rizwansayyed.zene.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ConnectProfileMessagingView(
    user: ConnectUserInfoResponse,
    connectViewModel: ConnectViewModel,
    close: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        close,
        sheetState = sheetState,
        contentColor = MainColor,
        containerColor = MainColor,
        properties = ModalBottomSheetProperties()
    ) {
        val homeViewModel: HomeViewModel = hiltViewModel()
        val viewModel: ConnectChatViewModel = hiltViewModel()

        val window = LocalActivity.current?.window
        val coroutine = rememberCoroutineScope()
        val connectChatSocket: ConnectSocketChatViewModel = hiltViewModel()
        val state = rememberLazyListState()
        val userInfo by DataStorageManager.userInfo.collectAsState(null)

        var showMoreMessage by remember { mutableStateOf(false) }
        var showTypingMessage by remember { mutableStateOf(false) }

        val lastVisibleItemIndex by remember {
            derivedStateOf { state.firstVisibleItemIndex + state.layoutInfo.visibleItemsInfo.size - 1 }
        }

        val isTying by remember { derivedStateOf { connectChatSocket.isTyping } }

        Scaffold(Modifier
            .fillMaxSize()
            .background(MainColor), topBar = {
            ConnectTopProfileView(user, connectChatSocket, connectViewModel, close)
        }, bottomBar = {
            if (user.isConnected() == ConnectedUserStatus.FRIENDS) {
                Column(Modifier.fillMaxWidth(), Arrangement.Center, Alignment.End) {
                    if (showTypingMessage) {
                        Column(
                            Modifier
                                .padding(5.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.Red)
                                .clickable {
                                    coroutine.launch {
                                        state.animateScrollToItem(0)
                                    }
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            Arrangement.Center, Alignment.CenterHorizontally
                        ) {
                            GlideImage(
                                R.raw.typing_animation,
                                "typing",
                                Modifier.size(55.dp, 39.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    if (showMoreMessage) {
                        Column(
                            Modifier
                                .padding(5.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.Red)
                                .clickable {
                                    coroutine.launch {
                                        state.animateScrollToItem(0)
                                    }
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            Arrangement.Center, Alignment.CenterHorizontally
                        ) {
                            ImageIcon(R.drawable.ic_message_multiple, 18)
                            ImageIcon(R.drawable.ic_arrow_down, 15)
                        }
                    }
                    ConnectProfileMessageView(viewModel, user, connectChatSocket)
                }
            }
        }) { innerPadding ->
            Box(
                Modifier
                    .background(MainColor)
                    .fillMaxSize()
                    .consumeWindowInsets(innerPadding)
            ) {
                LazyColumn(
                    Modifier.fillMaxSize(),
                    state = state,
                    reverseLayout = true,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    item(key = "bottom") {
                        Spacer(Modifier.height(80.dp))

                        LaunchedEffect(showMoreMessage) {
                            showMoreMessage = false
                            showTypingMessage = false
                        }
                    }

                    item(key = "typing") {
                        this@ModalBottomSheet.AnimatedVisibility(isTying, Modifier) {
                            UserTypingAnimation(user.user)
                        }
                    }

                    items(viewModel.recentChatItems) {
                        ConnectChatItemView(it, user.user, userInfo) {
                            viewModel.removeANewItemChat(user.user?.email, it._id, true)
                            connectChatSocket.connectDeleteMessage(it)
                        }
                    }

                    if (viewModel.isRecentChatLoading) item {
                        Spacer(Modifier.height(120.dp))
                        CircularLoadingView()
                    }

                    item(key = "top1") {
                        LaunchedEffect(Unit) {
                            if (!viewModel.isRecentChatLoading) {
                                viewModel.getChatConnectRecentMessage(user.user?.email, false)
                            }
                        }
                    }

                    item(key = "top2") {
                        Spacer(Modifier.height(90.dp))
                    }

                    if (!viewModel.isRecentChatLoading && viewModel.recentChatItems.isEmpty()) item {
                        TextViewSemiBold(
                            stringResource(R.string.break_the_ice_start_conversation), center = true
                        )
                    }
                }
            }
        }

        LaunchedEffect(viewModel.sendConnectMessageLoading) {
            if (!viewModel.sendConnectMessageLoading) state.animateScrollToItem(0)
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
                else showMoreMessage = true

                viewModel.markConnectMessageToRead(user.user?.email)
                connectChatSocket.clearChatSendItem()
            }
        }

        LaunchedEffect(connectChatSocket.deleteChatID) {
            if (connectChatSocket.deleteChatID.trim().length > 3) {
                viewModel.removeANewItemChat(
                    user.user?.email, connectChatSocket.deleteChatID, false
                )
            }
        }

        LaunchedEffect(isTying) {
            if (isTying) {
                if (state.firstVisibleItemIndex < 5) state.animateScrollToItem(0)
                else showTypingMessage = true
            } else
                showTypingMessage = false
        }

        BackHandler { close() }

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

    LaunchedEffect(sheetState.currentValue) {
        if (sheetState.currentValue == SheetValue.PartiallyExpanded) {
            sheetState.expand()
        }
    }
}