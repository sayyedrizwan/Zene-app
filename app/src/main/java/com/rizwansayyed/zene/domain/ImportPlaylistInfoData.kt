package com.rizwansayyed.zene.domain

import android.util.Log
import com.rizwansayyed.zene.domain.applemusic.AppleMusicPlaylistItemResponse
import com.rizwansayyed.zene.domain.spotify.playlist.SpotifyUserPlaylistResponse
import com.rizwansayyed.zene.domain.spotify.playlist.SpotifyUserPlaylistTrackResponse
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.PlaylistImportersType

const val likedSpotify = "liked_spotify"

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


fun toSpotifyLiked(): ImportPlaylistInfoData {
    return ImportPlaylistInfoData(
        "https://misc.scdn.co/liked-songs/liked-songs-300.png",
        "Liked Songs",
        "Spotify",
        likedSpotify,
        PlaylistImportersType.SPOTIFY
    )
}


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

fun SpotifyUserPlaylistTrackResponse.Item.toTrack(): ImportPlaylistTrackInfoData? {
    return try {
        ImportPlaylistTrackInfoData(
            track?.album?.images?.maxBy { i -> i?.height ?: 2 }?.url,
            track?.album?.artists?.map { it?.name }?.joinToString(", "), track?.name
        )
    } catch (e: Exception) {
        null
    }
}

fun SpotifyUserPlaylistResponse.toPlaylistInfo(p: PlaylistImportersType): List<ImportPlaylistInfoData>? {
    return items?.map {
        try {
            ImportPlaylistInfoData(
                it?.images?.maxBy { i -> i?.height ?: 2 }?.url,
                it?.name, it?.owner?.display_name, it?.id, p
            )
        } catch (e: Exception) {
            ImportPlaylistInfoData("", "", "", null, p)
        }
    }
}