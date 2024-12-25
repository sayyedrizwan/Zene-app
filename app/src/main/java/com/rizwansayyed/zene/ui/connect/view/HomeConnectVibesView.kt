package com.rizwansayyed.zene.ui.connect.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.api.APIResponse
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectVibesModel
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.LoadingView
import com.rizwansayyed.zene.ui.view.TextPoppins
import com.rizwansayyed.zene.ui.view.imgBuilder
import com.rizwansayyed.zene.ui.view.openSpecificIntent
import com.rizwansayyed.zene.viewmodel.RoomDBViewModel

@Composable
fun HomeConnectVibes(user: ZeneConnectContactsModel, close: () -> Unit) {
    val roomDB: RoomDBViewModel = hiltViewModel()

    Dialog(close, DialogProperties(usePlatformDefaultWidth = false)) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            when (val v = roomDB.vibesListsData) {
                APIResponse.Empty -> {}
                is APIResponse.Error -> {}
                APIResponse.Loading -> LoadingView(Modifier.size(24.dp))
                is APIResponse.Success -> {
                    val pagerState = rememberPagerState(pageCount = { v.data.size })
                    if (v.data.isNotEmpty()) {
                        Column(Modifier.fillMaxSize()) {
                            HorizontalPager(state = pagerState) { page ->
                                VibesImagesView(v.data, page, close) {
                                    v.data[page].id?.let { it1 -> roomDB.resetNewVibes(it1) }
                                }
                            }
                        }

                        ExtraInfoOnVibes(Modifier.align(Alignment.BottomCenter), pagerState)
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            roomDB.contactsAllVibes(user.number)
        }
    }
}

@Composable
fun VibesImagesView(data: List<ZeneConnectVibesModel>, page: Int,  close: () -> Unit, success: () -> Unit) {
    var isLoading by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxSize()) {
        AsyncImage(imgBuilder(data[page].imagePath),
            "",
            Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .background(Color.Black),
            contentScale = ContentScale.Crop,
            onLoading = {
                isLoading = true
            },
            onSuccess = {
                isLoading = false
                success()
            })

        Row(
            Modifier
                .padding(top = 2.dp, end = 4.dp)
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray)
                .padding(vertical = 5.dp, horizontal = 10.dp)
        ) {
            TextPoppins(data[page].timeAgo(), size = 15)
        }

        Row(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(vertical = 5.dp, horizontal = 10.dp)
        ) {
            Spacer(Modifier.weight(1f))

            data[page].getExtraDetails()?.let { d ->
                Row(
                    Modifier
                        .padding(vertical = 6.dp, horizontal = 5.dp)
                        .clickable {
                            close()
                            openSpecificIntent(d, listOf(d))
                        }
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.DarkGray)
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                ) {
                    ImageIcon(R.drawable.ic_copy_link, 27)
                }
            }

            Row(
                Modifier
                    .padding(vertical = 6.dp, horizontal = 5.dp)
                    .clickable {

                    }
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.DarkGray)
                    .padding(vertical = 5.dp, horizontal = 10.dp)
            ) {
                ImageIcon(R.drawable.ic_relieved, 27)
            }
        }

        Row(
            Modifier
                .padding(top = 2.dp, end = 4.dp)
                .align(Alignment.TopEnd)
                .clip(RoundedCornerShape(14.dp))
                .background(Color.DarkGray)
                .padding(vertical = 5.dp, horizontal = 10.dp)
        ) {
            TextPoppins(data[page].timeAgo(), size = 15)
        }

        if (isLoading) CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .width(45.dp),
            color = MainColor,
            trackColor = Color.White,
        )
    }
}

@Composable
fun ExtraInfoOnVibes(modifier: Modifier = Modifier, pagerState: PagerState) {
    var currentProgress by remember { mutableStateOf(0f) }

    Row(
        modifier
            .fillMaxWidth()
            .padding(5.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            progress = { currentProgress },
            modifier = Modifier.width(35.dp),
            color = MainColor,
            trackColor = Color.White,
        )

        Spacer(Modifier.weight(1f))
    }

    LaunchedEffect(pagerState.currentPage, pagerState.pageCount) {
        currentProgress = (pagerState.currentPage.toFloat() + 1) / pagerState.pageCount
    }
}