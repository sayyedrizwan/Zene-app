package com.rizwansayyed.zene.ui.main.view

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.viewmodel.NavigationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LongPressSheetView(viewModel: NavigationViewModel) {

    if (viewModel.showMediaInfoSheet != null) ModalBottomSheet(
        { viewModel.setShowMediaInfo(null) }, contentColor = MainColor, containerColor = MainColor
    ) {

    }
}