package com.rizwansayyed.zene.domain.yt

data class YoutubeMusicRelatedResponse(
    val contents: Contents?,
    val responseContext: ResponseContext?,
    val trackingParams: String?
) {
    data class Contents(
        val sectionListRenderer: SectionListRenderer?
    ) {
        data class SectionListRenderer(
            val contents: List<Content?>?,
            val trackingParams: String?
        ) {
            data class Content(
                val musicCarouselShelfRenderer: MusicCarouselShelfRenderer?,
                val musicDescriptionShelfRenderer: MusicDescriptionShelfRenderer?
            ) {

                data class MusicDescriptionShelfRenderer(
                    val description: Description?,
                    val header: Header?,
                    val moreButton: MoreButton?,
                    val shelfStyle: String?,
                    val trackingParams: String?
                ) {
                    data class Description(
                        val runs: List<Run?>?
                    ) {
                        data class Run(
                            val text: String?
                        )
                    }

                    data class Header(
                        val runs: List<Run?>?
                    ) {
                        data class Run(
                            val text: String?
                        )
                    }

                    data class MoreButton(
                        val toggleButtonRenderer: ToggleButtonRenderer?
                    ) {
                        data class ToggleButtonRenderer(
                            val defaultIcon: DefaultIcon?,
                            val defaultText: DefaultText?,
                            val isDisabled: Boolean?,
                            val isToggled: Boolean?,
                            val toggledIcon: ToggledIcon?,
                            val toggledText: ToggledText?,
                            val trackingParams: String?
                        ) {
                            data class DefaultIcon(
                                val iconType: String?
                            )

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

data class ModalEndpoint(
    val modal: Modal?
) {
    data class Modal(
        val modalWithTitleAndButtonRenderer: ModalWithTitleAndButtonRenderer?
    ) {
        data class ModalWithTitleAndButtonRenderer(
            val button: Button?,
            val content: Content?,
            val title: Title?
        ) {
            data class Button(
                val buttonRenderer: ButtonRenderer?
            ) {
                data class ButtonRenderer(
                    val isDisabled: Boolean?,
                    val navigationEndpoint: NavigationEndpoint?,
                    val style: String?,
                    val text: Text?,
                    val trackingParams: String?
                ) {
                    data class NavigationEndpoint(
                        val clickTrackingParams: String?,
                        val signInEndpoint: SignInEndpoint?
                    ) {
                        data class SignInEndpoint(
                            val hack: Boolean?
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

            data class Content(
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

data class MusicTwoRowItemRenderer(
    val aspectRatio: String?,
    val menu: Menu?,
    val navigationEndpoint: NavigationEndpoint?,
    val subtitle: Subtitle?,
    val thumbnailCornerOverlay: ThumbnailCornerOverlay?,
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
                        val browseEndpoint: BrowseEndpoint?,
                        val clickTrackingParams: String?,
                        val modalEndpoint: ModalEndpoint?,
                        val shareEntityEndpoint: ShareEntityEndpoint?,
                        val watchPlaylistEndpoint: WatchPlaylistEndpoint?
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
                        val modalEndpoint: ModalEndpoint?
                    )

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

    data class ThumbnailCornerOverlay(
        val musicThumbnailRenderer: MusicThumbnailRenderer?
    ) {
        data class MusicThumbnailRenderer(
            val thumbnail: Thumbnail?,
            val thumbnailCrop: String?,
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

data class MusicResponsiveListItemFlexColumnRenderer(
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
        }
    }
}

data class YTMusicResponsiveListItemRenderer(
    val flexColumnDisplayStyle: String?,
    val flexColumns: List<FlexColumn?>?,
    val itemHeight: String?,
    val menu: Menu?,
    val overlay: Overlay?,
    val playlistItemData: PlaylistItemData?,
    val thumbnail: Thumbnail?,
    val trackingParams: String?
) {

    data class FlexColumn(
        val musicResponsiveListItemFlexColumnRenderer: MusicResponsiveListItemFlexColumnRenderer?
    )

    data class Menu(
        val menuRenderer: MenuRenderer?
    ) {
        data class MenuRenderer(
            val accessibility: Accessibility?,
            val items: List<Item?>?,
            val topLevelButtons: List<TopLevelButton?>?,
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
                        val browseEndpoint: BrowseEndpoint?,
                        val clickTrackingParams: String?,
                        val modalEndpoint: ModalEndpoint?,
                        val shareEntityEndpoint: ShareEntityEndpoint?,
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
                                val videoId: String?
                            ) {
                                data class OnEmptyQueue(
                                    val clickTrackingParams: String?,
                                    val watchEndpoint: WatchEndpoint?
                                ) {
                                    data class WatchEndpoint(
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
                        val modalEndpoint: ModalEndpoint?
                    )

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
                        val feedbackEndpoint: FeedbackEndpoint?
                    ) {
                        data class FeedbackEndpoint(
                            val feedbackToken: String?
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
            }

            data class TopLevelButton(
                val likeButtonRenderer: LikeButtonRenderer?
            ) {
                data class LikeButtonRenderer(
                    val dislikeNavigationEndpoint: DislikeNavigationEndpoint?,
                    val likeCommand: LikeCommand?,
                    val likeStatus: String?,
                    val likesAllowed: Boolean?,
                    val target: Target?,
                    val trackingParams: String?
                ) {
                    data class DislikeNavigationEndpoint(
                        val clickTrackingParams: String?,
                        val modalEndpoint: ModalEndpoint?
                    )

                    data class LikeCommand(
                        val clickTrackingParams: String?,
                        val modalEndpoint: ModalEndpoint?
                    )
                    data class Target(
                        val videoId: String?
                    )
                }
            }
        }
    }

    data class Overlay(
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
                    val activeBackgroundColor: Int?,
                    val activeScaleFactor: Int?,
                    val backgroundColor: Int?,
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
                        val watchEndpoint: WatchEndpoint?
                    ) {
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

                    data class PlayingIcon(
                        val iconType: String?
                    )
                }
            }
        }
    }

    data class PlaylistItemData(
        val videoId: String?
    )

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

                fun thumbnailURL(): String? {
                    return try {
                        thumbnails?.first()?.url
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

                data class Thumbnail(
                    val height: Int?,
                    val url: String?,
                    val width: Int?
                )
            }
        }
    }
}

data class MusicCarouselShelfRenderer(
    val contents: List<Content?>?,
    val fullbleedStyle: String?,
    val header: Header?,
    val itemSize: String?,
    val numItemsPerColumn: String?,
    val trackingParams: String?
) {
    data class Content(
        val musicResponsiveListItemRenderer: YTMusicResponsiveListItemRenderer?,
        val musicTwoRowItemRenderer: MusicTwoRowItemRenderer?
    )

    data class Header(
        val musicCarouselShelfBasicHeaderRenderer: MusicCarouselShelfBasicHeaderRenderer?
    ) {
        data class MusicCarouselShelfBasicHeaderRenderer(
            val accessibilityData: AccessibilityData?,
            val headerStyle: String?,
            val strapline: Strapline?,
            val thumbnail: Thumbnail?,
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

            data class Strapline(
                val runs: List<Run?>?
            ) {
                data class Run(
                    val text: String?
                )
            }

            data class Thumbnail(
                val musicThumbnailRenderer: MusicThumbnailRenderer?
            ) {
                data class MusicThumbnailRenderer(
                    val accessibilityData: AccessibilityData?,
                    val onTap: OnTap?,
                    val thumbnail: Thumbnail?,
                    val thumbnailCrop: String?,
                    val thumbnailScale: String?,
                    val trackingParams: String?
                ) {
                    data class AccessibilityData(
                        val accessibilityData: AccessibilityData?
                    ) {
                        data class AccessibilityData(
                            val label: String?
                        )
                    }

                    data class OnTap(
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
}