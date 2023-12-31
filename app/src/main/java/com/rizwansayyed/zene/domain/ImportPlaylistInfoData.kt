package com.rizwansayyed.zene.domain

import com.rizwansayyed.zene.domain.applemusic.AppleMusicPlaylistItemResponse
import com.rizwansayyed.zene.domain.spotify.playlist.SpotifyUserPlaylistResponse
import com.rizwansayyed.zene.domain.spotify.playlist.SpotifyUserPlaylistTrackResponse
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.PlaylistImportersType

data class ImportPlaylistInfoData(
    val thumbnail: String?,
    val name: String?,
    val desc: String?,
    val id: String?,
    val type: PlaylistImportersType
)


data class ImportPlaylistTrackInfoData(
    val thumbnail: String?,
    val artistsName: String?,
    val songName: String?,
)


fun AppleMusicPlaylistItemResponse.toPlaylists(): List<ImportPlaylistInfoData> {
    return data?.map {
        ImportPlaylistInfoData(
            it?.attributes?.artwork?.url,
            it?.attributes?.name,
            it?.attributes?.description?.standard,
            it?.id, PlaylistImportersType.APPLE_MUSIC
        )
    } ?: emptyList()
}

fun SpotifyUserPlaylistTrackResponse.Item.toTrack(): ImportPlaylistTrackInfoData {
    return ImportPlaylistTrackInfoData(
        track?.album?.images?.maxBy { i -> i?.height ?: 2 }?.url,
        track?.album?.artists?.map { it?.name }?.joinToString(", "), track?.name
    )
}

fun SpotifyUserPlaylistResponse.toPlaylistInfo(p: PlaylistImportersType): List<ImportPlaylistInfoData>? {
    return items?.map {
        ImportPlaylistInfoData(
            it?.images?.maxBy { i -> i?.height ?: 2 }?.url,
            it?.name, it?.owner?.display_name, it?.id, p
        )
    }
}