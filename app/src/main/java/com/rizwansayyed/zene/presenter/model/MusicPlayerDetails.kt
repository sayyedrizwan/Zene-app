package com.rizwansayyed.zene.presenter.model

data class MusicPlayerDetails(
    val thumbnail: String?,
    val songName: String?,
    val artists: String?,
    var pId: String?,
    var currentDuration: Long?,
    var duration: Long?,
    var state: MusicPlayerState?,
    val time: Long?,
)

enum class MusicPlayerState(val s: Int) {
    EMPTY(0), LOADING(1), ERROR(2), PAUSE(3), PLAYING(4)
}