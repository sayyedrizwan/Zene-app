package com.rizwansayyed.zene.domain.yt

data class YoutubeMusicReleaseResponse(
    val contents: Contents?,
    val header: Header?,
    val maxAgeStoreSeconds: Int?,
    val responseContext: ResponseContext?,
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
                    val trackingParams: String?
                ) {
                    data class Content(
                        val sectionListRenderer: SectionListRendererRelease?
                    )
                }
            }
        }
    }

    data class SectionListRendererRelease(
        val contents: List<Content?>?,
        val trackingParams: String?
    ) {
        data class Content(
            val gridRenderer: GridRenderer?,
            val musicCarouselShelfRenderer: MusicCarouselShelfRenderer?,
            val musicPlaylistShelfRenderer: MusicCarouselShelfRenderer?
        ) {

            data class GridRenderer(
                val items: List<Item?>?,
                val trackingParams: String?
            ) {
                data class Item(
                    val musicTwoRowItemRenderer: MusicTwoRowItemRendererRelease?
                )
            }

            data class MusicCarouselShelfRenderer(
                val contents: List<Content?>?,
                val header: Header?,
                val itemSize: String?,
                val trackingParams: String?
            ) {
                data class Content(
                    val musicTwoRowItemRenderer: MusicTwoRowItemRenderer?
                ) {
                    data class MusicTwoRowItemRenderer(
                        val aspectRatio: String?,
                        val menu: Menu?,
                        val navigationEndpoint: NavigationEndpoint?,
                        val subtitle: Subtitle?,
                        val subtitleBadges: List<SubtitleBadge?>?,
                        val thumbnailOverlay: ThumbnailOverlay?,
                        val thumbnailRenderer: ThumbnailRenderer?,
                        val title: Title?,
                        val trackingParams: String?
                    ) {
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
                                    val menuNavigationItemRenderer: MenuNavigationItemRendererRelease?,
                                    val menuServiceItemRenderer: MenuServiceItemRendererRelease?,
                                    val toggleMenuServiceItemRenderer: ToggleMenuServiceItemRendererRelease?
                                )
                            }
                        }

                        data class NavigationEndpoint(
                            val browseEndpoint: BrowseEndpoint?,
                            val clickTrackingParams: String?,
                            val watchEndpoint: WatchEndpoint?
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

                            data class WatchEndpoint(
                                val videoId: String?,
                                val watchEndpointMusicSupportedConfigs: WatchEndpointMusicSupportedConfigs?
                            ) {
                                data class WatchEndpointMusicSupportedConfigs(
                                    val watchEndpointMusicConfig: WatchEndpointMusicConfig?
                                ) {
                                    data class WatchEndpointMusicConfig(
                                        val musicVideoType: String?
                                    )
                                }
                            }
                        }

                        data class Subtitle(
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

                        data class SubtitleBadge(
                            val musicInlineBadgeRenderer: MusicInlineBadgeRenderer?
                        ) {
                            data class MusicInlineBadgeRenderer(
                                val accessibilityData: AccessibilityData?,
                                val icon: Icon?,
                                val trackingParams: String?
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
                            }
                        }

                        data class ThumbnailOverlay(
                            val musicItemThumbnailOverlayRenderer: MusicItemThumbnailOverlayRendererRelease?
                        )

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
                }

                data class Header(
                    val musicCarouselShelfBasicHeaderRenderer: MusicCarouselShelfBasicHeaderRenderer?
                ) {
                    data class MusicCarouselShelfBasicHeaderRenderer(
                        val accessibilityData: AccessibilityData?,
                        val headerStyle: String?,
                        val moreContentButton: MoreContentButton?,
                        val title: Title?,
                        val trackingParams: String?
                    ) {
                        data class AccessibilityData(
                            val accessibilityData: AccessibilityData?
                        ) {
                            data class AccessibilityData(
                                val label: String?
                            )
                        }

                        data class MoreContentButton(
                            val buttonRenderer: ButtonRenderer?
                        ) {
                            data class ButtonRenderer(
                                val accessibilityData: AccessibilityData?,
                                val navigationEndpoint: NavigationEndpoint?,
                                val style: String?,
                                val text: Text?,
                                val trackingParams: String?
                            ) {
                                data class AccessibilityData(
                                    val accessibilityData: AccessibilityData?
                                ) {
                                    data class AccessibilityData(
                                        val label: String?
                                    )
                                }

                                data class NavigationEndpoint(
                                    val browseEndpoint: BrowseEndpoint?,
                                    val clickTrackingParams: String?
                                ) {
                                    data class BrowseEndpoint(
                                        val browseId: String?
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
        }
    }

    data class Header(
        val musicHeaderRenderer: MusicHeaderRenderer?
    ) {
        data class MusicHeaderRenderer(
            val title: Title?,
            val trackingParams: String?
        ) {
            data class Title(
                val runs: List<Run?>?
            ) {
                data class Run(
                    val text: String?
                )
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

data class MusicTwoRowItemRendererRelease(
    val aspectRatio: String?,
    val menu: Menu?,
    val navigationEndpoint: NavigationEndpoint?,
    val subtitle: Subtitle?,
    val thumbnailOverlay: ThumbnailOverlay?,
    val thumbnailRenderer: ThumbnailRenderer?,
    val title: Title?,
    val trackingParams: String?
) {
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
                val menuNavigationItemRenderer: MenuNavigationItemRenderer?,
                val menuServiceItemRenderer: MenuServiceItemRenderer?,
                val toggleMenuServiceItemRenderer: ToggleMenuServiceItemRenderer?
            ) {
                data class MenuNavigationItemRenderer(
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
                        val clickTrackingParams: String?,
                        val shareEntityEndpoint: ShareEntityEndpoint?,
                        val watchPlaylistEndpoint: WatchPlaylistEndpoint?
                    ) {
                        data class AddToPlaylistEndpoint(
                            val playlistId: String?
                        )

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

                data class MenuServiceItemRenderer(
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

                data class ToggleMenuServiceItemRenderer(
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
                        val likeEndpoint: LikeEndpoint?
                    ) {
                        data class LikeEndpoint(
                            val status: String?,
                            val target: Target?
                        ) {
                            data class Target(
                                val playlistId: String?
                            )
                        }
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
                        val likeEndpoint: LikeEndpoint?
                    ) {
                        data class LikeEndpoint(
                            val status: String?,
                            val target: Target?
                        ) {
                            data class Target(
                                val playlistId: String?
                            )
                        }
                    }

                    data class ToggledText(
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

    data class Subtitle(
        val runs: List<Run?>?
    ) {
        data class Run(
            val text: String?
        )
    }

    data class ThumbnailOverlay(
        val musicItemThumbnailOverlayRenderer: MusicItemThumbnailOverlayRenderer?
    ) {
        data class MusicItemThumbnailOverlayRenderer(
            val background: Background?,
            val content: Content?,
            val contentPosition: String?,
            val displayStyle: String?
        ) {
            data class Background(
                val verticalGradient: VerticalGradient?
            ) {
                data class VerticalGradient(
                    val gradientLayerColors: List<String?>?
                )
            }

            data class Content(
                val musicPlayButtonRenderer: MusicPlayButtonRenderer?
            ) {
                data class MusicPlayButtonRenderer(
                    val accessibilityPauseData: AccessibilityPauseData?,
                    val accessibilityPlayData: AccessibilityPlayData?,
                    val activeBackgroundColor: Long?,
                    val activeScaleFactor: Double?,
                    val backgroundColor: Long?,
                    val buttonSize: String?,
                    val iconColor: Long?,
                    val iconLoadingColor: Int?,
                    val loadingIndicatorColor: Long?,
                    val pauseIcon: PauseIcon?,
                    val playIcon: PlayIcon?,
                    val playNavigationEndpoint: PlayNavigationEndpoint?,
                    val playingIcon: PlayingIcon?,
                    val rippleTarget: String?,
                    val trackingParams: String?
                ) {
                    data class AccessibilityPauseData(
                        val accessibilityData: AccessibilityData?
                    ) {
                        data class AccessibilityData(
                            val label: String?
                        )
                    }

                    data class AccessibilityPlayData(
                        val accessibilityData: AccessibilityData?
                    ) {
                        data class AccessibilityData(
                            val label: String?
                        )
                    }

                    data class PauseIcon(
                        val iconType: String?
                    )

                    data class PlayIcon(
                        val iconType: String?
                    )

                    data class PlayNavigationEndpoint(
                        val clickTrackingParams: String?,
                        val watchPlaylistEndpoint: WatchPlaylistEndpoint?
                    ) {
                        data class WatchPlaylistEndpoint(
                            val params: String?,
                            val playlistId: String?
                        )
                    }

                    data class PlayingIcon(
                        val iconType: String?
                    )
                }
            }
        }
    }

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

data class MenuNavigationItemRendererRelease(
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
        val watchEndpoint: WatchEndpoint?,
        val watchPlaylistEndpoint: WatchPlaylistEndpoint?
    ) {
        data class AddToPlaylistEndpoint(
            val playlistId: String?,
            val videoId: String?
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

        data class WatchEndpoint(
            val loggingContext: LoggingContext?,
            val params: String?,
            val playlistId: String?,
            val videoId: String?,
            val watchEndpointMusicSupportedConfigs: WatchEndpointMusicSupportedConfigs?
        ) {
            data class LoggingContext(
                val vssLoggingContext: VssLoggingContext?
            ) {
                data class VssLoggingContext(
                    val serializedContextData: String?
                )
            }

            data class WatchEndpointMusicSupportedConfigs(
                val watchEndpointMusicConfig: WatchEndpointMusicConfig?
            ) {
                data class WatchEndpointMusicConfig(
                    val musicVideoType: String?
                )
            }
        }

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


data class MenuServiceItemRendererRelease(
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
        val playlistEditEndpoint: PlaylistEditEndpoint?,
        val queueAddEndpoint: QueueAddEndpoint?
    ) {
        data class PlaylistEditEndpoint(
            val actions: List<Action?>?,
            val params: String?,
            val playlistId: String?
        ) {
            data class Action(
                val action: String?,
                val addedVideoId: String?,
                val dedupeOption: String?
            )
        }

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
                val playlistId: String?,
                val videoId: String?
            ) {
                data class OnEmptyQueue(
                    val clickTrackingParams: String?,
                    val watchEndpoint: WatchEndpoint?
                ) {
                    data class WatchEndpoint(
                        val playlistId: String?,
                        val videoId: String?
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

data class ToggleMenuServiceItemRendererRelease(
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
        val likeEndpoint: LikeEndpoint?
    ) {
        data class LikeEndpoint(
            val status: String?,
            val target: Target?
        ) {
            data class Target(
                val playlistId: String?,
                val videoId: String?
            )
        }
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
        val likeEndpoint: LikeEndpoint?
    ) {
        data class LikeEndpoint(
            val status: String?,
            val target: Target?
        ) {
            data class Target(
                val playlistId: String?,
                val videoId: String?
            )
        }
    }

    data class ToggledText(
        val runs: List<Run?>?
    ) {
        data class Run(
            val text: String?
        )
    }
}

data class MusicItemThumbnailOverlayRendererRelease(
    val background: Background?,
    val content: Content?,
    val contentPosition: String?,
    val displayStyle: String?
) {
    data class Background(
        val verticalGradient: VerticalGradient?
    ) {
        data class VerticalGradient(
            val gradientLayerColors: List<String?>?
        )
    }

    data class Content(
        val musicPlayButtonRenderer: MusicPlayButtonRenderer?
    ) {
        data class MusicPlayButtonRenderer(
            val accessibilityPauseData: AccessibilityPauseData?,
            val accessibilityPlayData: AccessibilityPlayData?,
            val activeBackgroundColor: Long?,
            val activeScaleFactor: Double?,
            val backgroundColor: Long?,
            val buttonSize: String?,
            val iconColor: Long?,
            val iconLoadingColor: Int?,
            val loadingIndicatorColor: Long?,
            val pauseIcon: PauseIcon?,
            val playIcon: PlayIcon?,
            val playNavigationEndpoint: PlayNavigationEndpoint?,
            val playingIcon: PlayingIcon?,
            val rippleTarget: String?,
            val trackingParams: String?
        ) {
            data class AccessibilityPauseData(
                val accessibilityData: AccessibilityData?
            ) {
                data class AccessibilityData(
                    val label: String?
                )
            }

            data class AccessibilityPlayData(
                val accessibilityData: AccessibilityData?
            ) {
                data class AccessibilityData(
                    val label: String?
                )
            }

            data class PauseIcon(
                val iconType: String?
            )

            data class PlayIcon(
                val iconType: String?
            )

            data class PlayNavigationEndpoint(
                val clickTrackingParams: String?,
                val watchEndpoint: WatchEndpoint?,
                val watchPlaylistEndpoint: WatchPlaylistEndpoint?
            ) {
                data class WatchEndpoint(
                    val params: String?,
                    val videoId: String?,
                    val watchEndpointMusicSupportedConfigs: WatchEndpointMusicSupportedConfigs?
                ) {
                    data class WatchEndpointMusicSupportedConfigs(
                        val watchEndpointMusicConfig: WatchEndpointMusicConfig?
                    ) {
                        data class WatchEndpointMusicConfig(
                            val musicVideoType: String?
                        )
                    }
                }

                data class WatchPlaylistEndpoint(
                    val playlistId: String?
                )
            }

            data class PlayingIcon(
                val iconType: String?
            )
        }
    }
}