package com.rizwansayyed.zene.domain.instagram

data class SaveFromInstagramStoriesResponse(
    val result: List<Result?>?
) {
    data class Result(
        val has_audio: Boolean?,
        val image_versions2: ImageVersions2?,
        val original_height: Int?,
        val original_width: Int?,
        val pk: String?,
        val taken_at: Int?,
        val video_versions: List<VideoVersion?>?
    ) {
        data class ImageVersions2(
            val candidates: List<Candidate?>?
        ) {
            data class Candidate(
                val height: Int?,
                val url: String?,
                val url_signature: UrlSignature?,
                val width: Int?
            ) {
                data class UrlSignature(
                    val expires: Int?,
                    val signature: String?
                )
            }
        }

        data class VideoVersion(
            val height: Int?,
            val type: Int?,
            val url: String?,
            val url_signature: UrlSignature?,
            val width: Int?
        ) {
            data class UrlSignature(
                val expires: Int?,
                val signature: String?
            )
        }
    }
}