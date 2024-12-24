package com.rizwansayyed.zene.ui.connect.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.viewmodel.RoomDBViewModel

@Composable
fun HomeConnectVibes(user: ZeneConnectContactsModel, close: () -> Unit) {
    val roomDB: RoomDBViewModel = hiltViewModel()

    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Column(Modifier.fillMaxSize()) {
                when (val v = roomDB.vibesListsData) {
                    APIResponse.Empty -> {}
                    is APIResponse.Error -> {}
                    APIResponse.Loading -> LoadingView(Modifier.size(24.dp))
                    is APIResponse.Success -> {
                        val pagerState = rememberPagerState(pageCount = { v.data.size })
                       if (v.data.isNotEmpty()) HorizontalPager(state = pagerState) { page ->
                            AsyncImage(
                                imgBuilder(v.data[page].imagePath), "",
                                Modifier
                                    .fillMaxSize()
                                    .background(Color.Black),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            roomDB.contactsAllVibes(user.number)
        }
    }

}