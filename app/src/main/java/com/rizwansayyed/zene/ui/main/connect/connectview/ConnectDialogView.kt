package com.rizwansayyed.zene.ui.main.connect.connectview

import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager.userInfo
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.FullUsersShimmerLoadingCard
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.ui.view.UserContactInfo
import com.rizwansayyed.zene.ui.view.UserSearchInfo
import com.rizwansayyed.zene.utils.MainUtils.removeSpecialChars
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import com.rizwansayyed.zene.viewmodel.PhoneNumberViewModel
import kotlinx.coroutines.flow.firstOrNull


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EditProfileView(close: () -> Unit) {
    var showEmoji by remember { mutableStateOf(false) }
    var statusText by remember { mutableStateOf("") }
    var emailText by remember { mutableStateOf("") }
    var profilePic by remember { mutableStateOf("") }
    var nameText by remember { mutableStateOf("") }
    var usernameText by remember { mutableStateOf("") }

    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        it?.toString()?.let { p ->  profilePic = p }
    }
    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                item {
                    Spacer(Modifier.height(60.dp))
                    Column(
                        Modifier.fillMaxWidth(), Arrangement.Center, Alignment.CenterHorizontally
                    ) {
                        Box(Modifier.clickable {
                            pickMedia.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }) {
                            GlideImage(profilePic, nameText, Modifier.size(100.dp))
                            Box(
                                Modifier
                                    .align(Alignment.BottomEnd)
                                    .clickable {
                                        pickMedia.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )
                                    }) {
                                ImageIcon(R.drawable.ic_edit, 17, Color.White)
                            }
                        }
                        Spacer(Modifier.height(40.dp))

                        Row(
                            Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color.White)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                                .clickable { showEmoji = !showEmoji }
                                .padding(6.dp),
                            Arrangement.Center,
                            Alignment.CenterVertically) {
                            TextViewSemiBold(statusText.ifEmpty { "No Status" }, 15, Color.Black)
                            if (statusText.isNotEmpty()) Box(
                                Modifier
                                    .padding(start = 6.dp)
                                    .clickable { statusText = "" }) {
                                ImageIcon(R.drawable.ic_delete, 17, Color.Black)
                            }
                        }
                    }
                }

                item {
                    Box(
                        Modifier
                            .padding(top = 40.dp)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(MainColor)
                            .padding(10.dp)
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        TextViewSemiBold(emailText, 15, Color.White)
                    }
                }

                item {
                    TextField(
                        nameText,
                        { if (it.length <= 25) nameText = it },
                        Modifier
                            .padding(top = 10.dp)
                            .padding(10.dp)
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        placeholder = {
                            TextViewNormal(stringResource(R.string.enter_your_name), 14)
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

                item {
                    TextField(
                        usernameText,
                        { if (it.length <= 38) usernameText = it.removeSpecialChars() },
                        Modifier
                            .padding(top = 10.dp)
                            .padding(10.dp)
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        placeholder = {
                            TextViewNormal(stringResource(R.string.enter_your_username), 14)
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
            }

            if (showEmoji) AndroidView(
                { ctx ->
                    EmojiPickerView(ctx).apply {
                        emojiGridColumns = 9
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                        )

                        setOnEmojiPickedListener {
                            if (statusText.length <= 4) statusText += it.emoji.trim()
                        }
                    }
                },
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(Color.White)
            )
        }
    }

    LaunchedEffect(Unit) {
        val user = userInfo.firstOrNull()
        nameText = user?.name ?: ""
        profilePic = user?.photo ?: ""
        emailText = user?.email ?: ""
        usernameText = user?.username ?: ""
    }
}

@Composable
fun ContactListsInfo(close: () -> Unit) {
    val viewModel: PhoneNumberViewModel = hiltViewModel()
    val connectViewModel: ConnectViewModel = hiltViewModel()
    var searchText by remember { mutableStateOf("") }

    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                item {
                    TextField(
                        searchText,
                        { if (it.length <= 15) searchText = it },
                        Modifier
                            .padding(top = 20.dp)
                            .padding(10.dp)
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        placeholder = {
                            TextViewNormal(stringResource(R.string.search_email_name_username), 14)
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            connectViewModel.searchConnectUsers(searchText)
                        }),
                        trailingIcon = {
                            if (searchText.length > 3) {
                                IconButton({ connectViewModel.searchConnectUsers(searchText) }) {
                                    ImageIcon(R.drawable.ic_search, 24)
                                }
                            }
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

                when (val v = connectViewModel.connectSearch) {
                    ResponseResult.Empty -> {}
                    is ResponseResult.Error -> {}
                    ResponseResult.Loading -> item(4) {
                        FullUsersShimmerLoadingCard()
                    }

                    is ResponseResult.Success -> items(v.data) {
                        UserSearchInfo(it)
                    }
                }

                if (viewModel.usersOfZene.isNotEmpty()) item {
                    Spacer(Modifier.height(10.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 9.dp, vertical = 25.dp)
                    ) {
                        TextViewSemiBold(stringResource(R.string.in_your_contacts), 19)
                    }
                }

                items(viewModel.usersOfZene) {
                    UserSearchInfo(it)
                }

                if (viewModel.isUsersLoading) item(2) {
                    FullUsersShimmerLoadingCard()
                }

                if (viewModel.contactsUsers.isNotEmpty()) item {
                    Spacer(Modifier.height(10.dp))
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 9.dp, vertical = 25.dp)
                    ) {
                        TextViewSemiBold(stringResource(R.string.contacts), 19)
                    }
                }

                items(viewModel.contactsUsers) {
                    UserContactInfo(it)
                }
            }
        }

        LaunchedEffect(Unit) {
            viewModel.syncAllContacts()
        }
    }
}