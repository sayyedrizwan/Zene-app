package com.rizwansayyed.zene.ui.settings.importplaylists.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.settings.importplaylists.model.TrackItemCSV
import com.rizwansayyed.zene.ui.settings.importplaylists.model.parseCsvFileAndGroup
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewBoldBig
import com.rizwansayyed.zene.ui.view.TextViewLight
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.toast
import java.io.File
import kotlin.random.Random

@Composable
fun ImportSongSyncView(selectFiled: File, error: () -> Unit) {
    var list by remember { mutableStateOf<Map<String, List<TrackItemCSV>>>(emptyMap()) }
    var isShowingFull by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    val noValidFile = stringResource(R.string.not_a_valid_csv_file)

    LazyColumn(Modifier.fillMaxSize()) {
        item { Spacer(Modifier.height(80.dp)) }

        list.entries.forEach { (title, tracks) ->
            item {
                Column(
                    Modifier
                        .padding(horizontal = 6.dp)
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .background(MainColor)
                        .padding(horizontal = 18.dp)
                ) {
                    Spacer(Modifier.height(15.dp))
                    TextViewBoldBig(title, 25)
                    Spacer(Modifier.height(15.dp))
                }
            }

            items(tracks.take(5)) {
                Column(
                    Modifier
                        .padding(horizontal = 6.dp)
                        .fillMaxWidth()
                        .background(MainColor)
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                ) {
                    if (tracks.isEmpty()) {
                        TextViewNormal(stringResource(R.string.no_song_found_in_playlist), 18)
                    } else {
                        TextViewNormal(it.trackName, 16, line = 2)
                        TextViewLight(it.artistName, 14, line = 2)
                    }
                }
            }

            item {
                if (tracks.size > 5) {
                    val track = tracks[6]
                    Box(Modifier.fillMaxWidth(), Alignment.Center) {
                        Column(
                            Modifier
                                .padding(horizontal = 6.dp)
                                .fillMaxWidth()
                                .background(MainColor)
                                .padding(horizontal = 18.dp, vertical = 10.dp)
                        ) {
                            TextViewNormal(track.trackName, 16, line = 2)
                            TextViewLight(track.artistName, 14, line = 2)
                        }

                        if (tracks.size > 6) Box(
                            Modifier
                                .padding(horizontal = 6.dp)
                                .offset(y = (-5).dp)
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            MainColor.copy(0.5f), MainColor, MainColor
                                        )
                                    )
                                ), Alignment.BottomCenter
                        ) {
                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .padding(top = 8.dp)
                            ) {
                                Text(text = "Load More")
                            }
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(35.dp)) }

        }


        item { Spacer(Modifier.height(80.dp)) }
    }

//    LazyColumn(
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        list.entries.forEach { (title, tracks) ->
//            // Add header using item() inside LazyColumn scope
//            item {
//                TrackListCard(title = title, tracks = tracks)
//            }
//        }
//    }
//
//    LazyColumn(Modifier.fillMaxSize()) {
//        item { Spacer(Modifier.height(80.dp)) }
//        items(list.entries.toList()) { (title, tracks) ->
//            TrackListCard(title = title, tracks = tracks)
//        }
//        item { Spacer(Modifier.height(80.dp)) }
//
////        list.forEach { (playlistName, songs) ->
////            item {
////                Row(
////                    Modifier
////                        .padding(vertical = 8.dp)
////                        .fillMaxWidth()
////                ) {
////                    TextViewBold(playlistName, 18)
////                }
////            }
////
////            items(songs) { track ->
////                Column(modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)) {
////                    TextViewNormal(track.trackName, 16)
////                    TextViewNormal("by ${track.artistName}", 15)
////                }
////            }
////        }
//    }

    LaunchedEffect(Unit) {
        val v = parseCsvFileAndGroup(selectFiled)
        if (v != null) list = v
        else {
            noValidFile.toast()
            error()
        }
    }
}

@Composable
fun ImportSongItem(modifier: Modifier = Modifier) {

}
