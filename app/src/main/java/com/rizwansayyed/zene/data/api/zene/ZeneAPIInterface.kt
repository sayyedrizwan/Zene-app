package com.rizwansayyed.zene.data.api.zene

import com.rizwansayyed.zene.data.api.model.IpJsonResponse
import com.rizwansayyed.zene.data.api.model.StatusResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsData
import com.rizwansayyed.zene.data.api.model.ZeneArtistsDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsInfoResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsPostsResponse
import com.rizwansayyed.zene.data.api.model.ZeneBooleanResponse
import com.rizwansayyed.zene.data.api.model.ZeneLyricsData
import com.rizwansayyed.zene.data.api.model.ZeneMoodPlaylistData
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicHistoryResponse
import com.rizwansayyed.zene.data.api.model.ZenePlaylistAlbumsData
import com.rizwansayyed.zene.data.api.model.ZeneSavedPlaylistsResponse
import com.rizwansayyed.zene.data.api.model.ZeneSearchData
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.data.api.model.ZeneVideosMusicData
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ZeneAPIInterface {

    suspend fun updateUser(): Flow<StatusResponse>

    suspend fun getUser(email: String): Flow<ZeneUsersResponse>

    suspend fun topMostListeningSong(): Flow<ZeneMusicDataResponse>

    suspend fun recommendedPlaylists(): Flow<ZeneMusicDataResponse>

    suspend fun recommendedAlbums(): Flow<ZeneMusicDataResponse>

    suspend fun recommendedVideo(): Flow<ZeneMusicDataResponse>

    suspend fun suggestTopSongs(): Flow<ZeneMusicDataResponse>

    suspend fun moodLists(): Flow<ZeneMusicDataResponse>

    suspend fun latestReleases(id: String): Flow<ZeneMusicDataResponse>

    suspend fun topMostListeningArtists(): Flow<ZeneMusicDataResponse>

    suspend fun favArtistsData(): Flow<ZeneArtistsData>

    suspend fun suggestedSongs(): Flow<ZeneMusicDataResponse>

    suspend fun searchData(s: String): Flow<ZeneSearchData>

    suspend fun searchSuggestions(s: String): Flow<List<String>>

    suspend fun suggestedSongs(id: String): Flow<ZeneMusicDataResponse>

    suspend fun lyrics(id: String, name: String, artists: String): Flow<ZeneLyricsData>

    suspend fun playerVideoData(name: String, artists: String): Flow<ZeneVideosMusicData>

    suspend fun getMusicHistory(page: Int): Flow<ZeneMusicHistoryResponse>

    suspend fun addMusicHistory(songID: String): Flow<ZeneBooleanResponse>

    suspend fun moodLists(id: String): Flow<ZeneMoodPlaylistData>

    suspend fun merchandise(name: String, artists: String): Flow<ZeneMusicDataResponse>

    suspend fun artistsInfo(name: String): Flow<ZeneArtistsInfoResponse>

    suspend fun artistsData(name: String): Flow<ZeneArtistsDataResponse>

    suspend fun updateArtists(list: Array<String>): Flow<ZeneBooleanResponse>

    suspend fun ip(): Flow<IpJsonResponse>

    suspend fun searchImg(q: String): Flow<List<String>>

    suspend fun createNewPlaylists(
        name: String, file: File?, id: String?
    ): Flow<ZeneBooleanResponse>

    suspend fun savedPlaylists(page: Int): Flow<ZeneSavedPlaylistsResponse>

    suspend fun playlistAlbums(id: String): Flow<ZenePlaylistAlbumsData>

    suspend fun deletePlaylists(id: String): Flow<ZeneBooleanResponse>

    suspend fun checkIfSongPresentInPlaylists(
        songId: String, page: Int
    ): Flow<ZeneMusicDataResponse>

    suspend fun addRemoveSongFromPlaylists(
        songId: String, pID: String, doAdd: Boolean
    ): Flow<ZeneBooleanResponse>

    suspend fun userPlaylistData(playlistID: String): Flow<ZeneMusicDataItems>

    suspend fun userPlaylistSongs(playlistID: String, page: Int): Flow<ZeneMusicDataResponse>

    suspend fun artistsPosts(): Flow<ZeneArtistsPostsResponse>
}