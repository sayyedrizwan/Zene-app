package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.social


import android.util.Log
import com.rizwansayyed.zene.data.db.artistsfeed.ArtistsFeedEntity
import com.rizwansayyed.zene.data.db.artistsfeed.FeedPostType
import com.rizwansayyed.zene.data.db.artistsfeed.youtubeToTimestamp
import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.onlinesongs.instagram.InstagramInfoService
import com.rizwansayyed.zene.data.onlinesongs.instagram.stories.SaveFromStoriesImplInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.bing.BingScrapsInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.facebook.FacebookScrapsImplInterface
import com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.youtubescrap.YoutubeScrapsImpl
import com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.YoutubeAPIImplInterface
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.presenter.util.UiUtils.toast
import com.rizwansayyed.zene.utils.Utils.toLongWithPlaceHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class SocialMediaScrapsImpl @Inject constructor(
    private val roomDb: RoomDBInterface,
    private val instagramService: InstagramInfoService,
    private val bingScrap: BingScrapsInterface,
    private val youtubeScrap: YoutubeScrapsImpl,
    private val youtubeAPI: YoutubeAPIImplInterface,
    private val remoteConfig: RemoteConfigInterface,
    private val saveFromStories: SaveFromStoriesImplInterface
) : SocialMediaScrapsImplInterface {
    override suspend fun getAllArtistsData(a: PinnedArtistsEntity) =
        CoroutineScope(Dispatchers.IO).launch {
            roomDb.deleteArtistsFeeds(a.name).first()

            suspend fun updateLatestSyncTime() {
                roomDb.artistsLastInfoSync(a.name).collect()
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val instagram = instagramService
                        .instagramInfo(remoteConfig.instagramAppID(), a.instagramUsername)

                    instagram.data?.user?.edge_owner_to_timeline_media?.edges?.forEach {
                        val media =
                            if (it?.node?.is_video == true) it.node.video_url else it?.node?.thumbnail_src
                        val caption =
                            it?.node?.edge_media_to_caption?.edges?.map { i -> i?.node?.text }
                                ?.joinToString(" ")

                        val v = ArtistsFeedEntity(
                            null, a.name, a.instagramUsername,
                            "${it?.node?.taken_at_timestamp}000".toLongWithPlaceHolder(),
                            FeedPostType.INSTAGRAM, media, it?.node?.is_video ?: false,
                            caption ?: "", null, it?.node?.shortcode ?: ""
                        )
                        roomDb.insert(v).collect()
                    }

                    updateLatestSyncTime()
                } catch (e: Exception) {
                    e.message
                }

                if (isActive) cancel()
            }

            CoroutineScope(Dispatchers.IO).launch {
                bingScrap.bingNews(a.name).catch { }.collectLatest {
                    it.forEach { news ->
                        val media = if (news.img == null) a.thumbnail
                        else if (news.img.isEmpty()) a.thumbnail
                        else if (!news.img.contains("th.bing.com")) "https://th.bing.com${news.img}"
                        else if (!news.img.contains("https://")) "https://${news.img}"
                        else news.img

                        val v = ArtistsFeedEntity(
                            null, a.name, a.name, news.timeStamp(), FeedPostType.NEWS,
                            media, false, news.title ?: "", news.desc, news.url
                        )
                        roomDb.insert(v).collect()
                    }

                    updateLatestSyncTime()
                }

                if (isActive) cancel()
            }


            CoroutineScope(Dispatchers.IO).launch {
                val id =
                    if (a.youtubeChannel.contains("channel")) a.youtubeChannel.substringAfter("channel/")
                    else try {
                        youtubeScrap.getChannelId(a.youtubeChannel).first()
                    } catch (e: Exception) {
                        ""
                    }

                youtubeAPI.channelVideo(id).catch {}.collectLatest {
                    it.forEach { v ->
                        val feed = ArtistsFeedEntity(
                            null, a.name, a.youtubeChannel, youtubeToTimestamp(v.username ?: ""),
                            FeedPostType.YOUTUBE, v.media, false, v.title, v.desc, v.postId
                        )
                        roomDb.insert(feed).collect()
                    }

                    updateLatestSyncTime()
                }
                if (isActive) cancel()
            }

//            try {
//                val stories = saveFromStories.storiesList(a.instagramUsername).first()
//                stories?.forEach { s ->
//                    val media = if (s?.video_versions != null)
//                        s.video_versions.maxBy { i -> i?.height ?: 0 }?.url
//                    else
//                        s?.image_versions2?.candidates?.maxBy { i -> i?.height ?: 0 }?.url
//
//                    val feed = ArtistsFeedEntity(
//                        null, a.name, a.instagramUsername,
//                        "${s?.taken_at}000".toLongWithPlaceHolder(), FeedPostType.INSTAGRAM_STORIES,
//                        media, s?.video_versions != null, "", "", a.instagramUsername
//                    )
//                    roomDb.insert(feed).collect()
//                }
//                updateLatestSyncTime()
//            } catch (e: Exception) {
//                e.message
//            }
        }
}