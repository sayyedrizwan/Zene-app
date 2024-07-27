package com.rizwansayyed.zene.data.api.model

data class ImgBBResponse(
    val `data`: Data?,
    val status: Int?,
    val success: Boolean?
) {
    data class Data(
        val delete_url: String?,
        val display_url: String?,
        val expiration: Int?,
        val height: Int?,
        val id: String?,
        val image: Image?,
        val size: Int?,
        val thumb: Thumb?,
        val time: Int?,
        val title: String?,
        val url: String?,
        val url_viewer: String?,
        val width: Int?
    ) {
        data class Image(
            val extension: String?,
            val filename: String?,
            val mime: String?,
            val name: String?,
            val url: String?
        )

        data class Thumb(
            val extension: String?,
            val filename: String?,
            val mime: String?,
            val name: String?,
            val url: String?
        )
    }
}