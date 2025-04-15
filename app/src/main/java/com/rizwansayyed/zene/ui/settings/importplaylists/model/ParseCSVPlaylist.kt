package com.rizwansayyed.zene.ui.settings.importplaylists.model

import com.opencsv.CSVReader
import java.io.File
import java.io.FileReader

fun parseCsvFileAndGroup(file: File): Map<String, List<TrackItemCSV>>? {
    val groupedSongs = mutableMapOf<String, MutableList<TrackItemCSV>>()
    val expectedHeader = arrayOf("Track name", "Artist name", "Album", "Playlist name", "Type")

    try {
        CSVReader(FileReader(file)).use { reader ->
            val allRows = reader.readAll()

            if (allRows.isEmpty()) return null

            val header = allRows[0]

            for (expectedColumn in expectedHeader) {
                if (!header.contains(expectedColumn)) {
                    return null
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

        return groupedSongs
    } catch (e: Exception) {
        return null
    }
}