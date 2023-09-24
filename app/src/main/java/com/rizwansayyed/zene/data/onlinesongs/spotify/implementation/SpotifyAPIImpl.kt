package com.rizwansayyed.zene.data.onlinesongs.spotify.implementation

import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Days
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.ip.IpJsonService
import com.rizwansayyed.zene.data.onlinesongs.spotify.SpotifyAPIService
import com.rizwansayyed.zene.data.utils.CacheFiles.topCountrySongs
import com.rizwansayyed.zene.data.utils.CacheFiles.topGlobalSongs
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_COUNTRY_SEARCH
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_GLOBAL_SEARCH
import com.rizwansayyed.zene.domain.spotify.SpotifyTracksCacheResponse
import com.rizwansayyed.zene.domain.spotify.toTxtCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class SpotifyAPIImpl @Inject constructor(
    private val spotifyAPI: SpotifyAPIService,
    private val ipJson: IpJsonService
) : SpotifyAPIImplInterface {

    override suspend fun globalTrendingSongs() = flow {
        val cache = responseCache(topGlobalSongs, SpotifyTracksCacheResponse::class.java)
        if (cache != null) {
            if (returnFromCache2Days(cache.cacheTime) && cache.list.isNotEmpty()) {
                emit(cache.list)
                return@flow
            }
        }

        val token = spotifyAPI.spotifyAccessToken()
        val bearer = "${token.token_type} ${token.access_token}"
        val pid = spotifyAPI
            .spotifyPlaylistSearch(bearer, SPOTIFY_GLOBAL_SEARCH).playlists?.items?.first()?.id
            ?: return@flow

        val songs = spotifyAPI.spotifyPlaylistSongs(bearer, pid)
        val lists = songs.tracks?.items?.shuffled()
        lists?.toTxtCache()?.let { writeToCacheFile(topGlobalSongs, it) }

        emit(lists)
    }.flowOn(Dispatchers.IO)


    override suspend fun topSongsInCountry() = flow {
        val cache = responseCache(topCountrySongs, SpotifyTracksCacheResponse::class.java)
        if (cache != null) {
            if (returnFromCache2Days(cache.cacheTime) && cache.list.isNotEmpty()) {
                emit(cache.list)
                return@flow
            }
        }

        val ipDetails = ipJson.ip()
        CoroutineScope(Dispatchers.IO).launch {
            userIpDetails = flowOf(ipDetails)
            if (isActive) cancel()
        }

        val token = spotifyAPI.spotifyAccessToken()
        val bearer = "${token.token_type} ${token.access_token}"
        val pid = spotifyAPI.spotifyPlaylistSearch(
            bearer, "$SPOTIFY_COUNTRY_SEARCH${ipDetails.country}"
        ).playlists?.items?.first()?.id ?: return@flow

        val songs = spotifyAPI.spotifyPlaylistSongs(bearer, pid)
        val lists = songs.tracks?.items?.shuffled()
        lists?.toTxtCache()?.let { writeToCacheFile(topCountrySongs, it) }

        emit(lists)
    }.flowOn(Dispatchers.IO)

}