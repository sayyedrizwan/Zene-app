package com.rizwansayyed.zene.data.api.zene

import com.rizwansayyed.zene.data.api.model.StatusResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsData
import com.rizwansayyed.zene.data.api.model.ZeneBooleanResponse
import com.rizwansayyed.zene.data.api.model.ZeneLyricsData
import com.rizwansayyed.zene.data.api.model.ZeneMoodPlaylistData
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicHistoryResponse
import com.rizwansayyed.zene.data.api.model.ZenePlaylistAlbumsData
import com.rizwansayyed.zene.data.api.model.ZeneSearchData
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.data.api.model.ZeneVideosMusicData
import kotlinx.coroutines.flow.Flow

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

    suspend fun playlistAlbums(id: String): Flow<ZenePlaylistAlbumsData>

    suspend fun moodLists(id: String): Flow<ZeneMoodPlaylistData>
}