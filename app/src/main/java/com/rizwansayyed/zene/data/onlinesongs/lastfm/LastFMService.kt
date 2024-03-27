package com.rizwansayyed.zene.data.onlinesongs.lastfm


import com.rizwansayyed.zene.data.utils.LastFM.LFM_SEARCH_ARTISTS
import com.rizwansayyed.zene.data.utils.LastFM.LFM_TOP_LISTEN_SONGS
import com.rizwansayyed.zene.domain.OnlineRadioResponse
import com.rizwansayyed.zene.domain.lastfm.ArtistsImagesResponse
import com.rizwansayyed.zene.domain.lastfm.ArtistsSearchResponse
import com.rizwansayyed.zene.domain.lastfm.TopRecentPlaySongsResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface LastFMService {

    @GET(LFM_TOP_LISTEN_SONGS)
    suspend fun topRecentPlayingSongs(
        @Query("type") type: String = "artist",
        @Query("tracks") tracks: Int = 1,
        @Query("nr") nr: Int = 10,
        @Query("format") format: String = "json",
    ): TopRecentPlaySongsResponse


    @GET(LFM_SEARCH_ARTISTS)
    suspend fun searchArtists(@Query("q") q: String): ArtistsSearchResponse


    @GET
    suspend fun artistsImages(@Url q: String): ArtistsImagesResponse

}
