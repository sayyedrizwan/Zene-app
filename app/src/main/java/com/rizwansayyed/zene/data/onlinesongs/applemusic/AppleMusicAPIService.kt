package com.rizwansayyed.zene.data.onlinesongs.applemusic


import com.rizwansayyed.zene.data.utils.AppleMusicAPI.APPLE_MUSIC_PLAYLIST_TRACKS_URL
import com.rizwansayyed.zene.data.utils.AppleMusicAPI.APPLE_MUSIC_PLAYLIST_URL
import com.rizwansayyed.zene.domain.applemusic.AppleMusicPlaylistItemResponse
import com.rizwansayyed.zene.domain.applemusic.AppleMusicPlaylistTracksResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface AppleMusicAPIService {

    @GET(APPLE_MUSIC_PLAYLIST_URL)
    suspend fun appleMusicPlaylists(
        @Header("authorization") auth: String,
        @Header("cookie") cookie: String,
        @Query("offset") offset: Int = 0
    ): AppleMusicPlaylistItemResponse


    @GET(APPLE_MUSIC_PLAYLIST_TRACKS_URL)
    suspend fun appleMusicPlaylistsTracks(
        @Header("authorization") auth: String,
        @Header("cookie") cookie: String,
        @Path("playlist_id") playlistId: String,
        @Query("include") include: String = "catalog,artists,tracks"
    ): AppleMusicPlaylistTracksResponse


}
