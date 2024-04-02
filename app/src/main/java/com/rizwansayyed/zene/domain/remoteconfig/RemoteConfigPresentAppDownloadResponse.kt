package com.rizwansayyed.zene.domain.remoteconfig

import com.rizwansayyed.zene.data.utils.moshi

data class RemoteConfigPresentAppDownloadResponse(
    val lists: List<Lists?>?,
    val text: String?
) {
    data class Lists(
        val img: String?,
        val link: String?
    )
}



fun RemoteConfigPresentAppDownloadResponse.toCache(): String? {
    return moshi.adapter(RemoteConfigPresentAppDownloadResponse::class.java).toJson(this)
}
