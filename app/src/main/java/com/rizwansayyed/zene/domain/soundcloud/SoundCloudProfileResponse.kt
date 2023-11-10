package com.rizwansayyed.zene.domain.soundcloud

typealias SoundCloudProfileResponse = ArrayList<SoundCloudProfileResponseItem>

data class SoundCloudProfileResponseItem(
    val network: String?,
    val title: String?,
    val url: String?,
    val username: String?
)