package com.rizwansayyed.zene.domain

data class MusicPlayerData(
    val show: Boolean = false,
    var v: MusicPlayerList? = null,
    val currentTs: Long = 0,
    val duration: Long = 0,
    val backShorts: List<String> = emptyList(),
    val songID: String = "",
    val videoID: String = "",
    val songsLyrics: String = "",
    var songsLists: List<MusicData?> = emptyList()
)

data class MusicPlayerList(
    val songName: String?,
    val artists: String?,
    val songID: String?,
    val thumbnail: String?,
)