package com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation

import com.rizwansayyed.zene.domain.ArtistsEvents
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataWithArtists
import com.rizwansayyed.zene.domain.lastfm.ArtistsSearchResponse
import com.rizwansayyed.zene.domain.lastfm.LastFMArtist
import kotlinx.coroutines.flow.Flow

interface LastFMImplInterface {

    suspend fun artistsImages(name: LastFMArtist?, limit: Int = 40): Flow<MutableList<String>>
    suspend fun topRecentPlayingSongs(): Flow<MutableList<MusicDataWithArtists>>
    suspend fun artistsUsername(name: String): Flow<LastFMArtist?>
    suspend fun artistsDescription(user: LastFMArtist): Flow<String>
    suspend fun artistsEvent(user: LastFMArtist): Flow<ArrayList<ArtistsEvents>>
    suspend fun artistsTopSongs(user: LastFMArtist): Flow<ArrayList<MusicData>>
}