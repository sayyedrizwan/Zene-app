package com.rizwansayyed.zene.data.onlinesongs.spotify.music.implementation

import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.onlinesongs.cache.responseCache
import com.rizwansayyed.zene.data.onlinesongs.cache.returnFromCache2Days
import com.rizwansayyed.zene.data.onlinesongs.cache.writeToCacheFile
import com.rizwansayyed.zene.data.onlinesongs.spotify.music.SpotifyAPIService
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

class SpotifyAPIImpl @Inject constructor(
    private val spotifyAPI: SpotifyAPIService,
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
        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()

        val token = spotifyAPI.spotifyAccessToken()
        val bearer = "${token.token_type} ${token.access_token}"
        val pid = spotifyAPI
            .spotifyPlaylistSearch(bearer, SPOTIFY_GLOBAL_SEARCH).playlists?.items?.first()?.id
            ?: return@flow

        val songs = spotifyAPI.spotifyPlaylistSongs(bearer, pid)

        val music = mutableListOf<MusicData>()

        suspend fun synList(s: SpotifyItem) {
            val sortName = "${s.track?.album?.name} - ${(s.track?.wholeArtistsName())}"
            youtubeMusic.musicInfoSearch(sortName, ip, key?.music ?: "")
                ?.let { m -> if (!music.contains(m)) music.add(m) }
        }

        withContext(Dispatchers.IO) {
            val first = async {
                songs.tracks?.items?.forEachIndexed { index, s ->
                    if (index <= songs.tracks.items.size / 2) synList(s)
                }
            }

            val second = async {
                songs.tracks?.items?.forEachIndexed { index, s ->
                    if (index >= songs.tracks.items.size / 2) synList(s)
                }
            }

            first.await()
            second.await()
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

        val key = remoteConfig.allApiKeys()
        val ipDetails = userIpDetails.first()

        val token = spotifyAPI.spotifyAccessToken()
        val bearer = "${token.token_type} ${token.access_token}"
        val pid = spotifyAPI.spotifyPlaylistSearch(
            bearer, "$SPOTIFY_COUNTRY_SEARCH${ipDetails?.country}"
        ).playlists?.items?.first()?.id ?: return@flow

        val songs = spotifyAPI.spotifyPlaylistSongs(bearer, pid)

        val music = mutableListOf<MusicData>()

        suspend fun synList(s: SpotifyItem) {
            val sortName = "${s.track?.album?.name} - ${(s.track?.wholeArtistsName())}"
            youtubeMusic.musicInfoSearch(sortName, ipDetails, key?.music ?: "")
                ?.let { m -> if (!music.contains(m)) music.add(m) }
        }

        withContext(Dispatchers.IO) {
            val first = async {
                songs.tracks?.items?.forEachIndexed { index, s ->
                    if (index <= songs.tracks.items.size / 2) synList(s)
                }
            }

            val second = async {
                songs.tracks?.items?.forEachIndexed { index, s ->
                    if (index >= songs.tracks.items.size / 2) synList(s)
                }
            }

            first.await()
            second.await()
        }
        music.shuffle()
        music.toTxtCache()?.let { writeToCacheFile(topCountrySongs, it) }

        emit(music)
    }.flowOn(Dispatchers.IO)

}