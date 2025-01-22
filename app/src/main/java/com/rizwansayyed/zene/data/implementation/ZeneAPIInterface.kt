package com.rizwansayyed.zene.data.implementation

import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.EntertainmentDataResponse
import com.rizwansayyed.zene.data.model.MoviesDataResponse
import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.data.model.PodcastDataResponse
import com.rizwansayyed.zene.data.model.RadioDataResponse
import com.rizwansayyed.zene.data.model.StatusTypeResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.data.model.VideoDataResponse
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.utils.ContactData
import kotlinx.coroutines.flow.Flow

interface ZeneAPIInterface {
    suspend fun updateUser(email: String, name: String, photo: String): Flow<UserInfoResponse>
    suspend fun recentHome(): Flow<MusicDataResponse>
    suspend fun recentPodcast(): Flow<PodcastDataResponse>
    suspend fun recentRadio(): Flow<RadioDataResponse>
    suspend fun entertainmentNews(): Flow<EntertainmentDataResponse>
    suspend fun entertainmentMovies(): Flow<MoviesDataResponse>
    suspend fun homeVideos(): Flow<VideoDataResponse>
    suspend fun connectNearMusic(): Flow<ZeneMusicDataList>
    suspend fun updateTrueCallerNumber(codeVerifier: String, code: String): Flow<StatusTypeResponse>
    suspend fun sendVerifyPhoneNumber(number: String): Flow<StatusTypeResponse>
    suspend fun verifyPhoneNumber(code: String): Flow<StatusTypeResponse>
    suspend fun connectUsersSearch(contacts: List<ContactData>): Flow<List<ConnectUserResponse>>
    suspend fun searchConnect(query: String): Flow<List<ConnectUserResponse>>
    suspend fun connectUserInfo(toEmail: String): Flow<ConnectUserInfoResponse>
}