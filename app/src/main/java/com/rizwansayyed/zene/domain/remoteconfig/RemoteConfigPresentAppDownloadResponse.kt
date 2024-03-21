package com.rizwansayyed.zene.domain.remoteconfig

data class RemoteConfigPresentAppDownloadResponse(
    val lists: List<Lists?>?,
    val text: String?
) {
    data class Lists(
        val img: String?,
        val link: String?
    )
}