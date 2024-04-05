package com.rizwansayyed.zene.di.onlinemodule

data class YoutubeMusicPlaylistAPIResponse(
    val contents: Contents?,
    val trackingParams: String?
) {
    data class Contents(
        val singleColumnBrowseResultsRenderer: SingleColumnBrowseResultsRenderer?
    ) {
        data class SingleColumnBrowseResultsRenderer(
            val tabs: List<Tab?>?
        ) {
            data class Tab(
                val tabRenderer: TabRenderer?
            ) {
                data class TabRenderer(
                    val content: Content?,
                    val selected: Boolean?,
                    val tabIdentifier: String?,
                    val title: String?,
                    val trackingParams: String?
                ) {
                    data class Content(
                        val sectionListRenderer: SectionListRenderer?
                    ) {
                        data class SectionListRenderer(
                            val contents: List<Content?>?,
                            val trackingParams: String?
                        ) {
                            data class Content(
                                val gridRenderer: GridRenderer?
                            )
                        }
                    }
                }
            }
        }
    }
}

data class GridRenderer(
    val itemSize: String?,
    val items: List<Item?>?,
    val trackingParams: String?
) {
    data class Item(
        val musicTwoRowItemRenderer: MusicTwoRowItemRenderer?
    ) {
        data class MusicTwoRowItemRenderer(
            val aspectRatio: String?,
            val thumbnailRenderer: ThumbnailRenderer?,
            val title: Title?,
            val trackingParams: String?
        ) {
            data class ThumbnailRenderer(
                val musicThumbnailRenderer: MusicThumbnailRenderer?
            ) {
                data class MusicThumbnailRenderer(
                    val thumbnail: Thumbnail?,
                    val thumbnailCrop: String?,
                    val thumbnailScale: String?,
                    val trackingParams: String?
                ) {
                    data class Thumbnail(
                        val thumbnails: List<Thumbnail?>?
                    ) {
                        data class Thumbnail(
                            val height: Int?,
                            val url: String?,
                            val width: Int?
                        )
                    }
                }
            }

            data class Title(
                val runs: List<Run?>?
            ) {
                data class Run(
                    val navigationEndpoint: NavigationEndpoint?,
                    val text: String?
                ) {
                    data class NavigationEndpoint(
                        val browseEndpoint: BrowseEndpoint?,
                        val clickTrackingParams: String?
                    ) {
                        data class BrowseEndpoint(
                            val browseId: String?
                        )
                    }
                }
            }
        }
    }
}