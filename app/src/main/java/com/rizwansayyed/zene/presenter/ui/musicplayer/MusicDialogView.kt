package com.rizwansayyed.zene.presenter.ui.musicplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.rizwansayyed.zene.domain.MusicDataWithArtists
import com.rizwansayyed.zene.presenter.theme.BlackColor
import com.rizwansayyed.zene.presenter.theme.MainColor
import com.rizwansayyed.zene.presenter.ui.TextSemiBold
import com.rizwansayyed.zene.presenter.ui.TextThin
import com.rizwansayyed.zene.viewmodel.HomeNavViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicDialogSheet() {
    val homeNavModel: HomeNavViewModel = hiltViewModel()
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        { homeNavModel.setSongDetailsDialog(null) }, Modifier.fillMaxWidth(), sheetState,
        containerColor = MainColor, contentColor = BlackColor
    ) {
        MusicDialogView(homeNavModel)
    }
}

@Composable
fun MusicDialogView(homeNavModel: HomeNavViewModel) {
    Column(Modifier.fillMaxWidth()) {
        TextAndImageSideBySide(
            homeNavModel.songDetailDialog?.name ?: "",
            homeNavModel.songDetailDialog?.artists ?: "",
            homeNavModel.songDetailDialog?.thumbnail ?: ""
        )


        Spacer(Modifier.height(130.dp))
    }
}

@Composable
fun TextAndImageSideBySide(name: String, artists: String, img: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Column(
            Modifier
                .padding(10.dp)
                .weight(1f)
        ) {
            TextSemiBold(
                name, Modifier.fillMaxWidth(), size = 34
            )

            Spacer(Modifier.height(5.dp))

            TextThin(
                artists, Modifier.fillMaxWidth(), color = Color.Gray, size = 29, singleLine = true
            )
        }

        AsyncImage(
            img, name, Modifier
                .padding(end = 10.dp)
                .size(120.dp)
                .clip(RoundedCornerShape(10))
        )
    }
}