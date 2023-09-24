package com.rizwansayyed.zene.data.onlinesongs.youtube.implementation

import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache1Hour
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Days
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.ip.IpJsonService
import com.rizwansayyed.zene.data.onlinesongs.radio.OnlineRadioService
import com.rizwansayyed.zene.data.onlinesongs.radio.activeRadioBaseURL
import com.rizwansayyed.zene.data.onlinesongs.spotify.SpotifyAPIService
import com.rizwansayyed.zene.data.utils.CacheFiles.radioList
import com.rizwansayyed.zene.data.utils.CacheFiles.topCountrySongs
import com.rizwansayyed.zene.data.utils.CacheFiles.topGlobalSongs
import com.rizwansayyed.zene.data.utils.RadioOnlineAPI.RADIO_BASE_URL
import com.rizwansayyed.zene.data.utils.RadioOnlineAPI.radioSearchAPI
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_GLOBAL_SEARCH
import com.rizwansayyed.zene.domain.OnlineRadioCacheResponse
import com.rizwansayyed.zene.domain.spotify.SpotifyTracksCacheResponse
import com.rizwansayyed.zene.domain.spotify.toTxtCache
import com.rizwansayyed.zene.domain.toTxtCache
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.internal.filterList
import javax.inject.Inject

class YoutubeAPIImpl @Inject constructor(
    private val spotifyAPI: SpotifyAPIService,
    private val ipJson: IpJsonService
) : YoutubeAPIImplInterface {

    suspend fun newReleaseMusic() = flow {



        emit("")
    }.flowOn(Dispatchers.IO)
}