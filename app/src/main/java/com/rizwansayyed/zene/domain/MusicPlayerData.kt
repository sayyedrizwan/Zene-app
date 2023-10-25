package com.rizwansayyed.zene.domain

data class MusicPlayerData(
    val show: Boolean = false,
    val v: MusicPlayerList? = null,
    val currentTs: Long = 0,
    val duration: Long = 0,
    val backShorts: List<String> = emptyList(),
    val videoID: String = "",
    val songsLyrics: String = "",
    val songsLists: List<MusicPlayerList?> = emptyList()
)

data class MusicPlayerList(
    val songName: String = "",
    val artists: String = "",
    val songID: String = "",
    val playURL: String = "",
)