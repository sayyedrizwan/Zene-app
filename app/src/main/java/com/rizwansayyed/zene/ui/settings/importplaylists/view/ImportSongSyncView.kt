package com.rizwansayyed.zene.ui.settings.importplaylists.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.ui.settings.importplaylists.model.TrackItemCSV
import com.rizwansayyed.zene.ui.settings.importplaylists.model.parseCsvFileAndGroup
import com.rizwansayyed.zene.ui.theme.MainColor
import com.rizwansayyed.zene.ui.view.TextViewBold
import com.rizwansayyed.zene.ui.view.TextViewBoldBig
import com.rizwansayyed.zene.ui.view.TextViewNormal
import com.rizwansayyed.zene.utils.MainUtils.toast
import java.io.File
import kotlin.random.Random

@Composable
fun ImportSongSyncView(selectFiled: File, error: () -> Unit) {
    var list by remember { mutableStateOf<Map<String, List<TrackItemCSV>>>(emptyMap()) }
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
                    Spacer(Modifier.height(5.dp))
                    TextViewBoldBig(title, 20)
                    Spacer(Modifier.height(10.dp))
                }
            }

            items(tracks) {
                Column(
                    Modifier
                        .padding(horizontal = 6.dp)
                        .fillMaxWidth()
                        .background(MainColor)
                        .padding(horizontal = 18.dp)
                ) {
                    if (tracks.isEmpty()) {
                        TextViewNormal(stringResource(R.string.no_song_found_in_playlist), 18)
                    } else {
                        TextViewNormal(it.trackName, 18)
                    }
                }
            }
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

@Composable
fun TrackListCard(title: String, tracks: List<TrackItemCSV>) {
    var showAll by remember { mutableStateOf(false) }
    val displayedTracks = if (showAll) tracks else tracks.take(7)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Display the tracks list in LazyColumn
            LazyColumn(
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(displayedTracks) { track ->
                    Text(
                        text = track.trackName, // Assuming TrackItemCSV has a 'trackName' property
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            // Show "Load More" button if there are more than 7 items
            if (tracks.size > 7 && !showAll) {
                Button(
                    onClick = { showAll = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text(text = "Load More")
                }
            }
        }
    }
}

private fun generateContrastingColor(): Color {
    var r: Int
    var g: Int
    var b: Int

    // Keep generating random colors until we find one with enough contrast
    do {
        r = Random.nextInt(256)
        g = Random.nextInt(256)
        b = Random.nextInt(256)
    } while (ColorUtils.calculateLuminance(
            android.graphics.Color.rgb(
                r, g, b
            )
        ) > 0.7
    )

    // Return a Compose-compatible color
    return Color(android.graphics.Color.rgb(r, g, b))
}