package com.rizwansayyed.zene.domain.download

data class DownloadYTConvertor(
    val result: Result?
) {
    data class Result(
        val download_url: String?,
        val gid: String?,
        val quality: Int?,
        val status: Int?,
        val tid: String?,
        val vid: String?
    )
}