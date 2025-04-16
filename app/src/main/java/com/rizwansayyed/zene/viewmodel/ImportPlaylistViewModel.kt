package com.rizwansayyed.zene.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opencsv.CSVReader
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.ui.settings.importplaylists.model.TrackItemCSV
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileReader
import javax.inject.Inject

@HiltViewModel
class ImportPlaylistViewModel @Inject constructor(
    private val zeneAPI: ZeneAPIInterface
) : ViewModel() {

    var selectedFile by mutableStateOf<File?>(null)
    var songList by mutableStateOf<Map<String, List<TrackItemCSV>>>(emptyMap())
    var isShowingFullSongs = mutableStateMapOf<String, Boolean>()
    var importedSuccessPlaylist = mutableStateMapOf<String, Boolean>()
    var selectDialogTitle by mutableStateOf<String?>(null)
    private var selectDialogSong = mutableStateListOf<TrackItemCSV>()
    var isSyncing by mutableStateOf(false)

    fun setFile(v: File?) {
        selectedFile = v
        if (v != null) parseCsvFileAndGroup(v)
    }

    fun isShowingFullSong(title: String) {
        isShowingFullSongs[title] = true
    }

    fun setDialogTitleAndSong(title: String?, list: List<TrackItemCSV>) {
        selectDialogTitle = title
        selectDialogSong.clear()
        selectDialogSong.addAll(list)
    }

    fun syncLikedImport() = viewModelScope.launch(Dispatchers.IO) {
        isSyncing = true
        selectDialogSong.chunked(50).forEach {
            val list = it.map { s -> "${s.trackName} - ${s.artistName}" }
            zeneAPI.importSongsToLike(list, true, "").firstOrNull()
        }

        if (selectDialogTitle != null) importedSuccessPlaylist[selectDialogTitle!!] = true
        isSyncing = false
        selectDialogTitle = null
        selectDialogSong.clear()
    }

    fun importToCurrentNamePlaylist() = viewModelScope.launch(Dispatchers.IO) {
        selectDialogTitle ?: return@launch
        zeneAPI.createNewPlaylists(selectDialogTitle!!, null).onStart {
            isSyncing = true
        }.catch { }.collectLatest {
            importToPlaylist(it.playlistID)
        }
    }

    fun importToPlaylist(playlistID: String?) = viewModelScope.launch(Dispatchers.IO) {
        playlistID ?: return@launch
        isSyncing = true
        selectDialogSong.chunked(50).forEach {
            val list = it.map { s -> "${s.trackName} - ${s.artistName}" }
            zeneAPI.importSongsToLike(list, false, playlistID).firstOrNull()
        }

        if (selectDialogTitle != null) importedSuccessPlaylist[selectDialogTitle!!] = true
        isSyncing = false
        selectDialogTitle = null
        selectDialogSong.clear()
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