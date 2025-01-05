package com.rizwansayyed.zene.data.implementation

import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import kotlinx.coroutines.flow.Flow

interface ZeneAPIInterface {
    suspend fun updateUser(email: String, name: String, photo: String): Flow<UserInfoResponse>
    suspend fun recentHome(): Flow<MusicDataResponse>
}