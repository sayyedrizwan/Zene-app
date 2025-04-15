package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opencsv.CSVReader
import com.rizwansayyed.zene.ui.settings.importplaylists.model.TrackItemCSV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileReader

class ImportPlaylistViewModel : ViewModel() {

    var selectedFile by mutableStateOf<File?>(null)
    var songList by mutableStateOf<Map<String, List<TrackItemCSV>>>(emptyMap())
    var isShowingFullSongs = mutableStateMapOf<String, Boolean>()
    var selectDialogTitle by mutableStateOf("")
    var selectDialogSong = mutableStateListOf<TrackItemCSV>()

    fun setFile(v: File?) {
        selectedFile = v
        if (v != null) parseCsvFileAndGroup(v)
    }

    fun isShowingFullSong(title: String) {
        isShowingFullSongs[title] = true
    }

    fun setDialogTitleAndSong(title: String, list: List<TrackItemCSV>) {
        selectDialogTitle = title
        selectDialogTitle = title
        isShowingFullSongs[title] = true
    }


    private fun parseCsvFileAndGroup(file: File) = viewModelScope.launch(Dispatchers.IO) {
        val groupedSongs = mutableMapOf<String, MutableList<TrackItemCSV>>()
        val expectedHeader = arrayOf("Track name", "Artist name", "Album", "Playlist name", "Type")

        try {
            CSVReader(FileReader(file)).use { reader ->
                val allRows = reader.readAll()

                if (allRows.isEmpty()) {
                    selectedFile = null
                    return@launch
                }

                val header = allRows[0]

                for (expectedColumn in expectedHeader) {
                    if (!header.contains(expectedColumn)) {
                        selectedFile = null
                        return@launch
                    }
                }

                for ((index, row) in allRows.withIndex()) {
                    if (index == 0 || row.size < 5) continue

                    val trackName = row[0].trim()
                    val artistName = row[1].trim()
                    val album = row[2].trim()
                    val playlistName = row[3].trim()
                    val type = row[4].trim()

                    if (artistName.isBlank() || album.isBlank()) continue
                    if (type != "Playlist" && type != "Favorite") continue

                    val song = TrackItemCSV(trackName, artistName, album)
                    groupedSongs.getOrPut(playlistName) { mutableListOf() }.add(song)
                }
            }

            songList = groupedSongs
        } catch (e: Exception) {
            selectedFile = null
        }
    }
}