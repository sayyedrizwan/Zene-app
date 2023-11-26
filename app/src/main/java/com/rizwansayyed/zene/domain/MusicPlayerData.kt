package com.rizwansayyed.zene.domain

data class MusicPlayerData(
    var show: Boolean = false,
    var v: MusicPlayerList? = null,
    val currentTs: Long = 0,
    val duration: Long = 0,
    val backShorts: List<String> = emptyList(),
    var songID: String = "",
    var videoID: String = "",
    var shortsID: String = "",
    val songsLyrics: String = "",
    var songsLists: List<MusicData?> = emptyList(),
    var playType: MusicType?,
    var temp: Int?,
)

data class MusicPlayerList(
    val songName: String?,
    val artists: String?,
    val songID: String?,
    val thumbnail: String?,
)