package com.rizwansayyed.zene.domain.yt

data class YoutubeMusicReleaseResponse(
    val contents: Contents?
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
            val gridRenderer: GridRenderer?
        ) {

            data class GridRenderer(
                val items: List<Item?>?,
                val trackingParams: String?
            ) {
                data class Item(
                    val musicTwoRowItemRenderer: MusicTwoRowItemRendererRelease?
                )
            }
        }
    }
}

data class MusicTwoRowItemRendererRelease(
    val aspectRatio: String?,
    val menu: Menu?,
    val thumbnailOverlay: ThumbnailOverlay?,
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
}