package com.rizwansayyed.zene.viewmodel

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.data.ResponseResult
import com.rizwansayyed.zene.data.cache.CacheHelper
import com.rizwansayyed.zene.data.implementation.ZeneAPIInterface
import com.rizwansayyed.zene.data.model.AIDataResponse
import com.rizwansayyed.zene.data.model.ArtistsResponse
import com.rizwansayyed.zene.data.model.DeleteAccountInfoResponse
import com.rizwansayyed.zene.data.model.MoviesTvShowResponse
import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.data.model.PodcastDataResponse
import com.rizwansayyed.zene.data.model.PodcastPlaylistResponse
import com.rizwansayyed.zene.data.model.RadioDataResponse
import com.rizwansayyed.zene.data.model.SearchDataResponse
import com.rizwansayyed.zene.data.model.SearchPlacesDataResponse
import com.rizwansayyed.zene.data.model.SearchTrendingResponse
import com.rizwansayyed.zene.data.model.StoreDealResponseList
import com.rizwansayyed.zene.data.model.VideoDataResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.datastore.DataStorageManager.isPostedReviewDB
import com.rizwansayyed.zene.datastore.DataStorageManager.isPostedReviewTimestampDB
import com.rizwansayyed.zene.datastore.DataStorageManager.sponsorAdsDB
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.phoneverification.view.TrueCallerUtils
import com.rizwansayyed.zene.ui.view.playlist.PlaylistsType
import com.rizwansayyed.zene.utils.SnackBarManager
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_MUSIC_LIST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_MUSIC_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_PODCAST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_RADIO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_VIDEOS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_TRENDING_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_STORE_TOP_DEALS_API
import com.rizwansayyed.zene.utils.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    val trueCallerUtils: TrueCallerUtils, private val zeneAPI: ZeneAPIInterface
) : ViewModel() {

    private val cacheHelper = CacheHelper()
    var homeRecent by mutableStateOf<ResponseResult<MusicDataResponse>>(ResponseResult.Empty)
    var searchTrending by mutableStateOf<ResponseResult<SearchTrendingResponse>>(ResponseResult.Empty)
    var searchImages by mutableStateOf<ResponseResult<List<String>>>(ResponseResult.Empty)
    var aiMusicList by mutableStateOf<ResponseResult<AIDataResponse>>(ResponseResult.Empty)
    var homePodcast by mutableStateOf<ResponseResult<PodcastDataResponse>>(ResponseResult.Empty)
    var homeVideos by mutableStateOf<ResponseResult<VideoDataResponse>>(ResponseResult.Empty)
    var homeRadio by mutableStateOf<ResponseResult<RadioDataResponse>>(ResponseResult.Empty)
    var searchKeywords by mutableStateOf<ResponseResult<List<String>>>(ResponseResult.Empty)
    var couponApplied by mutableStateOf<ResponseResult<Boolean>>(ResponseResult.Empty)

    var searchASongData by mutableStateOf<ResponseResult<ZeneMusicData>>(ResponseResult.Empty)
    var searchData by mutableStateOf<ResponseResult<SearchDataResponse>>(ResponseResult.Empty)
    var searchPlaces by mutableStateOf<ResponseResult<List<SearchPlacesDataResponse>>>(
        ResponseResult.Empty
    )


    var checkUsernameInfo by mutableStateOf<ResponseResult<Boolean>>(ResponseResult.Empty)
    var updateProfilePhotoInfo by mutableStateOf(false)

    var movieShowInfo by mutableStateOf<ResponseResult<MoviesTvShowResponse>>(ResponseResult.Empty)
    var seasonsMovieShowInfo by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)

    var feedArtists by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)
    var feedUpdates by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)

    var topStoreDeals by mutableStateOf<ResponseResult<StoreDealResponseList>>(ResponseResult.Empty)

    var playlistsData by mutableStateOf<ResponseResult<PodcastPlaylistResponse>>(ResponseResult.Empty)
    var playlistSimilarList by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)
    var radioByCountry by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)
    var podcastCategories by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)
    var artistsInfo by mutableStateOf<ResponseResult<ArtistsResponse>>(ResponseResult.Empty)
    var isPlaylistAdded by mutableStateOf(false)
    var isFollowing by mutableStateOf(false)
    var deleteAccountInfo by mutableStateOf<ResponseResult<DeleteAccountInfoResponse>>(
        ResponseResult.Empty
    )


    var askForReview by mutableStateOf(false)

    fun homeRecentData(expireToken: () -> Unit) = viewModelScope.safeLaunch {
        val data: MusicDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_MUSIC_API)
        if ((data?.topSongs?.size ?: 0) > 0) {
            homeRecent = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.recentHome().onStart {
            homeRecent = ResponseResult.Loading
        }.catch {
            homeRecent = ResponseResult.Error(it)
        }.collectLatest {
            if (it.isExpire == true) {
                expireToken()
                return@collectLatest
            }
            cacheHelper.save(ZENE_RECENT_HOME_MUSIC_API, it)
            homeRecent = ResponseResult.Success(it)
        }
    }

    fun clearSearchKeywordsSuggestions() {
        searchKeywords = ResponseResult.Empty
    }

    fun searchKeywordsData(q: String) = viewModelScope.safeLaunch {
        zeneAPI.searchKeywords(q).onStart {
            searchKeywords = ResponseResult.Loading
        }.catch {
            searchKeywords = ResponseResult.Error(it)
        }.collectLatest {
            searchKeywords = ResponseResult.Success(it)
        }
    }

    fun searchTrendingData() = viewModelScope.safeLaunch {
        val data: SearchTrendingResponse? = cacheHelper.get(ZENE_SEARCH_TRENDING_API)
        if ((data?.songs?.size ?: 0) > 0) {
            searchTrending = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.trendingData().onStart {
            searchTrending = ResponseResult.Loading
        }.catch {
            searchTrending = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_SEARCH_TRENDING_API, it)
            searchTrending = ResponseResult.Success(it)
        }
    }

    fun searchImages(q: ZeneMusicData) = viewModelScope.safeLaunch {
        zeneAPI.searchImages(q).onStart {
            searchImages = ResponseResult.Loading
        }.catch {
            searchImages = ResponseResult.Error(it)
        }.collectLatest {
            searchImages = ResponseResult.Success(it)
        }
    }

    fun trendingAIMusic(expireToken: () -> Unit) = viewModelScope.safeLaunch {
        val data: AIDataResponse? = cacheHelper.get(ZENE_AI_MUSIC_LIST_API)
        if ((data?.trending?.size ?: 0) > 0) {
            aiMusicList = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.trendingAIMusic().onStart {
            aiMusicList = ResponseResult.Loading
        }.catch {
            aiMusicList = ResponseResult.Error(it)
        }.collectLatest {
            if (it.isExpire == true) {
                expireToken()
                return@collectLatest
            }
            cacheHelper.save(ZENE_AI_MUSIC_LIST_API, it)
            aiMusicList = ResponseResult.Success(it)
        }
    }

    fun homePodcastData() = viewModelScope.safeLaunch {
        val data: PodcastDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_PODCAST_API)

        if ((data?.latest?.size ?: 0) > 0) {
            homePodcast = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.recentPodcast().onStart {
            homePodcast = ResponseResult.Loading
        }.catch {
            homePodcast = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_RECENT_HOME_PODCAST_API, it)
            homePodcast = ResponseResult.Success(it)
        }
    }

    fun homeVideosData() = viewModelScope.safeLaunch {
        val data: VideoDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_VIDEOS_API)
        if ((data?.recommended?.size ?: 0) > 0) {
            homeVideos = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.homeVideos().onStart {
            homeVideos = ResponseResult.Loading
        }.catch {
            homeVideos = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_RECENT_HOME_VIDEOS_API, it)
            homeVideos = ResponseResult.Success(it)
        }
    }

    fun homeRadioData() = viewModelScope.safeLaunch {
        val data: RadioDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_RADIO_API)
        if ((data?.recent?.size ?: 0) > 0) {
            homeRadio = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.recentRadio().onStart {
            homeRadio = ResponseResult.Loading
        }.catch {
            homeRadio = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_RECENT_HOME_RADIO_API, it)
            homeRadio = ResponseResult.Success(it)
        }
    }

    fun searchASong(q: String) = viewModelScope.safeLaunch {
        zeneAPI.searchASong(q).onStart {
            searchASongData = ResponseResult.Loading
        }.catch {
            searchASongData = ResponseResult.Error(it)
        }.collectLatest {
            searchASongData = ResponseResult.Success(it)
        }
    }

    fun radioByCountry(name: String) = viewModelScope.safeLaunch {
        zeneAPI.radioByCountry(name).onStart {
            radioByCountry = ResponseResult.Loading
        }.catch {
            radioByCountry = ResponseResult.Error(it)
        }.collectLatest {
            radioByCountry = ResponseResult.Success(it)
        }
    }

    fun podcastCategories(name: String) = viewModelScope.safeLaunch {
        zeneAPI.podcastCategories(name).onStart {
            podcastCategories = ResponseResult.Loading
        }.catch {
            podcastCategories = ResponseResult.Error(it)
        }.collectLatest {
            podcastCategories = ResponseResult.Success(it)
        }
    }

    fun searchZene(q: String) = viewModelScope.safeLaunch {
        searchKeywords = ResponseResult.Empty
        zeneAPI.search(q).onStart {
            searchData = ResponseResult.Loading
        }.catch {
            searchData = ResponseResult.Error(it)
        }.collectLatest {
            searchData = ResponseResult.Success(it)
        }
    }

    @SuppressLint("MissingPermission")
    fun searchPlaces(q: String?) = viewModelScope.safeLaunch {
        searchPlaces = ResponseResult.Loading
        val l = if (q == null) {
            val l = LocationServices.getFusedLocationProviderClient(context)
            val token = CancellationTokenSource().token
            l.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token).await()
        } else null

        zeneAPI.searchPlaces(q, l?.longitude, l?.latitude).onStart {
            searchPlaces = ResponseResult.Loading
        }.catch {
            searchPlaces = ResponseResult.Error(it)
        }.collectLatest {
            searchPlaces = ResponseResult.Success(it)
        }
    }

    fun userInfo() = viewModelScope.safeLaunch {
        val data = DataStorageManager.userInfo.firstOrNull()
        if (data?.isLoggedIn() == false) return@safeLaunch

        zeneAPI.updateUser().catch {}.collectLatest {
            if (it.logout == true) {
                DataStorageManager.userInfo = flowOf(null)
                SnackBarManager.showMessage(context.resources.getString(R.string.login_expired))
                return@collectLatest
            }
            viewModelScope.safeLaunch {
                DataStorageManager.userInfo = flowOf(it)
            }

            isUserPremium()
        }
    }

    fun updateConnectInfo(connectStatus: String) = viewModelScope.safeLaunch {
        zeneAPI.updateConnectStatus(connectStatus).catch {}.collectLatest {
            if (it.status == true) {
                val info = DataStorageManager.userInfo.firstOrNull()
                info?.status = connectStatus
                DataStorageManager.userInfo = flowOf(info)
            }
        }
    }

    fun podcastData(id: String, expireToken: () -> Unit) = viewModelScope.safeLaunch {
        zeneAPI.podcastInfo(id).onStart {
            playlistsData = ResponseResult.Loading
        }.catch {
            playlistsData = ResponseResult.Error(it)
        }.collectLatest {
            if (it.isExpire == true) {
                expireToken()
                return@collectLatest
            }
            isPlaylistAdded = it.isAdded ?: false
            playlistsData = ResponseResult.Success(it)
        }
    }

    fun playlistsData(id: String, expireToken: () -> Unit) = viewModelScope.safeLaunch {
        zeneAPI.playlistsInfo(id).onStart {
            playlistsData = ResponseResult.Loading
        }.catch {
            playlistsData = ResponseResult.Error(it)
        }.collectLatest {
            if (it.isExpire == true) {
                expireToken()
                return@collectLatest
            }
            isPlaylistAdded = it.isAdded == true
            playlistsData = ResponseResult.Success(it)
        }
    }

    fun isPlaylistsAdded(id: String, type: String) = viewModelScope.safeLaunch {
        zeneAPI.isPlaylistAdded(id, type).onStart {
            playlistsData = ResponseResult.Loading
        }.catch {
            playlistsData = ResponseResult.Error(it)
        }.collectLatest {
            isPlaylistAdded = it.status == true
            playlistsData = ResponseResult.Success(PodcastPlaylistResponse(null, null, null, null))
        }
    }

    fun addToPlaylists(status: Boolean, data: PodcastPlaylistResponse, type: PlaylistsType) =
        viewModelScope.safeLaunch {
            isPlaylistAdded = status
            zeneAPI.savePlaylists(data, status, type).catch { }.collectLatest { }
        }

    fun similarPlaylistsData(id: String) = viewModelScope.safeLaunch {
        zeneAPI.similarPodcasts(id).onStart {
            playlistSimilarList = ResponseResult.Loading
        }.catch {
            playlistSimilarList = ResponseResult.Error(it)
        }.collectLatest {
            playlistSimilarList = ResponseResult.Success(it)
        }
    }

    fun artistsInfo(artistsID: String, expireToken: () -> Unit) =
        viewModelScope.safeLaunch {
            zeneAPI.artistsInfo(artistsID).onStart {
                artistsInfo = ResponseResult.Loading
            }.catch {
                artistsInfo = ResponseResult.Error(it)
            }.collectLatest {
                if (it.isExpire == true) {
                    expireToken()
                    return@collectLatest
                }
                artistsInfo = ResponseResult.Success(it)
            }
        }

    fun followArtists(name: String?, doAdd: Boolean, addedMore: () -> Unit) =
        viewModelScope.safeLaunch {
            isFollowing = doAdd
            zeneAPI.followArtists(name, doAdd).catch {}.collectLatest {
                if (!it) {
                    addedMore()
                    isFollowing = false
                }
            }
        }

    private var checkUsernameJob: Job? = null
    fun checkUsername(username: String) {
        checkUsernameJob = viewModelScope.safeLaunch {
            delay(500)
            zeneAPI.checkUsername(username).onStart {
                checkUsernameInfo = ResponseResult.Loading
            }.catch {
                checkUsernameInfo = ResponseResult.Error(it)
            }.collectLatest {
                checkUsernameInfo = ResponseResult.Success(it.status ?: false)
            }
        }
    }


    fun updateUsername(username: String) = viewModelScope.safeLaunch {
        zeneAPI.updateUsername(username).onStart { }.catch {}.collectLatest {}
    }

    fun updateName(name: String) = viewModelScope.safeLaunch {
        zeneAPI.updateName(name).onStart {}.catch {}.collectLatest {}
    }

    private fun isUserPremium() = viewModelScope.safeLaunch {
        zeneAPI.isUserPremium().catch {}.collectLatest {
            DataStorageManager.isPremiumDB = flowOf(it.status == true)
        }
    }

    fun updateProfilePhoto(photo: Uri?) = viewModelScope.safeLaunch {
        zeneAPI.updatePhoto(photo).onStart {
            updateProfilePhotoInfo = true
        }.catch {
            updateProfilePhotoInfo = false
        }.collectLatest {
            updateProfilePhotoInfo = false
            userInfo()
        }
    }

    fun updateSubscriptionToken(token: String?, subscriptionId: String?, done: () -> Unit) =
        viewModelScope.safeLaunch {
            token ?: return@safeLaunch
            zeneAPI.updateSubscription(token, subscriptionId).onStart {}.catch {}.collectLatest {
                viewModelScope.safeLaunch {
                    DataStorageManager.isPremiumDB = flowOf(it.status == true)
                }
                delay(3.seconds)
                done()
            }
        }

    fun applyCouponCode(couponCode: String?, done: () -> Unit) =
        viewModelScope.safeLaunch {
            zeneAPI.updateCoupon(couponCode).onStart {
                couponApplied = ResponseResult.Loading
            }.catch {
                SnackBarManager.showMessage(context.resources.getString(R.string.error_reading_coupon_code_try_again))
                couponApplied = ResponseResult.Error(it)
            }.collectLatest {
                couponApplied = ResponseResult.Empty
                if (it.error == true) {
                    SnackBarManager.showMessage(context.resources.getString(R.string.error_reading_coupon_code_try_again))
                } else if (it.isAvailable == true) {
                    couponApplied = ResponseResult.Success(true)
                    viewModelScope.safeLaunch {
                        DataStorageManager.isPremiumDB = flowOf(true)
                    }
                    delay(3.seconds)
                    done()
                } else {
                    SnackBarManager.showMessage(context.resources.getString(R.string.no_coupon_found_try_again))
                }
            }
        }

    fun moviesTvShowsInfo(id: String) = viewModelScope.safeLaunch {
        zeneAPI.moviesTvShowsInfo(id).onStart {
            movieShowInfo = ResponseResult.Loading
        }.catch {
            movieShowInfo = ResponseResult.Error(it)
        }.collectLatest {
            movieShowInfo = ResponseResult.Success(it)
        }
    }

    fun moviesTvShowsSeasonsInfo(id: String) = viewModelScope.safeLaunch {
        zeneAPI.seasonMoviesTvShowsInfo(id).onStart {
            seasonsMovieShowInfo = ResponseResult.Loading
        }.catch {
            seasonsMovieShowInfo = ResponseResult.Error(it)
        }.collectLatest {
            seasonsMovieShowInfo = ResponseResult.Success(it)
        }
    }

    fun sponsorAds() = viewModelScope.safeLaunch {
        zeneAPI.sponsorAds().catch {}.collectLatest {
            sponsorAdsDB = flowOf(it.android)
        }
    }


    fun artistsFollowedList() = viewModelScope.safeLaunch {
        zeneAPI.feedFollowedArtists().onStart {
            feedArtists = ResponseResult.Loading
        }.catch {
            feedArtists = ResponseResult.Error(it)
        }.collectLatest {
            feedArtists = ResponseResult.Success(it)
        }
    }

    fun feedUpdatesArtists() = viewModelScope.safeLaunch {
        zeneAPI.feedUpdatesArtists().onStart {
            feedUpdates = ResponseResult.Loading
        }.catch {
            feedUpdates = ResponseResult.Error(it)
        }.collectLatest {
            feedUpdates = ResponseResult.Success(it)
        }
    }

    fun deleteAccount() = viewModelScope.safeLaunch {
        zeneAPI.deleteAccount().catch { }.firstOrNull()
    }

    fun cancelDeleteAccount() = viewModelScope.safeLaunch {
        zeneAPI.cancelDeleteAccount().catch { }.firstOrNull()
    }

    fun deleteAccountInfo() = viewModelScope.safeLaunch {
        zeneAPI.deleteAccountInfo().onStart {
            deleteAccountInfo = ResponseResult.Loading
        }.catch {
            deleteAccountInfo = ResponseResult.Error(it)
        }.collectLatest {
            deleteAccountInfo = ResponseResult.Success(it)
        }
    }

    fun updateEmailSubscription(v: Boolean) = viewModelScope.safeLaunch {
        zeneAPI.updateEmailSubscription(v).catch {}.collectLatest {
            userInfo()
        }
    }


    fun askUserForReview() = viewModelScope.safeLaunch {
        delay(5.seconds)
        val now = System.currentTimeMillis()
        val lastDismiss = isPostedReviewTimestampDB.firstOrNull() ?: 0
        val daysPassed = (now - lastDismiss) / (1000 * 60 * 60 * 24)

        val shouldShow = !isPostedReviewDB.first() && daysPassed >= 6

        if (!shouldShow) return@safeLaunch

        zeneAPI.getHistory(0).catch {}.collectLatest {
            if (it.size > 5) askForReview = true
        }
    }


    fun storeTopDeals() = viewModelScope.safeLaunch {
        val data: StoreDealResponseList? = cacheHelper.get(ZENE_STORE_TOP_DEALS_API)
        if ((data?.size ?: 0) > 0) {
            topStoreDeals = ResponseResult.Success(data!!)
            return@safeLaunch
        }

        zeneAPI.storeTopDeals().onStart {
            topStoreDeals = ResponseResult.Loading
        }.catch {
            topStoreDeals = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_STORE_TOP_DEALS_API, it)
            topStoreDeals = ResponseResult.Success(it)
        }
    }

}