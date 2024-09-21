package com.rizwansayyed.zene.ui.radio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.ui.theme.DarkCharcoal
import com.rizwansayyed.zene.viewmodel.RadioViewModel

@Composable
fun RadioView() {
    val radioViewModel: RadioViewModel = hiltViewModel()
    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(DarkCharcoal)
    ) {

    }

    LaunchedEffect(Unit) {
        radioViewModel.init()
    }
}