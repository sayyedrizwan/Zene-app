package com.rizwansayyed.zene.data.onlinesongs.youtube.implementation

import com.rizwansayyed.zene.domain.ArtistsFanData
import com.rizwansayyed.zene.domain.IpJsonResponse
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.PlaylistItemsData
import com.rizwansayyed.zene.domain.SearchData
import com.rizwansayyed.zene.domain.SongsSuggestionsData
import kotlinx.coroutines.flow.Flow

interface YoutubeAPIImplInterface {

    suspend fun newReleaseMusic(): Flow<List<MusicData>>
    suspend fun musicInfoSearch(q: String, ip: IpJsonResponse?, key: String): MusicData?
    suspend fun artistsInfo(artists: List<MusicData>): Flow<List<MusicData>>
    suspend fun searchSuggestions(s: String): Flow<MutableList<String>>
    suspend fun allSongsSearch(q: String): Flow<MutableList<MusicData>>
    suspend fun songFromArtistsTopFive(artists: List<String>): Flow<List<MusicData>>
    suspend fun topThreeSongsSuggestionOnHistory(pIds: List<String>): Flow<List<MusicData>>
    suspend fun artistsAlbumsTopFive(names: List<String>): Flow<List<MusicData>>
    suspend fun searchArtistsInfo(s: String): Flow<List<MusicData>>
    suspend fun songsSuggestionsForUsers(sId: List<String>): Flow<SongsSuggestionsData>
    suspend fun artistsFansItemSearch(artists: List<String>): Flow<List<ArtistsFanData>>
    suspend fun searchTextsSuggestions(q: String): Flow<List<MusicData>>
    suspend fun searchData(q: String): Flow<SearchData>
    suspend fun allYoutubeVideoThisYearSearch(q: String): Flow<MutableList<MusicData>>
    suspend fun searchArtistsPlaylistsForRadio(q: String): Flow<MusicData>
    suspend fun albumsSearch(id: String): Flow<PlaylistItemsData>
}