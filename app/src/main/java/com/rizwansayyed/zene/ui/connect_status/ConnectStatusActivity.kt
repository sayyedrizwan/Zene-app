package com.rizwansayyed.zene.ui.connect_status

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.ui.connect_status.view.ConnectAddJam
import com.rizwansayyed.zene.ui.connect_status.view.ConnectAddLocation
import com.rizwansayyed.zene.ui.connect_status.view.ConnectAttachFiles
import com.rizwansayyed.zene.ui.connect_status.view.ConnectEmojiView
import com.rizwansayyed.zene.ui.connect_status.view.ConnectStatusCaptionView
import com.rizwansayyed.zene.ui.connect_status.view.ConnectStatusTopColumView
import com.rizwansayyed.zene.ui.connect_status.view.ConnectStatusTopHeaderView
import com.rizwansayyed.zene.ui.connect_status.view.ConnectVibeItemView
import com.rizwansayyed.zene.ui.connect_status.view.ConnectVibingSnapView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.view.ButtonHeavy
import com.rizwansayyed.zene.ui.view.ButtonWithBorder
import com.rizwansayyed.zene.ui.view.CircularLoadingView
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.viewmodel.ConnectViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectStatusActivity : ComponentActivity() {

    private val connectViewModel: ConnectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZeneTheme {
                val caption = remember { mutableStateOf("") }
                val pleaseEnterACaption =
                    stringResource(R.string.error_uploading_please_try_again_later)

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(DarkCharcoal)
                        .padding(horizontal = 5.dp)
                ) {
                    ConnectStatusTopColumView {
                        ConnectStatusTopHeaderView()

                        ConnectVibeItemView(connectViewModel.connectFileSelected)

                        ConnectStatusCaptionView(caption)

                        ConnectVibingSnapView(connectViewModel)

                        ConnectAttachFiles(connectViewModel)

                        ConnectAddJam(connectViewModel)

                        ConnectEmojiView(connectViewModel)

                        ConnectAddLocation(connectViewModel)

                        Spacer(Modifier.height(250.dp))
                    }

                    LaunchedEffect(caption.value) {
                        connectViewModel.updateCaptionInfo(caption.value)
                    }

                    when (val v = connectViewModel.isConnectSharing) {
                        ResponseResult.Empty -> ShareButtonUI(Modifier.align(Alignment.BottomCenter))
                        is ResponseResult.Error -> {
                            pleaseEnterACaption.toast()
                            ShareButtonUI(Modifier.align(Alignment.BottomCenter))
                        }

                        ResponseResult.Loading -> Column(
                            Modifier
                                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                                .background(Color.Black)
                                .padding(bottom = 40.dp, top = 20.dp)
                                .align(Alignment.BottomCenter)
                        ) {
                            TextViewNormal(connectViewModel.loadingTypeForFile, 15, center = true)
                            CircularLoadingView()
                        }

                        is ResponseResult.Success -> {
                            if (v.data.status == true) {
                                finish()
                            } else {
                                pleaseEnterACaption.toast()
                                ShareButtonUI(Modifier.align(Alignment.BottomCenter))
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ShareButtonUI(modifier: Modifier = Modifier) {
        val pleaseEnterACaption = stringResource(R.string.please_enter_valid_caption)

        Column(
            modifier
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(Color.Black)
                .padding(bottom = 60.dp, top = 20.dp)
                .fillMaxWidth(),
            Arrangement.Center,
            Alignment.CenterHorizontally
        ) {
            ButtonHeavy(stringResource(R.string.share)) {
                if ((connectViewModel.connectFileSelected?.caption?.length
                        ?: "".length) <= 3
                ) {
                    pleaseEnterACaption.toast()
                    return@ButtonHeavy
                }
                connectViewModel.uploadAVibe()
            }
            Spacer(Modifier.height(14.dp))
            ButtonWithBorder(R.string.cancel) {
                finish()
            }
        }
    }
}