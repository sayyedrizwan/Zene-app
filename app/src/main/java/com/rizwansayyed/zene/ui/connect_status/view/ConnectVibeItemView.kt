package com.rizwansayyed.zene.ui.connect_status.view

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rizwansayyed.zene.viewmodel.ConnectViewModel

@Composable
fun ConnectVibeItemView(viewModel: ConnectViewModel) {
    Spacer(Modifier.height(10.dp))

    viewModel.connectFileSelected

}