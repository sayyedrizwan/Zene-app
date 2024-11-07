package com.rizwansayyed.zene.ui.earphonetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.data.db.DataStoreManager.earphoneDevicesDB
import com.rizwansayyed.zene.ui.earphonetracker.view.AddEarphoneView
import com.rizwansayyed.zene.ui.earphonetracker.view.EarphonesDeviceItemsView
import com.rizwansayyed.zene.ui.earphonetracker.view.HeaderPermissionsView
import com.rizwansayyed.zene.ui.earphonetracker.view.NoHeadphoneAddedView
import com.rizwansayyed.zene.ui.earphonetracker.view.TopEarphoneHeaderView
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.ui.theme.ZeneTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class EarphoneTrackerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val devices by earphoneDevicesDB.collectAsState(initial = emptyArray())
            var status by remember { mutableStateOf(false) }

            ZeneTheme {
                var job by remember { mutableStateOf<Job?>(null) }
                val coroutine = rememberCoroutineScope()

                Box(
                    Modifier
                        .fillMaxSize()
                        .background(DarkCharcoal)
                ) {
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp)
                    ) {
                        item {
                            TopEarphoneHeaderView()
                        }

                        if ((devices?.size ?: 0) <= 0) item {
                            NoHeadphoneAddedView()
                        }

                        if ((devices?.size ?: 0) > 0) item {
                            HeaderPermissionsView()
                        }

                        items(devices ?: emptyArray()) {
                            EarphonesDeviceItemsView(it, status)
                        }
                    }

                    AddEarphoneView(Modifier.align(Alignment.BottomEnd))
                }
                DisposableEffect(Unit) {
                    job?.cancel()
                    job = coroutine.launch {
                        while (true) {
                            delay(1.seconds)
                            status = !status
                        }
                    }
                    onDispose {
                        job?.cancel()
                    }
                }
            }
        }
    }
}
