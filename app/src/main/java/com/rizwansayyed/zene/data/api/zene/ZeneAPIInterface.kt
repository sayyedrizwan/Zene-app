package com.rizwansayyed.zene.data.api.zene

import com.rizwansayyed.zene.data.api.model.StatusResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsData
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import kotlinx.coroutines.flow.Flow

interface ZeneAPIInterface {

    suspend fun updateUser(): Flow<StatusResponse>

    suspend fun getUser(email: String): Flow<ZeneUsersResponse>

    suspend fun topMostListeningSong(): Flow<ZeneMusicDataResponse>

    suspend fun recommendedPlaylists(list: Array<String>): Flow<ZeneMusicDataResponse>

    suspend fun recommendedAlbums(list: Array<String>): Flow<ZeneMusicDataResponse>

    suspend fun recommendedVideo(list: Array<String>): Flow<ZeneMusicDataResponse>

    suspend fun suggestTopSongs(list: Array<String>): Flow<ZeneMusicDataResponse>

    suspend fun moodLists(): Flow<ZeneMusicDataResponse>

    suspend fun latestReleases(id: String): Flow<ZeneMusicDataResponse>

    suspend fun topMostListeningArtists(): Flow<ZeneMusicDataResponse>

    suspend fun favArtistsData(list: Array<String>): Flow<ZeneArtistsData>
}