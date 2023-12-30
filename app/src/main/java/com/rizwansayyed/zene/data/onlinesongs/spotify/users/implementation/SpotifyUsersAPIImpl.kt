package com.rizwansayyed.zene.data.onlinesongs.spotify.users.implementation

import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.spotifyToken
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Days
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.spotify.music.SpotifyAPIService
import com.rizwansayyed.zene.data.onlinesongs.spotify.users.SpotifyUsersAPIService
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.CacheFiles.topCountrySongs
import com.rizwansayyed.zene.data.utils.CacheFiles.topGlobalSongs
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_COUNTRY_SEARCH
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_GLOBAL_SEARCH
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataCache
import com.rizwansayyed.zene.domain.spotify.music.SpotifyItem
import com.rizwansayyed.zene.domain.toTxtCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpotifyUsersAPIImpl @Inject constructor(
    private val spotifyAPI: SpotifyUsersAPIService,
) : SpotifyUsersAPIImplInterface {

    override suspend fun usersPlaylist() = flow {
        emit(spotifyAPI.userPlaylist(spotifyToken.first()))
    }.flowOn(Dispatchers.IO)


}