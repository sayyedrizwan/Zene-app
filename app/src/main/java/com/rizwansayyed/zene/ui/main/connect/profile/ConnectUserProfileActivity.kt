package com.rizwansayyed.zene.ui.main.connect.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.view.FullUsersShimmerLoadingCard
import com.rizwansayyed.zene.ui.view.ImageIcon
import com.rizwansayyed.zene.ui.view.ShimmerEffect
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectUserProfileActivity : ComponentActivity() {

    private val connectViewModel: ConnectViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val email = intent.getStringExtra(Intent.ACTION_MAIN) ?: return@setContent
            ZeneTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                    when (val v = connectViewModel.connectUserInfo) {
                        ResponseResult.Empty -> {}
                        is ResponseResult.Error -> {}
                        ResponseResult.Loading -> ConnectUserLoading()
                        is ResponseResult.Success -> {
                            BottomSheetScaffold(
                                { ConnectProfileDetailsView(v.data) },
                                Modifier.fillMaxSize(),
                                sheetPeekHeight = (screenHeight * 0.6f),
                                sheetContentColor = Color.Black,
                                sheetContainerColor = Color.Black
                            ) {
                                ConnectProfileProfilePhotoView(v.data.user)
                            }
                        }
                    }

                    Box(
                        Modifier
                            .padding(top = 50.dp, start = 15.dp)
                            .align(Alignment.TopStart)
                            .rotate(90f)
                            .clickable { finish() }) {
                        ImageIcon(R.drawable.ic_arrow_down, size = 25)
                    }
                }
                LaunchedEffect(Unit) {
                    connectViewModel.connectUserInfo(email)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectUserLoading() {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    BottomSheetScaffold(
        {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp)
            ) {
                FullUsersShimmerLoadingCard()
            }
        },
        Modifier.fillMaxSize(),
        sheetPeekHeight = (screenHeight * 0.6f),
        sheetContentColor = Color.Black,
        sheetContainerColor = Color.Black
    ) {
        Box(Modifier.fillMaxSize()) {
            ShimmerEffect(
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(bottom = 10.dp)
                    .fillMaxWidth()
                    .height(screenHeight * 0.55f),
            )
        }
    }
}