package com.rizwansayyed.zene.domain.yt

import com.rizwansayyed.zene.domain.MusicData


data class YoutubeLatestYearResponse(
    val contents: Contents?,
    val estimatedResults: String?
) {
    data class Contents(
        val twoColumnSearchResultsRenderer: TwoColumnSearchResultsRenderer?
    ) {
        data class TwoColumnSearchResultsRenderer(
            val primaryContents: PrimaryContents?
        ) {
            data class PrimaryContents(
                val sectionListRenderer: SectionListRenderer?
            ) {
                data class SectionListRenderer(
                    val contents: List<Content?>?,
                    val hideBottomSeparator: Boolean?,
                    val targetId: String?,
                    val trackingParams: String?
                ) {
                    data class Content(
                        val itemSectionRenderer: ItemSectionRenderer?
                    ) {
                        data class ItemSectionRenderer(
                            val contents: List<Content?>?,
                            val trackingParams: String?
                        ) {
                            data class Content(
                                val reelShelfRenderer: ReelShelfRendererLatestYoutube?,
                                val videoRenderer: VideoRendererLatestYoutube?
                            )
                        }
                    }

                }
            }
        }
    }
}


data class ReelShelfRendererLatestYoutube(
    val button: Button?,
    val icon: Icon?,
    val items: List<Item?>?,
    val title: Title?,
    val trackingParams: String?
) {
    data class Button(
        val menuRenderer: MenuRenderer?
    ) {
        data class MenuRenderer(
            val accessibility: Accessibility?,
            val items: List<Item?>?,
            val trackingParams: String?
        ) {
            data class Accessibility(
                val accessibilityData: AccessibilityData?
            ) {
                data class AccessibilityData(
                    val label: String?
                )
            }

            data class Item(
                val menuNavigationItemRenderer: MenuNavigationItemRenderer?
            ) {
                data class MenuNavigationItemRenderer(
                    val accessibility: Accessibility?,
                    val icon: Icon?,
                    val navigationEndpoint: NavigationEndpoint?,
                    val text: Text?,
                    val trackingParams: String?
                ) {
                    data class Accessibility(
                        val accessibilityData: AccessibilityData?
                    ) {
                        data class AccessibilityData(
                            val label: String?
                        )
                    }

                    data class Icon(
                        val iconType: String?
                    )

                    data class NavigationEndpoint(
                        val clickTrackingParams: String?,
                        val commandMetadata: CommandMetadata?,
                        val userFeedbackEndpoint: UserFeedbackEndpoint?
                    ) {
                        data class CommandMetadata(
                            val webCommandMetadata: WebCommandMetadata?
                        ) {
                            data class WebCommandMetadata(
                                val ignoreNavigation: Boolean?
                            )
                        }

                        data class UserFeedbackEndpoint(
                            val additionalDatas: List<AdditionalData?>?
                        ) {
                            data class AdditionalData(
                                val userFeedbackEndpointProductSpecificValueData: UserFeedbackEndpointProductSpecificValueData?
                            ) {
                                data class UserFeedbackEndpointProductSpecificValueData(
                                    val key: String?,
                                    val value: String?
                                )
                            }
                        }
                    }

                    data class Text(
                        val runs: List<Run?>?
                    ) {
                        data class Run(
                            val text: String?
                        )
                    }
                }
            }
        }
    }

    data class Icon(
        val iconType: String?
    )

    data class Item(
        val reelItemRenderer: ReelItemRenderer?
    ) {
        data class ReelItemRenderer(
            val headline: Headline?,
            val style: String?,
            val thumbnail: Thumbnail?,
            val trackingParams: String?,
            val videoId: String?,
            val videoType: String?,
            val viewCountText: ViewCountText?
        ) {

            fun thumbnailURL(): String? {
                return try {
                    thumbnail?.thumbnails?.maxByOrNull {
                        it?.width ?: 0
                    }?.url
                } catch (e: Exception) {
                    ""
                }
            }

            data class Headline(
                val simpleText: String?
            )


            data class Thumbnail(
                val isOriginalAspectRatio: Boolean?,
                val thumbnails: List<Thumbnail?>?
            ) {

                data class Thumbnail(
                    val height: Int?,
                    val url: String?,
                    val width: Int?
                )
            }

            data class ViewCountText(
                val accessibility: Accessibility?,
                val simpleText: String?
            ) {
                data class Accessibility(
                    val accessibilityData: AccessibilityData?
                ) {
                    data class AccessibilityData(
                        val label: String?
                    )
                }
            }
        }
    }

    data class Title(
        val simpleText: String?
    )
}


data class VideoRendererLatestYoutube(
    val isWatched: Boolean?,
    val ownerText: OwnerText?,
    val searchVideoResultEntityKey: String?,
    val shortViewCountText: ShortViewCountText?,
    val showActionMenu: Boolean?,
    val thumbnail: Thumbnail?,
    val title: Title?,
    val trackingParams: String?,
    val videoId: String?,
    val viewCountText: ViewCountText?
) {
    fun thumbnailURL(): String? {
        return try {
            thumbnail?.thumbnails?.maxByOrNull {
                it?.width ?: 0
            }?.url
        } catch (e: Exception) {
            ""
        }
    }

    data class OwnerText(
        val runs: List<Run?>?
    ) {
        data class Run(
            val navigationEndpoint: NavigationEndpoint?,
            val text: String?
        ) {
            data class NavigationEndpoint(
                val browseEndpoint: BrowseEndpoint?,
                val clickTrackingParams: String?,
                val commandMetadata: CommandMetadata?
            ) {
                data class BrowseEndpoint(
                    val browseId: String?,
                    val canonicalBaseUrl: String?
                )

                data class CommandMetadata(
                    val webCommandMetadata: WebCommandMetadata?
                ) {
                    data class WebCommandMetadata(
                        val apiUrl: String?,
                        val rootVe: Int?,
                        val url: String?,
                        val webPageType: String?
                    )
                }
            }
        }
    }

    data class ShortViewCountText(
        val accessibility: Accessibility?,
        val simpleText: String?
    ) {
        data class Accessibility(
            val accessibilityData: AccessibilityData?
        ) {
            data class AccessibilityData(
                val label: String?
            )
        }
    }

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
        val runs: List<Run?>?
    ) {
        data class Run(
            val text: String?
        )
    }

    data class ViewCountText(
        val simpleText: String?
    )
}