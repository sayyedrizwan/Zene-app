package com.rizwansayyed.zene.data.implementation

import android.net.Uri
import com.rizwansayyed.zene.data.model.AIDataResponse
import com.rizwansayyed.zene.data.model.ArtistsResponse
import com.rizwansayyed.zene.data.model.ConnectChatMessageResponse
import com.rizwansayyed.zene.data.model.ConnectFeedDataResponse
import com.rizwansayyed.zene.data.model.ConnectUserInfoResponse
import com.rizwansayyed.zene.data.model.ConnectUserResponse
import com.rizwansayyed.zene.data.model.CountResponse
import com.rizwansayyed.zene.data.model.DeleteAccountInfoResponse
import com.rizwansayyed.zene.data.model.EntertainmentDiscoverResponse
import com.rizwansayyed.zene.data.model.EventInfoResponse
import com.rizwansayyed.zene.data.model.LoveBuzzFullInfoResponse
import com.rizwansayyed.zene.data.model.MediaLikedResponse
import com.rizwansayyed.zene.data.model.MediaPathResponse
import com.rizwansayyed.zene.data.model.MediaStatusTypeResponse
import com.rizwansayyed.zene.data.model.MoviesTvShowResponse
import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.data.model.MusicDataTypes
import com.rizwansayyed.zene.data.model.MusicHistoryResponse
import com.rizwansayyed.zene.data.model.NewPlaylistResponse
import com.rizwansayyed.zene.data.model.PlayerLyricsInfoResponse
import com.rizwansayyed.zene.data.model.PlayerRadioResponse
import com.rizwansayyed.zene.data.model.PlayerVideoForSongsResponse
import com.rizwansayyed.zene.data.model.PodcastDataResponse
import com.rizwansayyed.zene.data.model.PodcastEpisodeResponse
import com.rizwansayyed.zene.data.model.PodcastPlaylistResponse
import com.rizwansayyed.zene.data.model.RadioDataResponse
import com.rizwansayyed.zene.data.model.RecommendationNotificationResponse
import com.rizwansayyed.zene.data.model.SavedPlaylistsPodcastsResponse
import com.rizwansayyed.zene.data.model.SearchDataResponse
import com.rizwansayyed.zene.data.model.SearchPlacesDataResponse
import com.rizwansayyed.zene.data.model.SearchTrendingResponse
import com.rizwansayyed.zene.data.model.SponsorAdsResponse
import com.rizwansayyed.zene.data.model.StatusCouponResponse
import com.rizwansayyed.zene.data.model.StatusTypeResponse
import com.rizwansayyed.zene.data.model.StoreDealResponseList
import com.rizwansayyed.zene.data.model.StoreStripeResponse
import com.rizwansayyed.zene.data.model.StreamingTrendingList
import com.rizwansayyed.zene.data.model.UpcomingMoviesList
import com.rizwansayyed.zene.data.model.UserInfoResponse
import com.rizwansayyed.zene.data.model.UserPlaylistResponse
import com.rizwansayyed.zene.data.model.VibesCommentsResponse
import com.rizwansayyed.zene.data.model.VideoDataResponse
import com.rizwansayyed.zene.data.model.WhoDatedWhoData
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.datastore.model.MusicPlayerData
import com.rizwansayyed.zene.ui.view.myplaylist.SortMyPlaylistType
import com.rizwansayyed.zene.ui.view.playlist.PlaylistsType
import com.rizwansayyed.zene.utils.ContactData
import kotlinx.coroutines.flow.Flow
import java.io.File

