package com.rizwansayyed.zene.ui.home.homeui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.rizwansayyed.zene.BaseApplication.Companion.dataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Composable
fun HomepageView() {
    val header by dataStoreManager.albumHeaderData.collectAsState(initial = runBlocking(Dispatchers.IO) { dataStoreManager.albumHeaderData.first() })

    Column {

    }
}