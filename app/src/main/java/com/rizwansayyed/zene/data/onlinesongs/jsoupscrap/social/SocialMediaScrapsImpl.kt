package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.social


import android.util.Log
import com.rizwansayyed.zene.data.db.artistsfeed.ArtistsFeedEntity
import com.rizwansayyed.zene.data.db.artistsfeed.FeedPostType
import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.onlinesongs.instagram.InstagramInfoService
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing.BingScrapsInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.facebook.FacebookScrapsImplInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.youtubescrap.YoutubeScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.utils.Utils.toLongWithPlaceHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class SocialMediaScrapsImpl @Inject constructor(
    private val roomDb: RoomDBInterface,
    private val instagramService: InstagramInfoService,
    private val bingScrap: BingScrapsInterface,
    private val youtubeScrap: YoutubeScrapsImpl,
    private val remoteConfig: RemoteConfigInterface
) : SocialMediaScrapsImplInterface {
    override suspend fun getAllArtistsData(a: PinnedArtistsEntity) = job.launch {
        roomDb.deleteAll().first()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val instagram = instagramService
                    .instagramInfo(remoteConfig.instagramAppID(), a.instagramUsername)

                instagram.data?.user?.edge_owner_to_timeline_media?.edges?.forEach {
                    val media =
                        if (it?.node?.is_video == true) it.node.video_url else it?.node?.thumbnail_src
                    val caption = it?.node?.edge_media_to_caption?.edges?.map { i -> i?.node?.text }
                        ?.joinToString(" ")

                    val v = ArtistsFeedEntity(
                        null, a.name, a.instagramUsername,
                        "${it?.node?.taken_at_timestamp}000".toLongWithPlaceHolder(),
                        FeedPostType.INSTAGRAM, media, it?.node?.is_video ?: false,
                        caption ?: "", null, it?.node?.shortcode ?: ""
                    )
                    roomDb.insert(v).collect()
                }
            } catch (e: Exception) {
                e.message
            }

            if (isActive) cancel()
        }

        CoroutineScope(Dispatchers.IO).launch {
            bingScrap.bingNews(a.name).catch { }.collectLatest {
                it.forEach { news ->
                    val v = ArtistsFeedEntity(
                        null, a.name, a.name, news.timeStamp(), FeedPostType.NEWS,
                        news.img, false, news.title ?: "", news.desc, news.url
                    )
                    roomDb.insert(v).collect()
                }
            }

            if (isActive) cancel()
        }



        CoroutineScope(Dispatchers.IO).launch {
            val id =
                if (a.youtubeChannel.contains("channel")) a.youtubeChannel.substringAfter("channel/")
                else youtubeScrap.getChannelId(a.youtubeChannel).first()

            Log.d("TAG", "getAllArtistsData: getdata $id")

            if (isActive) cancel()
        }
    }

}