interface ZeneAPIInterface {
    suspend fun loginUser(tokenID: String, type: String): Flow<UserInfoResponse>
    suspend fun recentHome(): Flow<MusicDataResponse>
    suspend fun recentPodcast(): Flow<PodcastDataResponse>
    suspend fun recentRadio(): Flow<RadioDataResponse>
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
        silentNotification: Boolean,
        expire: Int?
    ): Flow<StatusTypeResponse>

    suspend fun sendConnectMessage(toEmail: String, message: String, gif: String?): Flow<MediaStatusTypeResponse>
    suspend fun sendConnectLocation(toEmail: String): Flow<StatusTypeResponse>
    suspend fun connectFriendsList(): Flow<List<ConnectUserInfoResponse>>
    suspend fun search(q: String): Flow<SearchDataResponse>
    suspend fun searchPlaces(
        q: String?, lon: Double?, lat: Double?
    ): Flow<List<SearchPlacesDataResponse>>

    suspend fun shareConnectVibe(
        d: ConnectFeedDataResponse, file: String?, thumbnail: String?
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
    suspend fun createNewPlaylists(name: String, info: ZeneMusicData?): Flow<NewPlaylistResponse>
    suspend fun playlistSongCheck(songId: String, page: Int): Flow<List<UserPlaylistResponse>>
    suspend fun addItemToPlaylists(
        info: ZeneMusicData?, playlistID: String, state: Boolean
    ): Flow<StatusTypeResponse>

    suspend fun likedStatus(id: String?, type: MusicDataTypes): Flow<MediaLikedResponse>
    suspend fun addRemoveLikeItem(info: ZeneMusicData?, state: Boolean): Flow<StatusTypeResponse>
    suspend fun similarPlaylistsSongs(id: String?): Flow<ZeneMusicDataList>
    suspend fun playerLyrics(p: MusicPlayerData?): Flow<PlayerLyricsInfoResponse>
    suspend fun podcastInfo(id: String?): Flow<PodcastPlaylistResponse>
    suspend fun podcastMediaURL(id: String?): Flow<MediaPathResponse>
    suspend fun podcastCategories(name: String?): Flow<ZeneMusicDataList>
    suspend fun radioMediaURL(id: String?): Flow<MediaPathResponse>
    suspend fun aiMusicMediaURL(id: String?): Flow<MediaPathResponse>
    suspend fun similarAISongs(tags: String?): Flow<ZeneMusicDataList>
    suspend fun lyricsAIMusic(id: String?): Flow<PlayerLyricsInfoResponse>
    suspend fun similarPodcasts(id: String?): Flow<ZeneMusicDataList>
    suspend fun playlistsInfo(id: String?): Flow<PodcastPlaylistResponse>
    suspend fun searchImages(q: ZeneMusicData): Flow<List<String>>
    suspend fun playerPodcastInfo(id: String): Flow<PodcastEpisodeResponse>
    suspend fun playerRadioInfo(id: String): Flow<PlayerRadioResponse>
    suspend fun radioByCountry(name: String): Flow<ZeneMusicDataList>
    suspend fun similarSongs(id: String): Flow<SearchDataResponse>
    suspend fun playerVideoForSongs(p: ZeneMusicData?): Flow<PlayerVideoForSongsResponse>
    suspend fun similarRadio(tags: String?): Flow<ZeneMusicDataList>
    suspend fun artistsInfo(artistsID: String?): Flow<ArtistsResponse>
    suspend fun updateUser(): Flow<UserInfoResponse>
    suspend fun followArtists(name: String?, doAdd: Boolean?): Flow<Boolean>
    suspend fun addHistory(data: ZeneMusicData): Flow<StatusTypeResponse>
    suspend fun getHistory(page: Int): Flow<List<MusicHistoryResponse>>
    suspend fun savePlaylists(
        data: PodcastPlaylistResponse, status: Boolean, type: PlaylistsType
    ): Flow<StatusTypeResponse>

    suspend fun getSavePlaylists(page: Int): Flow<SavedPlaylistsPodcastsResponse>
    suspend fun myPlaylists(page: Int): Flow<SavedPlaylistsPodcastsResponse>
    suspend fun likeSongsCount(): Flow<CountResponse>
    suspend fun myPlaylistsSongs(playlistId: String, page: Int, customOrder: SortMyPlaylistType?): Flow<ZeneMusicDataList>
    suspend fun removeMyPlaylistsSongs(
        playlistId: String, songID: String, type: String?
    ): Flow<StatusTypeResponse>

    suspend fun myPlaylistInfo(id: String): Flow<ZeneMusicData>
    suspend fun deleteMyPlaylists(id: String): Flow<StatusTypeResponse>
    suspend fun nameUserPlaylist(id: String, name: String): Flow<StatusTypeResponse>
    suspend fun searchImages(q: String): Flow<List<String>>
    suspend fun updateImageUserPlaylist(id: String?, file: Uri?): Flow<StatusTypeResponse>
    suspend fun checkUsername(username: String): Flow<StatusTypeResponse>
    suspend fun updateUsername(username: String): Flow<StatusTypeResponse>
    suspend fun updateName(name: String): Flow<StatusTypeResponse>
    suspend fun checkNumberVerified(): Flow<StatusTypeResponse>
    suspend fun updatePhoto(file: Uri?): Flow<StatusTypeResponse>
    suspend fun moviesTvShowsInfo(id: String?): Flow<MoviesTvShowResponse>
    suspend fun seasonMoviesTvShowsInfo(id: String?): Flow<ZeneMusicDataList>
    suspend fun similarArtistsAlbumOfSong(id: String, name: String?, artists: String?): Flow<ZeneMusicData>
    suspend fun isPlaylistAdded(id: String, type: String): Flow<StatusTypeResponse>
    suspend fun markConnectMessageToRead(toEmail: String): Flow<StatusTypeResponse>
    suspend fun getChatConnectRecentMessage(toEmail: String, lastId: String?): Flow<List<ConnectChatMessageResponse>>
    suspend fun sendConnectMediaMessage(
        userEmail: String?, file: String?, thumbnail: String?
    ): Flow<MediaStatusTypeResponse>

    suspend fun sendConnectJamMessage(
        toEmail: String, data: ZeneMusicData?
    ): Flow<MediaStatusTypeResponse>

    suspend fun sendConnectFileMessage(toEmail: String?, file: File?): Flow<MediaStatusTypeResponse>
    suspend fun deleteConnectMessage(toEmail: String?, id: String?): Flow<MediaStatusTypeResponse>
    suspend fun sendPartyCall(toEmail: String, code: String): Flow<StatusTypeResponse>
    suspend fun declinePartyCall(toEmail: String): Flow<StatusTypeResponse>
    suspend fun connectCreatePlaylists(otherEmail: String, name: String): Flow<StatusTypeResponse>
    suspend fun getConnectPlaylists(
        otherEmail: String?, page: Int
    ): Flow<SavedPlaylistsPodcastsResponse>

    suspend fun songInfo(id: String): Flow<ZeneMusicData>
    suspend fun aiMusicInfo(id: String?): Flow<ZeneMusicData>
    suspend fun myAllPlaylists(): Flow<SavedPlaylistsPodcastsResponse>
    suspend fun importSongsToLike(name: List<String>, isLike: Boolean, playlistId: String): Flow<StatusTypeResponse>
    suspend fun updateSubscription(purchaseToken: String, subscriptionId: String?): Flow<StatusTypeResponse>
    suspend fun isUserPremium(): Flow<StatusTypeResponse>
    suspend fun updateCoupon(code: String?): Flow<StatusCouponResponse>
    suspend fun notificationRecommendation(): Flow<RecommendationNotificationResponse>
    suspend fun sponsorAds(): Flow<SponsorAdsResponse>
    suspend fun feedFollowedArtists(): Flow<ZeneMusicDataList>
    suspend fun feedUpdatesArtists(): Flow<ZeneMusicDataList>
    suspend fun deleteAccount(): Flow<StatusTypeResponse>
    suspend fun deleteAccountInfo(): Flow<DeleteAccountInfoResponse>
    suspend fun cancelDeleteAccount(): Flow<StatusTypeResponse>
    suspend fun updateEmailSubscription(value: Boolean): Flow<StatusTypeResponse>

    suspend fun myPlaylistsSongsReorder(
        v: ZeneMusicData,
        pId: String,
        position: Int
    ): Flow<StatusTypeResponse>


    suspend fun entDiscoverNews(): Flow<EntertainmentDiscoverResponse>
    suspend fun entBuzzNews(): Flow<ZeneMusicDataList>

    suspend fun entDiscoverLifeStyle(): Flow<ZeneMusicDataList>

    suspend fun entDating(): Flow<List<WhoDatedWhoData>?>
    suspend fun entAllTrailers(): Flow<ZeneMusicDataList>
    suspend fun entStreamingTrending(): Flow<StreamingTrendingList>
    suspend fun entBoxOfficeMovie(): Flow<ZeneMusicDataList>
    suspend fun entUpcomingMovie(): Flow<UpcomingMoviesList>
    suspend fun entLifestyleEvents(): Flow<ZeneMusicDataList>

    suspend fun storeTopDeals(): Flow<StoreDealResponseList>
    suspend fun storeStripeLink(url: String): Flow<StoreStripeResponse>
    suspend fun eventFullInfo(id: String): Flow<EventInfoResponse>
    suspend fun loveBuzzFullInfo(id: String): Flow<LoveBuzzFullInfoResponse>
}