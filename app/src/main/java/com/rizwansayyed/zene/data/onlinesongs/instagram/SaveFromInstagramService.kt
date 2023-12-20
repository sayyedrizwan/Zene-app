package com.rizwansayyed.zene.data.onlinesongs.instagram


import com.rizwansayyed.zene.data.utils.SaveFromInstagram.SAVE_FROM_INSTAGRAM_INFO
import com.rizwansayyed.zene.data.utils.SaveFromInstagram.SAVE_FROM_INSTAGRAM_STORIES
import com.rizwansayyed.zene.domain.instagram.InstagramDataResponse
import com.rizwansayyed.zene.domain.instagram.SaveFromInstagramStoriesResponse
import com.rizwansayyed.zene.domain.instagram.SaveFromInstagramUserInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SaveFromInstagramService {

    @GET(SAVE_FROM_INSTAGRAM_INFO)
    suspend fun userInfoByUsername(@Path("username") username: String): SaveFromInstagramUserInfoResponse


    @GET(SAVE_FROM_INSTAGRAM_STORIES)
    suspend fun userStories(@Path("user_id") id: String?): SaveFromInstagramStoriesResponse

}
