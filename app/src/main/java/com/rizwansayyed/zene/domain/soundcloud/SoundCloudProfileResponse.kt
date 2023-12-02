package com.rizwansayyed.zene.domain.soundcloud

typealias SoundCloudProfileResponse = List<SoundCloudProfileResponseItem>

data class SoundCloudProfileResponseItem(
    val network: String?,
    val title: String?,
    val url: String?,
    val username: String?
)

data class SoundCloudProfileInfo(
    val social: SoundCloudProfileResponse,
    val followersCount: Int?,
)