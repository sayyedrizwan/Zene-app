package com.rizwansayyed.zene.domain.yt

import android.util.Log
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.artistsListToString

data class YoutubeMusicMainSearchResponse(
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
                                val musicCardShelfRenderer: MusicCardShelfRenderer?,
                                val musicShelfRenderer: MusicShelfRenderer?
                            ) {

                                data class MusicShelfRenderer(
                                    val bottomEndpoint: BottomEndpoint?,
                                    val bottomText: BottomText?,
                                    val contents: List<Content?>?,
                                    val shelfDivider: ShelfDivider?,
                                    val title: Title?,
                                    val trackingParams: String?
                                ) {
                                    fun getArtists(): String {
                                        val list = ArrayList<String>(10)
                                        contents?.first()?.musicResponsiveListItemRenderer?.flexColumns?.forEach { f ->
                                            f?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.forEach { r ->
                                                if (r?.navigationEndpoint?.browseEndpoint?.browseEndpointContextSupportedConfigs?.browseEndpointContextMusicConfig?.pageType == "MUSIC_PAGE_TYPE_ARTIST") {
                                                    r.text?.let {
                                                        if (!list.any { n ->
                                                                n.lowercase()
                                                                    .trim() == it.lowercase()
                                                                    .trim()
                                                            }) list.add(it)
                                                    }
                                                }
                                            }
                                        }

                                        if (list.isEmpty()) {
                                            contents?.first()?.musicResponsiveListItemRenderer?.flexColumns?.forEach { m ->
                                                if ((m?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.size
                                                        ?: 0) > 2
                                                ) {
                                                    m?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.forEach { txt ->
                                                        txt?.text?.let { a ->
                                                            if (a.lowercase().contains(" and ")) list.add(a)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        return artistsListToString(list)
                                    }

                                    fun getArtistsNoCheck(): String? {
                                        var name: String? = null
                                        contents?.forEachIndexed { index, content ->
                                            if (index == 0) content?.musicResponsiveListItemRenderer?.flexColumns?.forEachIndexed { ind, flex ->
                                                if (ind == 0) flex?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.forEachIndexed { index, content ->
                                                    if (index == 0) content?.text?.let {
                                                        name = content.text
                                                    }
                                                }
                                            }
                                        }

                                        return name
                                    }

                                    data class BottomEndpoint(
                                        val clickTrackingParams: String?,
                                        val searchEndpoint: SearchEndpoint?
                                    ) {
                                        data class SearchEndpoint(
                                            val params: String?,
                                            val query: String?
                                        )
                                    }

                                    data class BottomText(
                                        val runs: List<Run?>?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }

                                    data class Content(
                                        val musicResponsiveListItemRenderer: MusicResponsiveListItemRenderer?
                                    )

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
                            }

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
        val serviceTrackingParams: List<ServiceTrackingParam?>?,
        val visitorData: String?
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

data class Button(
    val buttonRenderer: ButtonRenderer?
) {
    data class ButtonRenderer(
        val accessibility: Accessibility?,
        val accessibilityData: AccessibilityData?,
        val command: Command?,
        val icon: Icon?,
        val isDisabled: Boolean?,
        val size: String?,
        val style: String?,
        val text: Text?,
        val trackingParams: String?
    ) {
        data class Accessibility(
            val label: String?
        )

        data class AccessibilityData(
            val accessibilityData: AccessibilityData?
        ) {
            data class AccessibilityData(
                val label: String?
            )
        }

        data class Command(
            val clickTrackingParams: String?,
            val modalEndpoint: ModalEndpoint?,
            val watchEndpoint: WatchEndpoint?
        ) {
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
        }

        data class Icon(
            val iconType: String?
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

data class Header(
    val musicCardShelfHeaderBasicRenderer: MusicCardShelfHeaderBasicRenderer?
) {
    data class MusicCardShelfHeaderBasicRenderer(
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
    ) {
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

data class MusicCardShelfRenderer(
    val buttons: List<Button?>?,
    val header: YoutubeMusicMainSearchResponse.Contents.TabbedSearchResultsRenderer.Tab.TabRenderer.Content.SectionListRenderer.Header?,
    val menu: Menu?,
    val onTap: OnTap?,
    val subtitle: Subtitle?,
    val thumbnail: Thumbnail?,
    val thumbnailOverlay: ThumbnailOverlay?,
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

                        data class ModalEndpoint(
                            val modal: Modal?
                        ) {
                            data class Modal(
                                val modalWithTitleAndButtonRenderer: ModalWithTitleAndButtonRenderer?
                            )
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
                                val videoId: String?
                            )
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
                    val toggledText: ToggledText?,
                    val trackingParams: String?
                ) {
                    data class DefaultIcon(
                        val iconType: String?
                    )

                    data class DefaultServiceEndpoint(
                        val clickTrackingParams: String?,
                        val modalEndpoint: ModalEndpoint?
                    ) {
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

    data class OnTap(
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

    data class Title(
        val runs: List<Run?>?
    ) {
        data class Run(
            val navigationEndpoint: NavigationEndpoint?,
            val text: String?
        ) {
            data class NavigationEndpoint(
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
        }
    }
}

data class MusicResponsiveListItemRenderer(
    val flexColumnDisplayStyle: String?,
    val flexColumns: List<FlexColumn?>?,
    val itemHeight: String?,
    val menu: Menu?,
    val navigationEndpoint: NavigationEndpoint?,
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
                        val browseEndpoint: BrowseEndpoint?,
                        val clickTrackingParams: String?,
                        val modalEndpoint: ModalEndpoint?,
                        val shareEntityEndpoint: ShareEntityEndpoint?,
                        val watchEndpoint: WatchEndpoint?,
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
                                val playlistId: String?,
                                val videoId: String?
                            )
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
                        val watchEndpoint: WatchEndpoint?,
                        val watchPlaylistEndpoint: WatchPlaylistEndpoint?
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