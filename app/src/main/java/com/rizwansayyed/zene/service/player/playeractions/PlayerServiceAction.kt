package com.rizwansayyed.zene.service.player.playeractions

import androidx.media3.exoplayer.ExoPlayer
import com.rizwansayyed.zene.data.onlinesongs.downloader.implementation.SongDownloaderInterface
import com.rizwansayyed.zene.domain.MusicData
import com.rizwansayyed.zene.service.player.utils.Utils.toMediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlayerServiceAction @Inject constructor(
    private val player: ExoPlayer,
    private val songDownloader: SongDownloaderInterface
) : PlayerServiceActionInterface {

    override suspend fun addMultipleItemsAndPlay(list: Array<MusicData>?, position: Int) {
        withContext(Dispatchers.Main) {
            player.clearMediaItems()

            list?.forEach { m ->
                player.addMediaItem(m.toMediaItem(m.pId ?: ""))
            }

            var url: String?

            withContext(Dispatchers.IO) {
                url = try {
                    val music = list?.get(position)
                    songDownloader.download(music?.pId!!).first()
                } catch (e: Exception) {
                    null
                }
            }

            url ?: return@withContext

            val m = list?.get(position)?.toMediaItem(url!!) ?: return@withContext
            player.replaceMediaItem(position, m)
            player.seekTo(position, 0)


            player.playWhenReady = true
            player.prepare()
            player.play()
        }
    }
}