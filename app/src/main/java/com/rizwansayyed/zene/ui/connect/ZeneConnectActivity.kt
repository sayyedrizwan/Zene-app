package com.rizwansayyed.zene.ui.connect

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rizwansayyed.zene.ui.connect.view.SendVibesEditorView
import com.rizwansayyed.zene.ui.connect.view.ZeneConnectButtonView
import com.rizwansayyed.zene.ui.connect.view.ZeneImageEditorView
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ZeneConnectActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZeneTheme {
                var sendVibesView by remember { mutableStateOf(false) }
                var image by remember { mutableStateOf<Uri?>(null) }

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    LazyColumn(Modifier.fillMaxSize()) {
                        item {
                            ZeneConnectButtonView {
                                sendVibesView = true
                            }
                        }

                        item {
                            ZeneConnectButtonView {
                                sendVibesView = true
                            }
                        }
                    }
                }

                if (sendVibesView) SendVibesEditorView {
                    sendVibesView = false
                    image = it
                }

                if (image != null) ZeneImageEditorView(image!!)
            }
        }
    }
}
