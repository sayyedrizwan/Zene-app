package com.rizwansayyed.zene.ui.main.connect.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.view.CircularLoadingView
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
                }
                LaunchedEffect(Unit) {
                    connectViewModel.connectUserInfo(email)
                }
            }
        }
    }
}