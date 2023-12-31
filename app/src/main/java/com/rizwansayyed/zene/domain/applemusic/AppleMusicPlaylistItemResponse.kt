package com.rizwansayyed.zene.domain.applemusic

data class AppleMusicPlaylistItemResponse(
    val `data`: List<AppleMusicPlaylistData?>?,
    val meta: Meta?
) {
    data class AppleMusicPlaylistData(
        val attributes: Attributes?,
        val href: String?,
        val id: String?,
        val type: String?
    ) {
        data class Attributes(
            val artwork: Artwork?,
            val canDelete: Boolean?,
            val canEdit: Boolean?,
            val dateAdded: String?,
            val hasCatalog: Boolean?,
            val hasCollaboration: Boolean?,
            val isPublic: Boolean?,
            val description: Description?,
            val lastModifiedDate: String?,
            val name: String?,
            val playParams: PlayParams?
        ) {
            data class Description(
                val standard: String
            )

            data class Artwork(
                val hasP3: Boolean?,
                val height: Any?,
                val url: String?,
                val width: Any?
            )

            data class PlayParams(
                val id: String?,
                val isLibrary: Boolean?,
                val kind: String?
            )
        }
    }

    data class Meta(
        val total: Int?
    )
}