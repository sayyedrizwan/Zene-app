package com.rizwansayyed.zene.ui.main.connect.connectview

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.FullUsersShimmerLoadingCard
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.ui.view.UserContactInfo
import com.rizwansayyed.zene.ui.view.UserSearchInfo
import com.rizwansayyed.zene.utils.MainUtils.openAppSettings
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import com.rizwansayyed.zene.viewmodel.PhoneNumberViewModel

@Composable
fun ConnectRecentContactsView() {
    val needContact = stringResource(R.string.need_location_permission_to_read_contact)
    var contactsView by remember { mutableStateOf(false) }
    val contactPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) contactsView = true
            else {
                needContact.toast()
                openAppSettings()
            }
        }

    Spacer(Modifier.height(52.dp))
    Row(Modifier.padding(horizontal = 9.dp)) {
        TextViewBold(stringResource(R.string.friends), 18)
        Spacer(Modifier.weight(1f))
        Box(Modifier.clickable { contactPermission.launch(Manifest.permission.READ_CONTACTS) }) {
            ImageIcon(R.drawable.ic_user_search, 23)
        }
    }
    Spacer(Modifier.height(12.dp))

    if (contactsView) ContactListsInfo {
        contactsView = false
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
                        {
                            if (it.length <= 15) searchText = it
                        },
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