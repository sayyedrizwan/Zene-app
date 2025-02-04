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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
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

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(DarkCharcoal)
                        .padding(horizontal = 5.dp)
                ) {
                    ConnectStatusTopColumView {
                        ConnectStatusTopHeaderView()

                        ConnectVibeItemView(connectViewModel.connectFileSelected, false)

                        ConnectStatusCaptionView(caption)

                        ConnectVibingSnapView(connectViewModel)

                        ConnectAttachFiles(connectViewModel)

                        ConnectAddJam(connectViewModel)

                        ConnectEmojiView(connectViewModel)

                        ConnectAddLocation(connectViewModel)
                        Spacer(Modifier.height(250.dp))
                    }

                    Column(
                        Modifier
                            .padding(bottom = 60.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        Arrangement.Center, Alignment.CenterHorizontally
                    ) {
                        ButtonHeavy(stringResource(R.string.share)) {

                        }
                        Spacer(Modifier.height(14.dp))
                        ButtonWithBorder(R.string.cancel) {
                            finish()
                        }
                    }
                }
            }
        }
    }

}