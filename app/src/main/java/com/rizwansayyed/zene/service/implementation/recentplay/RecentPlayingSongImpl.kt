package com.rizwansayyed.zene.service.implementation.recentplay

import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedDao
import com.rizwansayyed.zene.data.db.recentplay.RecentPlayedEntity
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.presenter.util.UiUtils.ContentTypes.THE_VIDEO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject


class RecentPlayingSongImpl @Inject constructor(
    private val player: ExoPlayer,
    private val recentPlayed: RecentPlayedDao,
    private val youtubeAPI: YoutubeAPIImplInterface
) : RecentPlayingSongInterface {

    override suspend fun updateRecentPlayingSongInfo() = flow {
        if (!player.isPlaying) return@flow

        withContext(Dispatchers.Main) {
            val checkIsVideo = player.currentMediaItem?.mediaMetadata?.artist
            if (checkIsVideo == THE_VIDEO) return@withContext

            val halfwayPointMillis = player.duration / 2
            if (player.currentPosition in 2000..5000 || player.duration - player.currentPosition in 4000..8000 || (player.currentPosition >= halfwayPointMillis - 2000 && player.currentPosition <= halfwayPointMillis + 2000)) {
                val addValue =
                    if (player.currentPosition >= halfwayPointMillis - 2000 && player.currentPosition <= halfwayPointMillis + 2000) 2
                    else if (player.duration - player.currentPosition in 4000..8000) 4
                    else 1

                val mediaId = player.currentMediaItem?.mediaId ?: return@withContext
                val duration = player.duration
                val currentPosition = player.currentPosition

                withContext(Dispatchers.IO) {
                    val recentSongInfo = recentPlayed.search(mediaId)
                    if (recentSongInfo != null) {
                        recentSongInfo.playTimes += addValue
                        recentSongInfo.timestamp = System.currentTimeMillis()
                        recentSongInfo.playerDuration = duration
                        recentSongInfo.lastListenDuration = currentPosition
                        recentPlayed.insert(recentSongInfo)
                    } else {
                        val songInfo = youtubeAPI.songDetail(mediaId).first()
                        if (songInfo.pId == null) return@withContext

                        val data = RecentPlayedEntity(
                            songInfo.pId!!, songInfo.name, songInfo.artists, addValue,
                            songInfo.thumbnail, System.currentTimeMillis(), duration,
                            currentPosition
                        )
                        recentPlayed.insert(data)
                    }
                }
            }
        }

        emit("")
    }.flowOn(Dispatchers.Main)


    override suspend fun updateLatestListenTiming() = flow {
        withContext(Dispatchers.Main) {
            val mediaId = player.currentMediaItem?.mediaId ?: return@withContext
            val currentPosition = player.currentPosition
            withContext(Dispatchers.IO) {
                recentPlayed.updateTime(mediaId, currentPosition)
            }
        }
        emit("")
    }.flowOn(Dispatchers.Main)
}