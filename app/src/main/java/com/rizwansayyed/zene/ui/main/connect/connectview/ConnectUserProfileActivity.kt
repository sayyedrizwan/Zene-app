package com.rizwansayyed.zene.ui.main.connect.connectview

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
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
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                    when (val v = connectViewModel.connectUserInfo) {
                        ResponseResult.Empty -> {}
                        is ResponseResult.Error -> {}
                        ResponseResult.Loading -> CircularLoadingView()
                        is ResponseResult.Success -> {
                            BottomSheetScaffold(
                                sheetContent = {
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        TextViewSemiBold(v.data.user?.name ?: "", 16, line = 1)
                                        TextViewNormal("@${v.data.user?.username}", 13, line = 1)
                                        TextViewNormal(v.data.user?.country ?: "", 13, line = 1)
                                        Spacer(Modifier.height(124.dp))
                                        TextViewSemiBold(v.data.user?.name ?: "", 16, line = 1)
                                        TextViewNormal("@${v.data.user?.username}", 13, line = 1)
                                        TextViewNormal(v.data.user?.country ?: "", 13, line = 1)
                                        Spacer(Modifier.height(124.dp))
                                        TextViewSemiBold(v.data.user?.name ?: "", 16, line = 1)
                                        TextViewNormal("@${v.data.user?.username}", 13, line = 1)
                                        TextViewNormal(v.data.user?.country ?: "", 13, line = 1)
                                        Spacer(Modifier.height(124.dp))
                                        TextViewSemiBold(v.data.user?.name ?: "", 16, line = 1)
                                        TextViewNormal("@${v.data.user?.username}", 13, line = 1)
                                        TextViewNormal(v.data.user?.country ?: "", 13, line = 1)
                                        Spacer(Modifier.height(124.dp))
                                        TextViewSemiBold(v.data.user?.name ?: "", 16, line = 1)
                                        TextViewNormal("@${v.data.user?.username}", 13, line = 1)
                                        TextViewNormal(v.data.user?.country ?: "", 13, line = 1)
                                        Spacer(Modifier.height(124.dp))
                                        TextViewSemiBold(v.data.user?.name ?: "", 16, line = 1)
                                        TextViewNormal("@${v.data.user?.username}", 13, line = 1)
                                        TextViewNormal(v.data.user?.country ?: "", 13, line = 1)
                                        Spacer(Modifier.height(124.dp))
                                    }
                                },
                                Modifier.fillMaxSize(),
                                sheetPeekHeight = (screenHeight * 0.6f),
                                sheetContentColor = MainColor,
                                sheetContainerColor = MainColor
                            ) {
                                UserBigProfilePhoto(v.data.user)
                            }
                        }
                    }
                }
                LaunchedEffect(Unit) {
                    connectViewModel.connectUserInfo(email)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UserBigProfilePhoto(user: ConnectUserResponse?) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(Modifier.fillMaxSize()) {
        GlideImage(
            user?.profile_photo,
            user?.name,
            Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 10.dp)
                .fillMaxWidth()
                .height(screenHeight * 0.55f),
            contentScale = ContentScale.Crop
        )
    }
}