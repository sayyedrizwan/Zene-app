package com.rizwansayyed.zene.data.api.zene

import com.rizwansayyed.zene.data.api.model.StatusResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import kotlinx.coroutines.flow.Flow

interface ZeneAPIInterface {

    suspend fun updateUser(): Flow<StatusResponse>

    suspend fun getUser(email: String): Flow<ZeneUsersResponse>

    suspend fun recommendedPlaylists(list: Array<String>): Flow<ZeneMusicDataResponse>
    suspend fun recommendedAlbums(list: Array<String>): Flow<ZeneMusicDataResponse>
}