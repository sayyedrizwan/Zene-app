package com.rizwansayyed.zene.domain.yt

import com.rizwansayyed.zene.utils.Utils.artistsListToString

data class YoutubeMusicAllSongsResponse(
    val contents: Contents?,
    val responseContext: ResponseContext?,
    val trackingParams: String?
) {
    data class Contents(
        val tabbedSearchResultsRenderer: TabbedSearchResultsRenderer?
    ) {
        data class TabbedSearchResultsRenderer(
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
                            val header: Header?,
                            val trackingParams: String?
                        ) {
                            data class Content(
                                val musicShelfRenderer: MusicShelfRendererSongs?
                            )

                            data class Header(
                                val chipCloudRenderer: ChipCloudRenderer?
                            ) {
                                data class ChipCloudRenderer(
                                    val chips: List<Chip?>?,
                                    val collapsedRowCount: Int?,
                                    val horizontalScrollable: Boolean?,
                                    val trackingParams: String?
                                ) {
                                    data class Chip(
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
                                                val searchEndpoint: SearchEndpoint?
                                            ) {
                                                data class SearchEndpoint(
                                                    val params: String?,
                                                    val query: String?
                                                )
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
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    data class ResponseContext(
        val maxAgeSeconds: Int?,
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

data class MusicShelfRendererSongs(
    val contents: List<Content?>?,
    val continuations: List<Continuation?>?,
    val shelfDivider: ShelfDivider?,
    val title: Title?,
    val trackingParams: String?
) {
    data class Content(
        val musicResponsiveListItemRenderer: MusicResponsiveListItemRenderer?
    ) {
        fun getArtists(): String {
            val list = ArrayList<String>(10)
            musicResponsiveListItemRenderer?.flexColumns?.forEach { f ->
                f?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.forEach { r ->
                    if (r?.navigationEndpoint?.browseEndpoint?.browseEndpointContextSupportedConfigs?.browseEndpointContextMusicConfig?.pageType == "MUSIC_PAGE_TYPE_ARTIST") {
                        r.text?.let { list.add(it) }
                    }
                }
            }
            return artistsListToString(list)
        }

        data class MusicResponsiveListItemRenderer(
            val badges: List<Badge?>?,
            val flexColumnDisplayStyle: String?,
            val flexColumns: List<FlexColumn?>?,
            val itemHeight: String?,
            val menu: Menu?,
            val overlay: Overlay?,
            val playlistItemData: PlaylistItemData?,
            val thumbnail: Thumbnail?,
            val trackingParams: String?
        ) {

            fun names(): Pair<String, String> {
                var nameMain = ""
                var videoMain = ""
                flexColumns?.forEach {
                    it?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.forEach { name ->
                        if (name?.navigationEndpoint?.watchEndpoint?.watchEndpointMusicSupportedConfigs?.watchEndpointMusicConfig?.musicVideoType == "MUSIC_VIDEO_TYPE_ATV") {
                            if (nameMain.isEmpty())
                                nameMain = name.text ?: ""
                            if (videoMain.isEmpty())
                                videoMain =
                                    name.navigationEndpoint.watchEndpoint.videoId
                                        ?: ""
                        }
                    }
                }

                return Pair(nameMain, videoMain)
            }

            data class Badge(
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

            data class FlexColumn(
                val musicResponsiveListItemFlexColumnRenderer: MusicResponsiveListItemFlexColumnRenderer?
            ) {
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
            }

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
                                val browseEndpoint: BrowseEndpoint?,
                                val clickTrackingParams: String?,
                                val shareEntityEndpoint: ShareEntityEndpoint?,
                                val watchEndpoint: WatchEndpoint?
                            ) {
                                data class AddToPlaylistEndpoint(
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
                                val feedbackEndpoint: FeedbackEndpoint?,
                                val likeEndpoint: LikeEndpoint?
                            ) {
                                data class FeedbackEndpoint(
                                    val feedbackToken: String?
                                )

                                data class LikeEndpoint(
                                    val actions: List<Action?>?,
                                    val status: String?,
                                    val target: Target?
                                ) {
                                    data class Action(
                                        val clickTrackingParams: String?,
                                        val musicLibraryStatusUpdateCommand: MusicLibraryStatusUpdateCommand?
                                    ) {
                                        data class MusicLibraryStatusUpdateCommand(
                                            val addToLibraryFeedbackToken: String?,
                                            val libraryStatus: String?
                                        )
                                    }

                                    data class Target(
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
                                val feedbackEndpoint: FeedbackEndpoint?,
                                val likeEndpoint: LikeEndpoint?
                            ) {
                                data class FeedbackEndpoint(
                                    val feedbackToken: String?
                                )

                                data class LikeEndpoint(
                                    val status: String?,
                                    val target: Target?
                                ) {
                                    data class Target(
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
    }

    data class Continuation(
        val nextContinuationData: NextContinuationData?
    ) {
        data class NextContinuationData(
            val clickTrackingParams: String?,
            val continuation: String?
        )
    }

    data class ShelfDivider(
        val musicShelfDividerRenderer: MusicShelfDividerRenderer?
    ) {
        data class MusicShelfDividerRenderer(
            val hidden: Boolean?
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