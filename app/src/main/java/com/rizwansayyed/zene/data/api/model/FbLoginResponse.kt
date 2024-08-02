package com.rizwansayyed.zene.data.api.model

data class FbLoginResponse(
    private val email: String?,
    val id: String?,
    val name: String?,
    val picture: Picture?
) {
    data class Picture(
        val `data`: Data?
    ) {
        data class Data(
            val height: Int?,
            val is_silhouette: Boolean?,
            val url: String?,
            val width: Int?
        )
    }

    fun email(): String? {
        if (email == null) return null
        return email.replace("\\u0040", "")
    }

    fun profilePic(): String? {
        if (picture?.data?.url == null) return null
        return picture.data.url
    }
}