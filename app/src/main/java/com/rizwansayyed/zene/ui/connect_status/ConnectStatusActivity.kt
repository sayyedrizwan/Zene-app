package com.rizwansayyed.zene.ui.connect_status

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.connect_status.view.ConnectAddJam
import com.rizwansayyed.zene.ui.connect_status.view.ConnectAttachFiles
import com.rizwansayyed.zene.ui.connect_status.view.ConnectEmojiView
import com.rizwansayyed.zene.ui.connect_status.view.ConnectStatusCaptionView
import com.rizwansayyed.zene.ui.connect_status.view.ConnectStatusTopColumView
import com.rizwansayyed.zene.ui.connect_status.view.ConnectStatusTopHeaderView
import com.rizwansayyed.zene.ui.connect_status.view.ConnectVibeItemView
import com.rizwansayyed.zene.ui.connect_status.view.ConnectVibingSnapView
import com.rizwansayyed.zene.ui.main.connect.profile.SettingsViewSimpleItems
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.ZeneTheme
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

                        ConnectVibeItemView(connectViewModel.connectFileSelected)

                        ConnectStatusCaptionView(caption)

                        ConnectVibingSnapView(connectViewModel)
                        ConnectAttachFiles(connectViewModel)

                        ConnectAddJam(connectViewModel)

                        ConnectEmojiView(connectViewModel)

                        Spacer(Modifier.height(30.dp))
                        SettingsViewSimpleItems(R.drawable.ic_location, R.string.add_location) {

                        }
                        Spacer(Modifier.height(150.dp))
                    }
                }
                LaunchedEffect(Unit) {

                }
            }
        }
    }

}