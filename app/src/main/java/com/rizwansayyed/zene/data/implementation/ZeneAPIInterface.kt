package com.rizwansayyed.zene.data.implementation

import com.rizwansayyed.zene.data.model.AIDataResponse
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.EntertainmentDataResponse
import com.rizwansayyed.zene.data.model.MoviesDataResponse
import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.data.model.PodcastDataResponse
import com.rizwansayyed.zene.data.model.RadioDataResponse
import com.rizwansayyed.zene.data.model.SearchDataResponse
import com.rizwansayyed.zene.data.model.SearchPlacesDataResponse
import com.rizwansayyed.zene.data.model.SearchTrendingResponse
import com.rizwansayyed.zene.data.model.StatusTypeResponse
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.data.model.VibesCommentsResponse
import com.rizwansayyed.zene.data.model.VideoDataResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
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
    suspend fun updateTrueCallerNumber(codeVerifier: String, code: String): Flow<StatusTypeResponse>
    suspend fun sendVerifyPhoneNumber(number: String): Flow<StatusTypeResponse>
    suspend fun verifyPhoneNumber(code: String): Flow<StatusTypeResponse>
    suspend fun connectUsersSearch(contacts: List<ContactData>): Flow<List<ConnectUserResponse>>
    suspend fun searchConnect(query: String): Flow<List<ConnectUserResponse>>
    suspend fun connectUserInfo(toEmail: String): Flow<ConnectUserInfoResponse>
    suspend fun connectSendRequest(toEmail: String, remove: Boolean): Flow<StatusTypeResponse>
    suspend fun connectAcceptRequest(toEmail: String): Flow<StatusTypeResponse>
    suspend fun updateConnectSettings(
        toEmail: String,
        lastListenSongs: Boolean,
        locationSharing: Boolean,
        silentNotification: Boolean
    ): Flow<StatusTypeResponse>

    suspend fun sendConnectMessage(toEmail: String, message: String): Flow<StatusTypeResponse>
    suspend fun sendConnectLocation(toEmail: String): Flow<StatusTypeResponse>
    suspend fun connectFriendsList(): Flow<List<ConnectUserInfoResponse>>
    suspend fun search(q: String): Flow<SearchDataResponse>
    suspend fun searchPlaces(
        q: String?,
        lon: Double?,
        lat: Double?
    ): Flow<List<SearchPlacesDataResponse>>

    suspend fun shareConnectVibe(
        d: ConnectFeedDataResponse,
        file: String?,
        thumbnail: String?
    ): Flow<StatusTypeResponse>

    suspend fun connectFriendsRequestList(): Flow<List<ConnectUserResponse>>
    suspend fun connectFriendsVibesList(page: Int): Flow<List<ConnectFeedDataResponse>>
    suspend fun trendingGIF(): Flow<List<String>>
    suspend fun searchGif(q: String): Flow<List<String>>
    suspend fun postCommentOnVibes(gif: String, id: Int?): Flow<StatusTypeResponse>
    suspend fun getCommentOfVibes(id: Int?, page: Int): Flow<List<VibesCommentsResponse>>
    suspend fun updateConnectStatus(status: String): Flow<StatusTypeResponse>
    suspend fun trendingAIMusic(): Flow<AIDataResponse>
    suspend fun trendingData(): Flow<SearchTrendingResponse>
    suspend fun searchKeywords(q: String): Flow<List<String>>
    suspend fun searchASong(q: String): Flow<ZeneMusicData>
    suspend fun similarVideos(id: String): Flow<ZeneMusicDataList>
}