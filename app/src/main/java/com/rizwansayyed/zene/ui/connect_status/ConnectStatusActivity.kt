package com.rizwansayyed.zene.ui.connect_status

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.ui.view.TextViewSemiBold
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectStatusActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZeneTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(DarkCharcoal)
                        .padding(horizontal = 5.dp)
                ) {
                    Column(
                        Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxSize()
                            .background(DarkCharcoal)
                    ) {
                        Spacer(Modifier.height(70.dp))
                        Column(
                            Modifier.padding(horizontal = 2.dp),
                            Arrangement.Center, Alignment.Start
                        ) {
                            TextViewSemiBold(stringResource(R.string.upload_a_vibes), 18)
                            TextViewNormal(
                                stringResource(R.string.vibes_will_be_shared_and_expire_after_24),
                                12
                            )
                        }

                        Spacer(Modifier.height(50.dp))

                        var text by remember { mutableStateOf("") }

                        OutlinedTextField(
                            value = text,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = { text = it },
                            label = { Text("Label") }
                        )
                    }
                }
            }
        }
    }

}