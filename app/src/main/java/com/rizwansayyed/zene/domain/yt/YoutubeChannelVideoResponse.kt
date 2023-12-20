package com.rizwansayyed.zene.domain.yt

data class YoutubeChannelVideoResponse(
    val contents: Contents?,
    val trackingParams: String?
) {
    data class Contents(
        val twoColumnBrowseResultsRenderer: TwoColumnBrowseResultsRenderer?
    ) {
        data class TwoColumnBrowseResultsRenderer(
            val tabs: List<Tab?>?
        ) {
            data class Tab(
                val tabRenderer: TabRenderer?
            ) {
                data class TabRenderer(
                    val content: Content?,
                    val endpoint: Endpoint?,
                    val selected: Boolean?,
                    val title: String?,
                    val trackingParams: String?
                ) {
                    data class Content(
                        val richGridRenderer: RichGridRenderer?
                    ) {
                        data class RichGridRenderer(
                            val contents: List<Content?>?,
                            val style: String?,
                            val targetId: String?,
                            val trackingParams: String?
                        ) {
                            data class Content(
                                val richItemRenderer: RichItemRenderer?
                            ) {
                                data class RichItemRenderer(
                                    val content: Content?,
                                    val trackingParams: String?
                                ) {
                                    data class Content(
                                        val videoRenderer: VideoRenderer?
                                    ) {
                                        data class VideoRenderer(
                                            val descriptionSnippet: DescriptionSnippet?,
                                            val isWatched: Boolean?,
                                            val publishedTimeText: PublishedTimeText?,
                                            val showActionMenu: Boolean?,
                                            val thumbnail: Thumbnail?,
                                            val title: Title?,
                                            val trackingParams: String?,
                                            val videoId: String?,
                                            val viewCountText: ViewCountText?
                                        ) {
                                            data class DescriptionSnippet(
                                                val runs: List<Run?>?
                                            ) {
                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class PublishedTimeText(
                                                val simpleText: String?
                                            )

                                            data class Thumbnail(
                                                val thumbnails: List<Thumbnail?>?
                                            ) {
                                                data class Thumbnail(
                                                    val height: Int?,
                                                    val url: String?,
                                                    val width: Int?
                                                )
                                            }

                                            data class Title(
                                                val accessibility: Accessibility?,
                                                val runs: List<Run?>?
                                            ) {
                                                data class Accessibility(
                                                    val accessibilityData: AccessibilityData?
                                                ) {
                                                    data class AccessibilityData(
                                                        val label: String?
                                                    )
                                                }

                                                data class Run(
                                                    val text: String?
                                                )
                                            }

                                            data class ViewCountText(
                                                val simpleText: String?
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}