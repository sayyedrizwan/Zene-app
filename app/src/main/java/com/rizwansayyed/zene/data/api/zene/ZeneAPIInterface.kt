package com.rizwansayyed.zene.data.api.zene

import com.rizwansayyed.zene.data.api.model.IpJsonResponse
import com.rizwansayyed.zene.data.api.model.SongLikedResponse
import com.rizwansayyed.zene.data.api.model.StatusResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsData
import com.rizwansayyed.zene.data.api.model.ZeneArtistsDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsInfoResponse
import com.rizwansayyed.zene.data.api.model.ZeneArtistsPostsResponse
import com.rizwansayyed.zene.data.api.model.ZeneBooleanResponse
import com.rizwansayyed.zene.data.api.model.ZeneCacheTopSongsArtistsPostsResponse
import com.rizwansayyed.zene.data.api.model.ZeneLyricsData
import com.rizwansayyed.zene.data.api.model.ZeneMoodPlaylistData
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataItems
import com.rizwansayyed.zene.data.api.model.ZeneMusicDataResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicHistoryResponse
import com.rizwansayyed.zene.data.api.model.ZeneMusicImportPlaylistsDataResponse
import com.rizwansayyed.zene.data.api.model.ZenePlaylistAlbumsData
import com.rizwansayyed.zene.data.api.model.ZeneSavedPlaylistsResponse
import com.rizwansayyed.zene.data.api.model.ZeneSearchData
import com.rizwansayyed.zene.data.api.model.ZeneUpdateAvailabilityResponse
import com.rizwansayyed.zene.data.api.model.ZeneUsersPremiumResponse
import com.rizwansayyed.zene.data.api.model.ZeneUsersResponse
import com.rizwansayyed.zene.data.api.model.ZeneVideosMusicData
import com.rizwansayyed.zene.data.roomdb.zeneconnect.model.ZeneConnectContactsModel
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

    suspend fun addMusicHistory(songID: String, artists: String?): Flow<ZeneBooleanResponse>

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

    suspend fun importSpotifyPlaylists(
        token: String, path: String?
    ): Flow<ZeneMusicImportPlaylistsDataResponse>

    suspend fun songInfo(id: String): Flow<ZeneMusicDataItems>

    suspend fun sponsorsAds()

    suspend fun updateAvailability(): Flow<ZeneUpdateAvailabilityResponse>
    suspend fun isSongLiked(songID: String): Flow<SongLikedResponse>
    suspend fun relatedVideos(id: String): Flow<ZeneMusicDataResponse>
    suspend fun topCacheSongsAndArtists(): Flow<ZeneCacheTopSongsArtistsPostsResponse>
    suspend fun updateUserSubscription(
        orderId: String, purchaseToken: String
    ): Flow<ZeneBooleanResponse>

    suspend fun isUserPremium(): Flow<ZeneUsersPremiumResponse>
    suspend fun similarSongsToPlay(id: String): Flow<ZeneMusicDataResponse>
    suspend fun isCouponAvailable(code: String): Flow<ZeneBooleanResponse>
    suspend fun numberVerification(number: String, country: String): Flow<ZeneBooleanResponse>
    suspend fun numberVerificationUpdate(
        number: String, country: String, otp: String
    ): Flow<ZeneBooleanResponse>

    suspend fun numberUserInfo(numbers: Array<String>): Flow<List<ZeneUsersResponse>>
    suspend fun sendConnectVibes(
        connect: ZeneConnectContactsModel, song: ZeneMusicDataItems?
    ): Flow<ZeneBooleanResponse>
}