package com.rizwansayyed.zene.domain.download

data class DownloadYTConvertor(
    val result: Result?,
    val error: ErrorResult?
) {
    data class Result(
        val download_url: String?,
        val gid: String?,
        val quality: Int?,
        val status: Int?,
        val tid: String?,
        val vid: String?
    )

    data class ErrorResult(
        val code: Int?,
        val message: String?,
        val status: Int?
    )
}