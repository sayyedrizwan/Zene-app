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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@AndroidEntryPoint
class EarphoneTrackerActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val devices by earphoneDevicesDB.collectAsState(initial = emptyArray())

            ZeneTheme {
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
                            EarphonesDeviceItemsView(it)
                        }
                    }

                    AddEarphoneView(Modifier.align(Alignment.BottomEnd))
                }
            }
        }
    }
}
