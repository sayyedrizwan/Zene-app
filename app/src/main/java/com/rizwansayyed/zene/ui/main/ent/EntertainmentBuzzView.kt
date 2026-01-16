package com.rizwansayyed.zene.ui.main.ent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun EntertainmentBuzzView(viewModel: EntertainmentViewModel) {



    LaunchedEffect(Unit) {
        viewModel.entBuzzNews()
    }
}