package com.rizwansayyed.zene.domain.yt

data class BrowserIdYTResponse(
    val contents: Contents?,
    val queueContextParams: String?,
    val trackingParams: String?
) {

    fun browseID(): String? {
        var id: String? = null
        contents?.singleColumnMusicWatchNextResultsRenderer?.tabbedRenderer?.watchNextTabbedResultsRenderer?.tabs?.forEach { t ->
            if (t?.tabRenderer?.endpoint?.browseEndpoint?.browseEndpointContextSupportedConfigs?.browseEndpointContextMusicConfig?.pageType == "MUSIC_PAGE_TYPE_TRACK_RELATED")
                id = t.tabRenderer.endpoint.browseEndpoint.browseId
        }

        return id
    }

    data class Contents(
        val singleColumnMusicWatchNextResultsRenderer: SingleColumnMusicWatchNextResultsRenderer?
    ) {
        data class SingleColumnMusicWatchNextResultsRenderer(
            val tabbedRenderer: TabbedRenderer?
        ) {
            data class TabbedRenderer(
                val watchNextTabbedResultsRenderer: WatchNextTabbedResultsRenderer?
            ) {
                data class WatchNextTabbedResultsRenderer(
                    val tabs: List<Tab?>?
                ) {
                    data class Tab(
                        val tabRenderer: TabRenderer?
                    ) {
                        data class TabRenderer(
                            val content: Content?,
                            val endpoint: Endpoint?,
                            val title: String?,
                            val trackingParams: String?
                        )
                    }
                }
            }
        }
    }
}


data class Content(
    val musicQueueRenderer: MusicQueueRenderer?
) {
    data class MusicQueueRenderer(
        val content: Content?,
        val hack: Boolean?,
        val header: Header?
    ) {
        data class Content(
            val playlistPanelRenderer: PlaylistPanelRenderer?
        ) {
            data class PlaylistPanelRenderer(
                val contents: List<Content?>?,
                val isInfinite: Boolean?,
                val playlistId: String?,
                val trackingParams: String?
            ) {
                data class Content(
                    val playlistPanelVideoRenderer: PlaylistPanelVideoRenderer?
                ) {
                    data class PlaylistPanelVideoRenderer(
                        val longBylineText: LongBylineText?,
                        val selected: Boolean?,
                        val thumbnail: Thumbnail?,
                        val title: Title?,
                        val trackingParams: String?,
                        val videoId: String?
                    ) {
                        fun thumbnailURL(): String? {
                            return try {
                                thumbnail?.thumbnails?.first()?.url
                                    ?.replace(
                                        "w60-h60",
                                        "w544-h544"
                                    )
                                    ?.replace(
                                        "w120-h120",
                                        "w544-h544"
                                    )
                            } catch (e: Exception) {
                                ""
                            }
                        }

                        data class LongBylineText(
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
                                        val browseEndpointContextSupportedConfigs: BrowseEndpointContextSupportedConfigs?,
                                        val browseId: String?
                                    ) {
                                        data class BrowseEndpointContextSupportedConfigs(
                                            val browseEndpointContextMusicConfig: BrowseEndpointContextMusicConfig?
                                        ) {
                                            data class BrowseEndpointContextMusicConfig(
                                                val pageType: String?
                                            )
                                        }
                                    }
                                }
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
                    }
                }
            }
        }

        data class Header(
            val musicQueueHeaderRenderer: MusicQueueHeaderRenderer?
        ) {
            data class MusicQueueHeaderRenderer(
                val buttons: List<Button?>?,
                val subtitle: Subtitle?,
                val title: Title?,
                val trackingParams: String?
            ) {
                data class Button(
                    val chipCloudChipRenderer: ChipCloudChipRenderer?
                ) {
                    data class ChipCloudChipRenderer(
                        val accessibilityData: AccessibilityData?,
                        val icon: Icon?,
                        val isSelected: Boolean?,
                        val navigationEndpoint: NavigationEndpoint?,
                        val style: Style?,
                        val text: Text?,
                        val trackingParams: String?,
                        val uniqueId: String?
                    ) {
                        data class AccessibilityData(
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
                            val saveQueueToPlaylistCommand: SaveQueueToPlaylistCommand?
                        ) {
                            class SaveQueueToPlaylistCommand
                        }

                        data class Style(
                            val styleType: String?
                        )

                        data class Text(
                            val runs: List<Run?>?
                        ) {
                            data class Run(
                                val text: String?
                            )
                        }
                    }
                }

                data class Subtitle(
                    val runs: List<Run?>?
                ) {
                    data class Run(
                        val text: String?
                    )
                }

                data class Title(
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

data class Endpoint(
    val browseEndpoint: BrowseEndpoint?,
    val clickTrackingParams: String?
) {
    data class BrowseEndpoint(
        val browseEndpointContextSupportedConfigs: BrowseEndpointContextSupportedConfigs?,
        val browseId: String?
    ) {
        data class BrowseEndpointContextSupportedConfigs(
            val browseEndpointContextMusicConfig: BrowseEndpointContextMusicConfig?
        ) {
            data class BrowseEndpointContextMusicConfig(
                val pageType: String?
            )
        }
    }
}