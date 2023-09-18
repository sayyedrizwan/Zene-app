package com.rizwansayyed.zene.presenter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rizwansayyed.zene.presenter.theme.DarkBlack


@Composable
fun MainHomePageView() {
    val columnModifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .background(if (isSystemInDarkTheme()) DarkBlack else Color.White)

    Column(columnModifier) {
        repeat(1000) {
            Text(text = "1111111", color = Color.Black)
        }
    }
}