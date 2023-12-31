package com.rizwansayyed.zene.domain.applemusic

data class AppleMusicPlaylistTracksResponse(
    val `data`: List<Data?>?
) {
    data class Data(
        val attributes: Attributes?,
        val href: String?,
        val id: String?,
        val relationships: Relationships?,
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
            val lastModifiedDate: String?,
            val name: String?,
            val playParams: PlayParams?
        ) {
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

        data class Relationships(
            val tracks: Tracks?
        ) {
            data class Tracks(
                val `data`: List<Data?>?,
                val href: String?,
                val meta: Meta?
            ) {
                data class Data(
                    val attributes: Attributes?,
                    val href: String?,
                    val id: String?,
                    val type: String?
                ) {
                    data class Attributes(
                        val albumName: String?,
                        val artistName: String?,
                        val artwork: Artwork?,
                        val discNumber: Int?,
                        val durationInMillis: Int?,
                        val genreNames: List<String?>?,
                        val hasCredits: Boolean?,
                        val hasLyrics: Boolean?,
                        val name: String?,
                        val playParams: PlayParams?,
                        val releaseDate: String?,
                        val trackNumber: Int?
                    ) {
                        data class Artwork(
                            val hasP3: Boolean?,
                            val height: Int?,
                            val url: String?,
                            val width: Int?
                        )

                        data class PlayParams(
                            val catalogId: String?,
                            val id: String?,
                            val isLibrary: Boolean?,
                            val kind: String?,
                            val reporting: Boolean?,
                            val reportingId: String?
                        )
                    }
                }

                data class Meta(
                    val total: Int?
                )
            }
        }
    }
}