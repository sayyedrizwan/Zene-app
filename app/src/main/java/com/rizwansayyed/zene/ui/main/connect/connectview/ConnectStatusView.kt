package com.rizwansayyed.zene.ui.main.connect.connectview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.ui.main.connect.view.PhoneNumberVerificationView
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.HorizontalShimmerLoadingCard
import com.rizwansayyed.zene.ui.view.ItemCardView
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.viewmodel.HomeViewModel

@Composable
fun ConnectStatusView() {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val userInfo by DataStorageManager.userInfo.collectAsState(null)
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
                .background(MainColor)
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
                    is ResponseResult.Success ->
                        LazyRow(Modifier.fillMaxWidth()) {
                            items(v.data) {
                                ItemCardView(it)
                            }
                        }
                }
            }

            item {
                if ((userInfo?.phoneNumber?.length ?: 0) < 4) PhoneNumberVerificationView()
            }

            if ((userInfo?.phoneNumber?.length ?: 0) > 4) item {
                ConnectRecentContactsView()
            }
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.connectNearMusic()
    }
}