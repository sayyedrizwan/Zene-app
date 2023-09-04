package com.rizwansayyed.zene.presenter.jsoup

import com.rizwansayyed.zene.presenter.jsoup.model.InstagramPostsResponse
import com.rizwansayyed.zene.presenter.model.ArtistsInstagramPostResponse
import com.rizwansayyed.zene.presenter.model.InstagramPost
import com.rizwansayyed.zene.utils.RemoteConfigReader
import com.rizwansayyed.zene.utils.Utils.URL.searchInstagramAPI
import com.rizwansayyed.zene.utils.Utils.URL.searchViaBingInstagram
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.downloadHTMLOkhttp
import com.rizwansayyed.zene.utils.downloadHeaderOkhttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class InstagramFilterManager(private val name: String) {

    private var appID: String = ""
    private var userUrl: String = ""
    private var postData: InstagramPostsResponse? = null
    private val config = RemoteConfigReader()

    init {
        readAppID()
    }

    private fun readAppID() = CoroutineScope(Dispatchers.IO).launch {
        appID = config.getInstagramKey()

        if (isActive) cancel()
    }

    fun getData(): ArtistsInstagramPostResponse? {
        getUsername()
        downloadData()
        if (postData == null) {
            return null
        }

        val bioUrl = try {
            postData?.data?.user?.bio_links?.get(0)?.url ?: ""
        } catch (e: Exception) {
            ""
        }

        val posts = ArrayList<InstagramPost>(15)
        postData?.data?.user?.edge_owner_to_timeline_media?.edges?.forEach {
            val p = InstagramPost(
                it?.node?.display_url,
                it?.node?.is_video,
                it?.node?.shortcode,
                it?.node?.taken_at_timestamp,
                it?.node?.edge_liked_by?.count,
                it?.node?.edge_media_to_comment?.count,
                it?.node?.edge_sidecar_to_children?.edges?.size
            )
            posts.add(p)
        }

        return ArtistsInstagramPostResponse(
            postData?.data?.user?.biography,
            userUrl,
            postData?.data?.user?.full_name,
            bioUrl,
            postData?.data?.user?.edge_owner_to_timeline_media?.count,
            postData?.data?.user?.username,
            postData?.data?.user?.edge_followed_by?.count,
            postData?.data?.user?.profile_pic_url_hd,
            posts
        )
    }

    private fun downloadData() {
        try {
            val instagramURL = userUrl.substringAfter("instagram.com").replace("/", "")
            val instagramPost =
                downloadHeaderOkhttp(searchInstagramAPI(instagramURL), Pair("x-ig-app-id", appID))
            postData = try {
                moshi.adapter(InstagramPostsResponse::class.java).fromJson(instagramPost!!)
            } catch (e: Exception) {
                null
            }

        } catch (e: Exception) {
            e.message
        }
    }

    private fun getUsername() {
        try {
            val response = downloadHTMLOkhttp(searchViaBingInstagram(name))
            val document = Jsoup.parse(response!!)

            document.select("ol#b_results").select("li.b_algo").forEach {
                val url = it.selectFirst("a.tilk")?.attr("href")
                if (url?.contains("https://www.instagram.com/") == true && userUrl.isEmpty()) {
                    userUrl = url
                }
            }
        } catch (e: Exception) {
            e.message
        }

    }
}