package com.rizwansayyed.zene.utils

import android.widget.Toast
import com.rizwansayyed.zene.BaseApplication.Companion.context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object Algorithims {

    object URL {
        const val ALBUMS_WITH_HEADERS = "albumsWithHeaders"
    }

    fun extractSongTitles(songsName: String): String {
        try {
            if (!songsName.contains("|")) {
                if (songsName.contains("-")) {
                    return songsName.substringAfter("-").replace("(Official Video)", "")
                        .replace("(official video)", "").replace("(Official Song)", "")
                        .replace("(official song)", "")
                }
                return songsName
            }
            val getName =
                songsName.substringBefore("|").replace("song", "").replace("lyrical", "")
                    .replace("Song", "").replace("Lyrical", "").trim()
            return if (getName.contains("-")) {
                getName.substringBefore("-")
            } else {
                getName
            }

        } catch (e: Exception) {
            return songsName
        }
    }

    fun extractSongSubTitles(songsName: String): String {
        try {
            if (!songsName.contains("|")) {
                if (songsName.contains("-")) {
                    return songsName.substringBefore("-").replace("(Official Video)", "")
                        .replace("(official video)", "").replace("(Official Song)", "")
                        .replace("(official song)", "")
                }
                return "Official Song"
            }
            val getName = songsName.substringBefore("|").trim()
            return if (getName.contains("-")) {
                getName.substringAfter("-")
            } else {
                return songsName.substringAfter("|").substringBefore("|").trim()
            }

        } catch (e: Exception) {
            return songsName
        }
    }

    fun randomIds(): String {
        return (111..9999999999).random().toString()
    }

}