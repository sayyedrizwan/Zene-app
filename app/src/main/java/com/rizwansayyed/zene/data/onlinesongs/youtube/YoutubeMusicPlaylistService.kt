package com.rizwansayyed.zene.data.onlinesongs.youtube

import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_BROWSE_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_NEXT_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_PLAYER_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_SEARCH_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_SEARCH_SUGGESTION_API
import com.rizwansayyed.zene.data.utils.YoutubeAPI.YT_SUGGESTIONS_API
import com.rizwansayyed.zene.di.onlinemodule.YoutubeMusicPlaylistAPIResponse
import com.rizwansayyed.zene.domain.yt.BrowserIdYTResponse
import com.rizwansayyed.zene.domain.yt.YoutubeChannelVideoResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicAllSongsResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicMainSearchResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicRelatedResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicReleaseResponse
import com.rizwansayyed.zene.domain.yt.YoutubeMusicSongDetailResponse
import com.rizwansayyed.zene.domain.yt.YoutubePlaylistAlbumsResponse
import com.rizwansayyed.zene.domain.yt.YoutubePlaylistItemsResponse
import com.rizwansayyed.zene.domain.yt.YoutubeSearchSuggestionResponse
import com.rizwansayyed.zene.domain.yt.YoutubeSearchTextSuggestionResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface YoutubeMusicPlaylistService {

    @POST(YT_BROWSE_API)
    suspend fun youtubePlaylistsResponse(
        @Header("authorization") authorization: String?,
        @Header("cookie") cookie: String?,
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubeMusicPlaylistAPIResponse

    @POST(YT_BROWSE_API)
    suspend fun youtubePlaylistsTracksResponse(
        @Header("authorization") authorization: String?,
        @Header("cookie") cookie: String?,
        @Body body: RequestBody,
        @Query("key") key: String,
        @Query("prettyPrint") prettyPrint: Boolean = false,
    ): YoutubePlaylistItemsResponse

}
