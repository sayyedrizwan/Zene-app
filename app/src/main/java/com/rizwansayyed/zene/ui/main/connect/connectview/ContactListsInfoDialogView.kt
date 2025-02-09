package com.rizwansayyed.zene.ui.main.connect.connectview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.FullUsersShimmerLoadingCard
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import com.rizwansayyed.zene.ui.view.UserContactInfo
import com.rizwansayyed.zene.ui.view.UserSearchInfo
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import com.rizwansayyed.zene.viewmodel.PhoneNumberViewModel


@Composable
fun ContactListsInfo() {
    val viewModel: PhoneNumberViewModel = hiltViewModel()
    val connectViewModel: ConnectViewModel = hiltViewModel()
    var searchText by remember { mutableStateOf("") }
    val enterAValidName = stringResource(R.string.enter_a_valid_name_or_username)

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(MainColor)
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
                keyboardActions = KeyboardActions {
                    if (searchText.length <= 3) {
                        enterAValidName.toast()
                        return@KeyboardActions
                    }
                    connectViewModel.searchConnectUsers(searchText)
                },
                trailingIcon = {
                    if (searchText.length > 3) {
                        IconButton({
                            if (searchText.length <= 3) {
                                enterAValidName.toast()
                                return@IconButton
                            }
                            connectViewModel.searchConnectUsers(searchText)
                        }) {
                            ImageIcon(R.drawable.ic_search, 24)
                        }
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Black,
                    unfocusedContainerColor = Color.Black,
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

            is ResponseResult.Success -> {
                if (v.data.isNotEmpty()) items(v.data) {
                    UserSearchInfo(it)
                } else item {
                    TextViewLight(
                        stringResource(R.string.no_user_found), 15, center = true, line = 1
                    )
                }

                item {
                    Spacer(Modifier.height(15.dp))
                }
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

    LaunchedEffect(Unit) {
        viewModel.syncAllContacts()
    }
}