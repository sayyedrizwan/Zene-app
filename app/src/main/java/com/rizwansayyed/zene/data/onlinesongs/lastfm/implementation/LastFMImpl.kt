package com.rizwansayyed.zene.data.onlinesongs.lastfm.implementation

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager
import com.rizwansayyed.zene.data.onlinesongs.lastfm.LastFMService
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.LastFM.searchLastFMImageURLPath
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.domain.MusicData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LastFMImpl @Inject constructor(
    private val lastFMS: LastFMService,
    private val youtubeMusic: YoutubeAPIImplInterface,
    private val remoteConfig: RemoteConfigInterface,
) : LastFMImplInterface {

    override suspend fun topRecentPlayingSongs() = flow {
        val ip = DataStorageManager.userIpDetails.first()
        val key = remoteConfig.ytApiKeys()

        val res = lastFMS.topRecentPlayingSongs()
        val song = res.results?.artist?.random()

        val songName = "${song?.tracks?.first()?.name} - ${song?.name}"
        val songs = youtubeMusic.musicInfoSearch(songName, ip, key?.music ?: "")
        emit(songs)
    }.flowOn(Dispatchers.IO)


    override suspend fun artistsImages(name: String, limit: Int) = flow {
        val list = mutableListOf<String>()
        val artistDetails = lastFMS.searchArtists(name).results?.artistmatches?.artist?.first()
        var imgId = artistDetails?.image?.substringAfterLast("/")?.substringBeforeLast(".")

        if (limit == 1) {
            artistDetails?.image?.let { list.add(it) }
            emit(list)
            return@flow
        }

        while (list.size <= limit) {
            val img = lastFMS.artistsImages(searchLastFMImageURLPath(imgId ?: ""))
            img.values.forEach {
                it.src?.let { img -> list.add(img) }
                imgId = it.next?.substringAfterLast("/")
            }
        }
        emit(list)
    }
}