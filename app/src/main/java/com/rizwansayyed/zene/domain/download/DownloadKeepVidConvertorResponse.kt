package com.rizwansayyed.zene.domain.download

data class ConvertorKeepVidConvertorResponse(
    val success: String, val jobid: String
)

data class DownloadKeepVidConvertorResponse(
    val ready: Int?, val dlurl: String
)