package com.rizwansayyed.zene.ui.connect_status.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.viewmodel.GifViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ConnectVibeCommentsView(data: ConnectFeedDataResponse?, close: () -> Unit) {
    val viewModel: GifViewModel = hiltViewModel()

    ModalBottomSheet(
        close, contentColor = Color.Black, containerColor = Color.Black
    ) {
        LazyColumn(Modifier.fillMaxWidth()) {
            stickyHeader {
                Spacer(Modifier.height(20.dp))
                TextViewBold(stringResource(R.string.gif_spress), 18, center = true)
                Spacer(Modifier.height(2.dp))
                TextViewNormal(
                    stringResource(R.string.comment_express_yourself_with_gif), 14, center = true
                )
                Spacer(Modifier.height(20.dp))
            }


            item {
                Spacer(Modifier.height(100.dp))
            }
        }
    }
}