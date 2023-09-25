package com.rizwansayyed.zene.data.onlinesongs.spotify.implementation

import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Days
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.ip.IpJsonService
import com.rizwansayyed.zene.data.onlinesongs.spotify.SpotifyAPIService
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeAPIService
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeMusicAPIService
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.CacheFiles.topCountrySongs
import com.rizwansayyed.zene.data.utils.CacheFiles.topGlobalSongs
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_COUNTRY_SEARCH
import com.rizwansayyed.zene.data.utils.SpotifyAPI.SPOTIFY_GLOBAL_SEARCH
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.data.utils.config.RemoteConfigManager
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.domain.MusicDataCache
import com.rizwansayyed.zene.domain.spotify.SpotifyTracksCacheResponse
import com.rizwansayyed.zene.domain.spotify.toTxtCache
import com.rizwansayyed.zene.domain.toTxtCache
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
    private val ipJson: IpJsonService,
    private val youtubeMusic: YoutubeAPIImplInterface,
    private val remoteConfig: RemoteConfigInterface,
) : SpotifyAPIImplInterface {

    override suspend fun globalTrendingSongs() = flow {
        val cache = responseCache(topGlobalSongs, MusicDataCache::class.java)
        if (cache != null) {
            if (returnFromCache2Days(cache.cacheTime) && cache.list.isNotEmpty()) {
                emit(cache.list)
                return@flow
            }
        }
        val ip = ipJson.ip()
        val key = remoteConfig.ytApiKeys()

        val token = spotifyAPI.spotifyAccessToken()
        val bearer = "${token.token_type} ${token.access_token}"
        val pid = spotifyAPI
            .spotifyPlaylistSearch(bearer, SPOTIFY_GLOBAL_SEARCH).playlists?.items?.first()?.id
            ?: return@flow

        val songs = spotifyAPI.spotifyPlaylistSongs(bearer, pid)

        val music = mutableListOf<MusicData>()
        songs.tracks?.items?.forEach { s ->
            val sortName = "${s.track?.album?.name} - ${s.track?.wholeArtistsName()}"
            youtubeMusic.musicInfoSearch(sortName, ip, key?.music ?: "")?.let { m -> music.add(m) }
        }
        music.shuffle()
        music.toTxtCache()?.let { writeToCacheFile(topGlobalSongs, it) }

        emit(music)
    }.flowOn(Dispatchers.IO)


    override suspend fun topSongsInCountry() = flow {
        val cache = responseCache(topCountrySongs, MusicDataCache::class.java)
        if (cache != null) {
            if (returnFromCache2Days(cache.cacheTime) && cache.list.isNotEmpty()) {
                emit(cache.list)
                return@flow
            }
        }

        val key = remoteConfig.ytApiKeys()
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

        val music = mutableListOf<MusicData>()
        songs.tracks?.items?.forEach { s ->
            val sortName = "${s.track?.album?.name} - ${s.track?.wholeArtistsName()}"
            youtubeMusic.musicInfoSearch(sortName, ipDetails, key?.music ?: "")?.let { m -> music.add(m) }
        }
        music.shuffle()
        music.toTxtCache()?.let { writeToCacheFile(topCountrySongs, it) }

        emit(music)
    }.flowOn(Dispatchers.IO)

}