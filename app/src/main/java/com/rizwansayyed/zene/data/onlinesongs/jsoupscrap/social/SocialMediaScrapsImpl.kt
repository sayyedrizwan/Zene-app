package com.rizwansayyed.zene.data.onlinesongs.jsoupscrap.social


import com.rizwansayyed.zene.data.db.artistsfeed.ArtistsFeedEntity
import com.rizwansayyed.zene.data.db.artistsfeed.FeedPostType
import com.rizwansayyed.zene.data.db.artistspin.PinnedArtistsEntity
import com.rizwansayyed.zene.data.db.impl.RoomDBInterface
import com.rizwansayyed.zene.data.onlinesongs.instagram.InstagramInfoService
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.utils.Utils.toLongWithPlaceHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class SocialMediaScrapsImpl @Inject constructor(
    private val roomDb: RoomDBInterface,
    private val instagramService: InstagramInfoService,
    private val remoteConfig: RemoteConfigInterface
) : SocialMediaScrapsImplInterface {
    override suspend fun getAllArtistsData(a: PinnedArtistsEntity) = job.launch {
        roomDb.deleteArtistsFeeds(a.name).first()

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
                        caption ?: "", "", it?.node?.shortcode ?: ""
                    )
                    roomDb.insert(v).collect()
                }
            } catch (e: Exception) {
                e.message
            }
        }

    }

}