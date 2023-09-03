package com.rizwansayyed.zene.presenter.model

data class InstagramPostResponse(
    val instagram: ArrayList<ArtistsInstagramPostResponse>?,
    val response: ApiResponse
)

data class ArtistsInstagramPostResponse(
    val bio: String?,
    val profile: String?,
    val name: String?,
    val url: String?,
    val postCount: Long?,
    val username: String?,
    val followers: Long?,
    val profilePic: String?,
    val posts: List<InstagramPost>?,
)

data class InstagramPost(
    val postImage: String?,
    val isVideo: Boolean?,
    val postId: String?,
    val timestamp: Long?,
    val likeCount: Long?,
    val commentCount: Long?,
    val totalImages: Int?,
)

data class ArtistsTwitterInfoResponse(
    val banner: String?,
    val profilePic: String?,
    val username: String?,
    val name: String?,
    val description: String?,
    val followers: Int?,
    val created_at: String?,
    val location: String?,
    val url: String?,
)