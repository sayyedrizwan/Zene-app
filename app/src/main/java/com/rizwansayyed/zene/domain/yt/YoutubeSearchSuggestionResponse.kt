package com.rizwansayyed.zene.domain.yt

data class YoutubeSearchSuggestionResponse(
    val contents: List<Content?>?,
    val responseContext: ResponseContext?,
    val trackingParams: String?
) {
    data class Content(
        val searchSuggestionsSectionRenderer: SearchSuggestionsSectionRenderer?
    ) {
        data class SearchSuggestionsSectionRenderer(
            val contents: List<Content?>?
        ) {
            data class Content(
                val musicResponsiveListItemRenderer: MusicResponsiveListItemRenderer?,
                val searchSuggestionRenderer: SearchSuggestionRenderer?
            ) {
                data class MusicResponsiveListItemRenderer(
                    val flexColumnDisplayStyle: String?,
                    val flexColumns: List<FlexColumn?>?,
                    val itemHeight: String?,
                    val menu: Menu?,
                    val navigationEndpoint: NavigationEndpoint?,
                    val thumbnail: Thumbnail?,
                    val trackingParams: String?
                ) {
                    data class FlexColumn(
                        val musicResponsiveListItemFlexColumnRenderer: MusicResponsiveListItemFlexColumnRendererSearch?
                    )

                    data class Menu(
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
                                val menuNavigationItemRenderer: MenuNavigationItemRendererMusic?,
                                val menuServiceItemRenderer: MenuServiceItemRendererSearch?,
                                val toggleMenuServiceItemRenderer: ToggleMenuServiceItemRendererMusic?
                            )
                        }
                    }

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

                    data class Thumbnail(
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
                }

                data class SearchSuggestionRenderer(
                    val icon: Icon?,
                    val navigationEndpoint: NavigationEndpoint?,
                    val suggestion: Suggestion?,
                    val trackingParams: String?
                ) {
                    data class Icon(
                        val iconType: String?
                    )

                    data class NavigationEndpoint(
                        val clickTrackingParams: String?,
                        val searchEndpoint: SearchEndpoint?
                    ) {
                        data class SearchEndpoint(
                            val query: String?
                        )
                    }

                    data class Suggestion(
                        val runs: List<Run?>?
                    ) {
                        data class Run(
                            val bold: Boolean?,
                            val text: String?
                        )
                    }
                }
            }
        }
    }

    data class ResponseContext(
        val serviceTrackingParams: List<ServiceTrackingParam?>?
    ) {
        data class ServiceTrackingParam(
            val params: List<Param?>?,
            val service: String?
        ) {
            data class Param(
                val key: String?,
                val value: String?
            )
        }
    }
}

data class MusicResponsiveListItemFlexColumnRendererSearch(
    val displayPriority: String?,
    val text: Text?
) {
    data class Text(
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
}

data class MenuNavigationItemRendererMusic(
    val icon: Icon?,
    val navigationEndpoint: NavigationEndpoint?,
    val text: Text?,
    val trackingParams: String?
) {
    data class Icon(
        val iconType: String?
    )

    data class NavigationEndpoint(
        val addToPlaylistEndpoint: AddToPlaylistEndpoint?,
        val browseEndpoint: BrowseEndpoint?,
        val clickTrackingParams: String?,
        val shareEntityEndpoint: ShareEntityEndpoint?,
        val watchPlaylistEndpoint: WatchPlaylistEndpoint?
    ) {
        data class AddToPlaylistEndpoint(
            val playlistId: String?
        )

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

        data class ShareEntityEndpoint(
            val serializedShareEntity: String?,
            val sharePanelType: String?
        )

        data class WatchPlaylistEndpoint(
            val params: String?,
            val playlistId: String?
        )
    }

    data class Text(
        val runs: List<Run?>?
    ) {
        data class Run(
            val text: String?
        )
    }
}

data class MenuServiceItemRendererSearch(
    val icon: Icon?,
    val serviceEndpoint: ServiceEndpoint?,
    val text: Text?,
    val trackingParams: String?
) {
    data class Icon(
        val iconType: String?
    )

    data class ServiceEndpoint(
        val clickTrackingParams: String?,
        val queueAddEndpoint: QueueAddEndpoint?
    ) {
        data class QueueAddEndpoint(
            val commands: List<Command?>?,
            val queueInsertPosition: String?,
            val queueTarget: QueueTarget?
        ) {
            data class Command(
                val addToToastAction: AddToToastAction?,
                val clickTrackingParams: String?
            ) {
                data class AddToToastAction(
                    val item: Item?
                ) {
                    data class Item(
                        val notificationTextRenderer: NotificationTextRenderer?
                    ) {
                        data class NotificationTextRenderer(
                            val successResponseText: SuccessResponseText?,
                            val trackingParams: String?
                        ) {
                            data class SuccessResponseText(
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

            data class QueueTarget(
                val onEmptyQueue: OnEmptyQueue?,
                val playlistId: String?
            ) {
                data class OnEmptyQueue(
                    val clickTrackingParams: String?,
                    val watchEndpoint: WatchEndpoint?
                ) {
                    data class WatchEndpoint(
                        val playlistId: String?
                    )
                }
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

data class ToggleMenuServiceItemRendererMusic(
    val defaultIcon: DefaultIcon?,
    val defaultServiceEndpoint: DefaultServiceEndpoint?,
    val defaultText: DefaultText?,
    val toggledIcon: ToggledIcon?,
    val toggledServiceEndpoint: ToggledServiceEndpoint?,
    val toggledText: ToggledText?,
    val trackingParams: String?
) {
    data class DefaultIcon(
        val iconType: String?
    )

    data class DefaultServiceEndpoint(
        val clickTrackingParams: String?,
        val likeEndpoint: LikeEndpoint?,
        val subscribeEndpoint: SubscribeEndpoint?
    ) {
        data class LikeEndpoint(
            val status: String?,
            val target: Target?
        ) {
            data class Target(
                val playlistId: String?
            )
        }

        data class SubscribeEndpoint(
            val channelIds: List<String?>?,
            val params: String?
        )
    }

    data class DefaultText(
        val runs: List<Run?>?
    ) {
        data class Run(
            val text: String?
        )
    }

    data class ToggledIcon(
        val iconType: String?
    )

    data class ToggledServiceEndpoint(
        val clickTrackingParams: String?,
        val likeEndpoint: LikeEndpoint?,
        val unsubscribeEndpoint: UnsubscribeEndpoint?
    ) {
        data class LikeEndpoint(
            val status: String?,
            val target: Target?
        ) {
            data class Target(
                val playlistId: String?
            )
        }

        data class UnsubscribeEndpoint(
            val channelIds: List<String?>?,
            val params: String?
        )
    }

    data class ToggledText(
        val runs: List<Run?>?
    ) {
        data class Run(
            val text: String?
        )
    }
}