package com.rizwansayyed.zene.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.viewmodel.MyLibraryViewModel

@Composable
fun MyPlaylistView(id: String) {
    val myLibraryViewModel: MyLibraryViewModel = hiltViewModel()

    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(Modifier.height(100.dp)) }

    }

    LaunchedEffect(Unit) {

//        if (myLibraryViewModel.savedIsLoading !is ResponseResult.Success) homeViewModel.similarPlaylistsData(
//            id
//        )

        return@LaunchedEffect
    }
}

@Composable
fun TopLiked(modifier: Modifier = Modifier) {
    
}