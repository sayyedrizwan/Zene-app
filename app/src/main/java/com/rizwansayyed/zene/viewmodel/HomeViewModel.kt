package com.rizwansayyed.zene.viewmodel

import android.annotation.SuppressLint
import android.net.Uri
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
import com.rizwansayyed.zene.data.model.EntertainmentDataResponse
import com.rizwansayyed.zene.data.model.MoviesDataResponse
import com.rizwansayyed.zene.data.model.MoviesTvShowResponse
import com.rizwansayyed.zene.data.model.MusicDataResponse
import com.rizwansayyed.zene.data.model.PodcastDataResponse
import com.rizwansayyed.zene.data.model.PodcastPlaylistResponse
import com.rizwansayyed.zene.data.model.RadioDataResponse
import com.rizwansayyed.zene.data.model.SearchDataResponse
import com.rizwansayyed.zene.data.model.SearchPlacesDataResponse
import com.rizwansayyed.zene.data.model.SearchTrendingResponse
import com.rizwansayyed.zene.data.model.VideoDataResponse
import com.rizwansayyed.zene.data.model.ZeneMusicData
import com.rizwansayyed.zene.data.model.ZeneMusicDataList
import com.rizwansayyed.zene.datastore.DataStorageManager
import com.rizwansayyed.zene.di.ZeneBaseApplication.Companion.context
import com.rizwansayyed.zene.ui.login.utils.LoginUtils
import com.rizwansayyed.zene.ui.phoneverification.view.TrueCallerUtils
import com.rizwansayyed.zene.ui.view.playlist.PlaylistsType
import com.rizwansayyed.zene.utils.MainUtils.toast
import com.rizwansayyed.zene.utils.SnackBarManager
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_AI_MUSIC_LIST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_ENTERTAINMENT_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_ENTERTAINMENT_MOVIES_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_MUSIC_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_PODCAST_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_RADIO_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_RECENT_HOME_VIDEOS_API
import com.rizwansayyed.zene.utils.URLSUtils.ZENE_SEARCH_TRENDING_API
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    val loginUtils: LoginUtils,
    val trueCallerUtils: TrueCallerUtils,
    private val zeneAPI: ZeneAPIInterface
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
    var entertainmentData by mutableStateOf<ResponseResult<EntertainmentDataResponse>>(
        ResponseResult.Empty
    )
    var entertainmentMoviesData by mutableStateOf<ResponseResult<MoviesDataResponse>>(
        ResponseResult.Empty
    )

    var searchASongData by mutableStateOf<ResponseResult<ZeneMusicData>>(ResponseResult.Empty)
    var searchData by mutableStateOf<ResponseResult<SearchDataResponse>>(ResponseResult.Empty)
    var searchPlaces by mutableStateOf<ResponseResult<List<SearchPlacesDataResponse>>>(
        ResponseResult.Empty
    )


    var checkUsernameInfo by mutableStateOf<ResponseResult<Boolean>>(ResponseResult.Empty)
    var updateProfilePhotoInfo by mutableStateOf(false)

    var movieShowInfo by mutableStateOf<ResponseResult<MoviesTvShowResponse>>(ResponseResult.Empty)
    var seasonsMovieShowInfo by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)

    var playlistsData by mutableStateOf<ResponseResult<PodcastPlaylistResponse>>(ResponseResult.Empty)
    var playlistSimilarList by mutableStateOf<ResponseResult<ZeneMusicDataList>>(ResponseResult.Empty)
    var artistsInfo by mutableStateOf<ResponseResult<ArtistsResponse>>(ResponseResult.Empty)
    var isPlaylistAdded by mutableStateOf(false)
    var isFollowing by mutableStateOf(false)

    fun homeRecentData(expireToken: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val data: MusicDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_MUSIC_API)
        if ((data?.topSongs?.size ?: 0) > 0) {
            homeRecent = ResponseResult.Success(data!!)
            return@launch
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

    fun searchKeywordsData(q: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.searchKeywords(q).onStart {
            searchKeywords = ResponseResult.Loading
        }.catch {
            searchKeywords = ResponseResult.Error(it)
        }.collectLatest {
            searchKeywords = ResponseResult.Success(it)
        }
    }

    fun searchTrendingData() = viewModelScope.launch(Dispatchers.IO) {
        val data: SearchTrendingResponse? = cacheHelper.get(ZENE_SEARCH_TRENDING_API)
        if ((data?.songs?.size ?: 0) > 0) {
            searchTrending = ResponseResult.Success(data!!)
            return@launch
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

    fun searchImages(q: ZeneMusicData) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.searchImages(q).onStart {
            searchImages = ResponseResult.Loading
        }.catch {
            searchImages = ResponseResult.Error(it)
        }.collectLatest {
            searchImages = ResponseResult.Success(it)
        }
    }

    fun trendingAIMusic(expireToken: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        val data: AIDataResponse? = cacheHelper.get(ZENE_AI_MUSIC_LIST_API)
        if ((data?.trending?.size ?: 0) > 0) {
            aiMusicList = ResponseResult.Success(data!!)
            return@launch
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

    fun homePodcastData() = viewModelScope.launch(Dispatchers.IO) {
        val data: PodcastDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_PODCAST_API)

        if ((data?.latest?.size ?: 0) > 0) {
            homePodcast = ResponseResult.Success(data!!)
            return@launch
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

    fun homeVideosData() = viewModelScope.launch(Dispatchers.IO) {
        val data: VideoDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_VIDEOS_API)
        if ((data?.recommended?.size ?: 0) > 0) {
            homeVideos = ResponseResult.Success(data!!)
            return@launch
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

    fun homeRadioData() = viewModelScope.launch(Dispatchers.IO) {
        val data: RadioDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_RADIO_API)
        if ((data?.recent?.size ?: 0) > 0) {
            homeRadio = ResponseResult.Success(data!!)
            return@launch
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


    fun entertainmentNewsData() = viewModelScope.launch(Dispatchers.IO) {
        val data: EntertainmentDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_ENTERTAINMENT_API)
        if ((data?.topNews?.size ?: 0) > 0) {
            entertainmentData = ResponseResult.Success(data!!)
            return@launch
        }

        zeneAPI.entertainmentNews().onStart {
            entertainmentData = ResponseResult.Loading
        }.catch {
            entertainmentData = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_RECENT_HOME_ENTERTAINMENT_API, it)
            entertainmentData = ResponseResult.Success(it)
        }
    }

    fun entertainmentMovies() = viewModelScope.launch(Dispatchers.IO) {
        val data: MoviesDataResponse? = cacheHelper.get(ZENE_RECENT_HOME_ENTERTAINMENT_MOVIES_API)
        if ((data?.trendingMovies?.size ?: 0) > 0) {
            entertainmentMoviesData = ResponseResult.Success(data!!)
            return@launch
        }

        zeneAPI.entertainmentMovies().onStart {
            entertainmentMoviesData = ResponseResult.Loading
        }.catch {
            entertainmentMoviesData = ResponseResult.Error(it)
        }.collectLatest {
            cacheHelper.save(ZENE_RECENT_HOME_ENTERTAINMENT_MOVIES_API, it)
            entertainmentMoviesData = ResponseResult.Success(it)
        }
    }

    fun searchASong(q: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.searchASong(q).onStart {
            searchASongData = ResponseResult.Loading
        }.catch {
            searchASongData = ResponseResult.Error(it)
        }.collectLatest {
            searchASongData = ResponseResult.Success(it)
        }
    }

    fun searchZene(q: String) = viewModelScope.launch(Dispatchers.IO) {
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
    fun searchPlaces(q: String?) = viewModelScope.launch(Dispatchers.IO) {
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

    fun userInfo() = viewModelScope.launch(Dispatchers.IO) {
        val data = DataStorageManager.userInfo.firstOrNull()
        if (data?.isLoggedIn() == false) return@launch

        zeneAPI.updateUser().catch {}.collectLatest {
            if (it.logout == true) {
                DataStorageManager.userInfo = flowOf(null)
                SnackBarManager.showMessage(context.resources.getString(R.string.login_expired))
                return@collectLatest
            }
            viewModelScope.launch(Dispatchers.IO) {
                DataStorageManager.userInfo = flowOf(it)
            }
            isUserPremium()
        }
    }

    fun updateConnectInfo(connectStatus: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.updateConnectStatus(connectStatus).catch {}.collectLatest {
            if (it.status == true) {
                val info = DataStorageManager.userInfo.firstOrNull()
                info?.status = connectStatus
                DataStorageManager.userInfo = flowOf(info)
            }
        }
    }

    fun podcastData(id: String, expireToken: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
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

    fun playlistsData(id: String, expireToken: () -> Unit) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.playlistsInfo(id).onStart {
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

    fun isPlaylistsAdded(id: String, type: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.isPlaylistAdded(id, type).onStart {
            playlistsData = ResponseResult.Loading
        }.catch {
            playlistsData = ResponseResult.Error(it)
        }.collectLatest {
            isPlaylistAdded = it.status ?: false
            playlistsData = ResponseResult.Success(PodcastPlaylistResponse(null, null, null, null))
        }
    }

    fun addToPlaylists(status: Boolean, data: PodcastPlaylistResponse, type: PlaylistsType) =
        viewModelScope.launch(Dispatchers.IO) {
            isPlaylistAdded = status
            zeneAPI.savePlaylists(data, status, type).catch { }.collectLatest { }
        }

    fun similarPlaylistsData(id: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.similarPodcasts(id).onStart {
            playlistSimilarList = ResponseResult.Loading
        }.catch {
            playlistSimilarList = ResponseResult.Error(it)
        }.collectLatest {
            playlistSimilarList = ResponseResult.Success(it)
        }
    }

    fun artistsInfo(artistsID: String, expireToken: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
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
        checkUsernameJob = viewModelScope.launch(Dispatchers.IO) {
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


    fun updateUsername(username: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.updateUsername(username).onStart { }.catch {}.collectLatest {}
    }

    fun updateName(name: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.updateName(name).onStart {}.catch {}.collectLatest {}
    }

    private fun isUserPremium() = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.isUserPremium().catch {}.collectLatest {
            DataStorageManager.isPremiumDB = flowOf(it.status ?: false)
        }
    }

    fun updateProfilePhoto(photo: Uri?) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.updatePhoto(photo).onStart {
            updateProfilePhotoInfo = true
        }.catch {
            updateProfilePhotoInfo = false
        }.collectLatest {
            updateProfilePhotoInfo = false
            userInfo()
        }
    }

    fun updateSubscriptionToken(token: String?, done: () -> Unit) =
        viewModelScope.launch(Dispatchers.IO) {
            token ?: return@launch
            zeneAPI.updateSubscription(token).onStart {}.catch {}.collectLatest {
                delay(3.seconds)
                done()
            }
        }

    fun moviesTvShowsInfo(id: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.moviesTvShowsInfo(id).onStart {
            movieShowInfo = ResponseResult.Loading
        }.catch {
            movieShowInfo = ResponseResult.Error(it)
        }.collectLatest {
            movieShowInfo = ResponseResult.Success(it)
        }
    }

    fun moviesTvShowsSeasonsInfo(id: String) = viewModelScope.launch(Dispatchers.IO) {
        zeneAPI.seasonMoviesTvShowsInfo(id).onStart {
            seasonsMovieShowInfo = ResponseResult.Loading
        }.catch {
            seasonsMovieShowInfo = ResponseResult.Error(it)
        }.collectLatest {
            seasonsMovieShowInfo = ResponseResult.Success(it)
        }
    }
}