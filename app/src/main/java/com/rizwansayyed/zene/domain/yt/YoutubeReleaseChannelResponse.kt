package com.rizwansayyed.zene.domain.yt

data class YoutubeReleaseChannelResponse(
    val contents: Contents?,
    val frameworkUpdates: FrameworkUpdates?,
    val header: Header?,
    val metadata: Metadata?,
    val microformat: Microformat?,
    val responseContext: ResponseContext?,
    val topbar: Topbar?,
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
                        val sectionListRenderer: SectionListRenderer?
                    ) {
                        data class SectionListRenderer(
                            val contents: List<Content?>?,
                            val disablePullToRefresh: Boolean?,
                            val targetId: String?,
                            val trackingParams: String?
                        ) {
                            data class Content(
                                val itemSectionRenderer: ItemSectionRenderer?
                            )
                        }
                    }

                    data class Endpoint(
                        val browseEndpoint: BrowseEndpoint?,
                        val clickTrackingParams: String?,
                        val commandMetadata: CommandMetadata?
                    ) {
                        data class BrowseEndpoint(
                            val browseId: String?,
                            val canonicalBaseUrl: String?,
                            val params: String?
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
        }
    }

    data class FrameworkUpdates(
        val entityBatchUpdate: EntityBatchUpdate?
    ) {
        data class EntityBatchUpdate(
            val mutations: List<Mutation?>?,
            val timestamp: Timestamp?
        ) {
            data class Mutation(
                val entityKey: String?,
                val payload: Payload?,
                val type: String?
            ) {
                data class Payload(
                    val subscriptionStateEntity: SubscriptionStateEntity?
                ) {
                    data class SubscriptionStateEntity(
                        val key: String?,
                        val subscribed: Boolean?
                    )
                }
            }

            data class Timestamp(
                val nanos: Int?,
                val seconds: String?
            )
        }
    }

    data class Header(
        val carouselHeaderRenderer: CarouselHeaderRenderer?
    )

    data class Metadata(
        val channelMetadataRenderer: ChannelMetadataRenderer?
    ) {
        data class ChannelMetadataRenderer(
            val androidAppindexingLink: String?,
            val androidDeepLink: String?,
            val availableCountryCodes: List<String?>?,
            val avatar: Avatar?,
            val channelUrl: String?,
            val description: String?,
            val externalId: String?,
            val iosAppindexingLink: String?,
            val isFamilySafe: Boolean?,
            val keywords: String?,
            val ownerUrls: List<String?>?,
            val rssUrl: String?,
            val title: String?,
            val vanityChannelUrl: String?
        ) {
            data class Avatar(
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


data class Microformat(
    val microformatDataRenderer: MicroformatDataRenderer?
) {
    data class MicroformatDataRenderer(
        val androidPackage: String?,
        val appName: String?,
        val availableCountries: List<String?>?,
        val description: String?,
        val familySafe: Boolean?,
        val iosAppArguments: String?,
        val iosAppStoreId: String?,
        val linkAlternates: List<LinkAlternate?>?,
        val noindex: Boolean?,
        val ogType: String?,
        val schemaDotOrgType: String?,
        val siteName: String?,
        val thumbnail: Thumbnail?,
        val title: String?,
        val twitterCardType: String?,
        val twitterSiteHandle: String?,
        val unlisted: Boolean?,
        val urlApplinksAndroid: String?,
        val urlApplinksIos: String?,
        val urlApplinksWeb: String?,
        val urlCanonical: String?,
        val urlTwitterAndroid: String?,
        val urlTwitterIos: String?
    ) {
        data class LinkAlternate(
            val hrefUrl: String?
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
    }
}

data class ResponseContext(
    val mainAppWebResponseContext: MainAppWebResponseContext?,
    val maxAgeSeconds: Int?,
    val serviceTrackingParams: List<ServiceTrackingParam?>?,
    val webResponseContextExtensionData: WebResponseContextExtensionData?
) {
    data class MainAppWebResponseContext(
        val loggedOut: Boolean?,
        val trackingParam: String?
    )

    data class ServiceTrackingParam(
        val params: List<Param?>?,
        val service: String?
    ) {
        data class Param(
            val key: String?,
            val value: String?
        )
    }

    data class WebResponseContextExtensionData(
        val hasDecorated: Boolean?,
        val ytConfigData: YtConfigData?
    ) {
        data class YtConfigData(
            val rootVisualElementType: Int?,
            val visitorData: String?
        )
    }
}

data class Topbar(
    val desktopTopbarRenderer: DesktopTopbarRenderer?
) {
    data class DesktopTopbarRenderer(
        val a11ySkipNavigationButton: A11ySkipNavigationButton?,
        val backButton: BackButton?,
        val countryCode: String?,
        val forwardButton: ForwardButton?,
        val hotkeyDialog: HotkeyDialog?,
        val logo: Logo?,
        val searchbox: Searchbox?,
        val topbarButtons: List<TopbarButton?>?,
        val trackingParams: String?,
        val voiceSearchButton: VoiceSearchButton?
    ) {
        data class A11ySkipNavigationButton(
            val buttonRenderer: ButtonRenderer?
        ) {
            data class ButtonRenderer(
                val command: Command?,
                val isDisabled: Boolean?,
                val size: String?,
                val style: String?,
                val text: Text?,
                val trackingParams: String?
            ) {
                data class Command(
                    val clickTrackingParams: String?,
                    val commandMetadata: CommandMetadata?,
                    val signalServiceEndpoint: SignalServiceEndpoint?
                ) {
                    data class CommandMetadata(
                        val webCommandMetadata: WebCommandMetadata?
                    ) {
                        data class WebCommandMetadata(
                            val sendPost: Boolean?
                        )
                    }

                    data class SignalServiceEndpoint(
                        val actions: List<Action?>?,
                        val signal: String?
                    ) {
                        data class Action(
                            val clickTrackingParams: String?,
                            val signalAction: SignalAction?
                        ) {
                            data class SignalAction(
                                val signal: String?
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

        data class BackButton(
            val buttonRenderer: ButtonRenderer?
        ) {
            data class ButtonRenderer(
                val command: Command?,
                val trackingParams: String?
            ) {
                data class Command(
                    val clickTrackingParams: String?,
                    val commandMetadata: CommandMetadata?,
                    val signalServiceEndpoint: SignalServiceEndpoint?
                ) {
                    data class CommandMetadata(
                        val webCommandMetadata: WebCommandMetadata?
                    ) {
                        data class WebCommandMetadata(
                            val sendPost: Boolean?
                        )
                    }

                    data class SignalServiceEndpoint(
                        val actions: List<Action?>?,
                        val signal: String?
                    ) {
                        data class Action(
                            val clickTrackingParams: String?,
                            val signalAction: SignalAction?
                        ) {
                            data class SignalAction(
                                val signal: String?
                            )
                        }
                    }
                }
            }
        }

        data class ForwardButton(
            val buttonRenderer: ButtonRenderer?
        ) {
            data class ButtonRenderer(
                val command: Command?,
                val trackingParams: String?
            ) {
                data class Command(
                    val clickTrackingParams: String?,
                    val commandMetadata: CommandMetadata?,
                    val signalServiceEndpoint: SignalServiceEndpoint?
                ) {
                    data class CommandMetadata(
                        val webCommandMetadata: WebCommandMetadata?
                    ) {
                        data class WebCommandMetadata(
                            val sendPost: Boolean?
                        )
                    }

                    data class SignalServiceEndpoint(
                        val actions: List<Action?>?,
                        val signal: String?
                    ) {
                        data class Action(
                            val clickTrackingParams: String?,
                            val signalAction: SignalAction?
                        ) {
                            data class SignalAction(
                                val signal: String?
                            )
                        }
                    }
                }
            }
        }

        data class HotkeyDialog(
            val hotkeyDialogRenderer: HotkeyDialogRenderer?
        ) {
            data class HotkeyDialogRenderer(
                val dismissButton: DismissButton?,
                val sections: List<Section?>?,
                val title: Title?,
                val trackingParams: String?
            ) {
                data class DismissButton(
                    val buttonRenderer: ButtonRenderer?
                ) {
                    data class ButtonRenderer(
                        val isDisabled: Boolean?,
                        val size: String?,
                        val style: String?,
                        val text: Text?,
                        val trackingParams: String?
                    ) {
                        data class Text(
                            val runs: List<Run?>?
                        ) {
                            data class Run(
                                val text: String?
                            )
                        }
                    }
                }

                data class Section(
                    val hotkeyDialogSectionRenderer: HotkeyDialogSectionRenderer?
                ) {
                    data class HotkeyDialogSectionRenderer(
                        val options: List<Option?>?,
                        val title: Title?
                    ) {
                        data class Option(
                            val hotkeyDialogSectionOptionRenderer: HotkeyDialogSectionOptionRenderer?
                        ) {
                            data class HotkeyDialogSectionOptionRenderer(
                                val hotkey: String?,
                                val hotkeyAccessibilityLabel: HotkeyAccessibilityLabel?,
                                val label: Label?
                            ) {
                                data class HotkeyAccessibilityLabel(
                                    val accessibilityData: AccessibilityData?
                                ) {
                                    data class AccessibilityData(
                                        val label: String?
                                    )
                                }

                                data class Label(
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
                                val text: String?
                            )
                        }
                    }
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

        data class Logo(
            val topbarLogoRenderer: TopbarLogoRenderer?
        ) {
            data class TopbarLogoRenderer(
                val endpoint: Endpoint?,
                val iconImage: IconImage?,
                val overrideEntityKey: String?,
                val tooltipText: TooltipText?,
                val trackingParams: String?
            ) {
                data class Endpoint(
                    val browseEndpoint: BrowseEndpoint?,
                    val clickTrackingParams: String?,
                    val commandMetadata: CommandMetadata?
                ) {
                    data class BrowseEndpoint(
                        val browseId: String?
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

                data class IconImage(
                    val iconType: String?
                )

                data class TooltipText(
                    val runs: List<Run?>?
                ) {
                    data class Run(
                        val text: String?
                    )
                }
            }
        }

        data class Searchbox(
            val fusionSearchboxRenderer: FusionSearchboxRenderer?
        ) {
            data class FusionSearchboxRenderer(
                val clearButton: ClearButton?,
                val config: Config?,
                val icon: Icon?,
                val placeholderText: PlaceholderText?,
                val searchEndpoint: SearchEndpoint?,
                val trackingParams: String?
            ) {
                data class ClearButton(
                    val buttonRenderer: ButtonRenderer?
                ) {
                    data class ButtonRenderer(
                        val accessibilityData: AccessibilityData?,
                        val icon: Icon?,
                        val isDisabled: Boolean?,
                        val size: String?,
                        val style: String?,
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

                data class Config(
                    val webSearchboxConfig: WebSearchboxConfig?
                ) {
                    data class WebSearchboxConfig(
                        val focusSearchbox: Boolean?,
                        val hasOnscreenKeyboard: Boolean?,
                        val requestDomain: String?,
                        val requestLanguage: String?
                    )
                }

                data class Icon(
                    val iconType: String?
                )

                data class PlaceholderText(
                    val runs: List<Run?>?
                ) {
                    data class Run(
                        val text: String?
                    )
                }

                data class SearchEndpoint(
                    val clickTrackingParams: String?,
                    val commandMetadata: CommandMetadata?,
                    val searchEndpoint: SearchEndpoint?
                ) {
                    data class CommandMetadata(
                        val webCommandMetadata: WebCommandMetadata?
                    ) {
                        data class WebCommandMetadata(
                            val rootVe: Int?,
                            val url: String?,
                            val webPageType: String?
                        )
                    }

                    data class SearchEndpoint(
                        val query: String?
                    )
                }
            }
        }

        data class TopbarButton(
            val buttonRenderer: ButtonRenderer?,
            val topbarMenuButtonRenderer: TopbarMenuButtonRenderer?
        ) {
            data class ButtonRenderer(
                val icon: Icon?,
                val navigationEndpoint: NavigationEndpoint?,
                val size: String?,
                val style: String?,
                val targetId: String?,
                val text: Text?,
                val trackingParams: String?
            ) {
                data class Icon(
                    val iconType: String?
                )

                data class NavigationEndpoint(
                    val clickTrackingParams: String?,
                    val commandMetadata: CommandMetadata?,
                    val signInEndpoint: SignInEndpoint?
                ) {
                    data class CommandMetadata(
                        val webCommandMetadata: WebCommandMetadata?
                    ) {
                        data class WebCommandMetadata(
                            val rootVe: Int?,
                            val url: String?,
                            val webPageType: String?
                        )
                    }

                    data class SignInEndpoint(
                        val idamTag: String?
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

            data class TopbarMenuButtonRenderer(
                val accessibility: Accessibility?,
                val icon: Icon?,
                val menuRequest: MenuRequest?,
                val style: String?,
                val tooltip: String?,
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

                data class MenuRequest(
                    val clickTrackingParams: String?,
                    val commandMetadata: CommandMetadata?,
                    val signalServiceEndpoint: SignalServiceEndpoint?
                ) {
                    data class CommandMetadata(
                        val webCommandMetadata: WebCommandMetadata?
                    ) {
                        data class WebCommandMetadata(
                            val apiUrl: String?,
                            val sendPost: Boolean?
                        )
                    }

                    data class SignalServiceEndpoint(
                        val actions: List<Action?>?,
                        val signal: String?
                    ) {
                        data class Action(
                            val clickTrackingParams: String?,
                            val openPopupAction: OpenPopupAction?
                        ) {
                            data class OpenPopupAction(
                                val beReused: Boolean?,
                                val popup: Popup?,
                                val popupType: String?
                            ) {
                                data class Popup(
                                    val multiPageMenuRenderer: MultiPageMenuRenderer?
                                ) {
                                    data class MultiPageMenuRenderer(
                                        val showLoadingSpinner: Boolean?,
                                        val style: String?,
                                        val trackingParams: String?
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        data class VoiceSearchButton(
            val buttonRenderer: ButtonRenderer?
        ) {
            data class ButtonRenderer(
                val accessibilityData: AccessibilityData?,
                val icon: Icon?,
                val isDisabled: Boolean?,
                val serviceEndpoint: ServiceEndpoint?,
                val size: String?,
                val style: String?,
                val tooltip: String?,
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

                data class ServiceEndpoint(
                    val clickTrackingParams: String?,
                    val commandMetadata: CommandMetadata?,
                    val signalServiceEndpoint: SignalServiceEndpoint?
                ) {
                    data class CommandMetadata(
                        val webCommandMetadata: WebCommandMetadata?
                    ) {
                        data class WebCommandMetadata(
                            val sendPost: Boolean?
                        )
                    }

                    data class SignalServiceEndpoint(
                        val actions: List<Action?>?,
                        val signal: String?
                    ) {
                        data class Action(
                            val clickTrackingParams: String?,
                            val openPopupAction: OpenPopupAction?
                        ) {
                            data class OpenPopupAction(
                                val popup: Popup?,
                                val popupType: String?
                            ) {
                                data class Popup(
                                    val voiceSearchDialogRenderer: VoiceSearchDialogRenderer?
                                ) {
                                    data class VoiceSearchDialogRenderer(
                                        val connectionErrorHeader: ConnectionErrorHeader?,
                                        val connectionErrorMicrophoneLabel: ConnectionErrorMicrophoneLabel?,
                                        val disabledHeader: DisabledHeader?,
                                        val disabledSubtext: DisabledSubtext?,
                                        val exampleQuery1: ExampleQuery1?,
                                        val exampleQuery2: ExampleQuery2?,
                                        val exitButton: ExitButton?,
                                        val loadingHeader: LoadingHeader?,
                                        val microphoneButtonAriaLabel: MicrophoneButtonAriaLabel?,
                                        val microphoneOffPromptHeader: MicrophoneOffPromptHeader?,
                                        val permissionsHeader: PermissionsHeader?,
                                        val permissionsSubtext: PermissionsSubtext?,
                                        val placeholderHeader: PlaceholderHeader?,
                                        val promptHeader: PromptHeader?,
                                        val promptMicrophoneLabel: PromptMicrophoneLabel?,
                                        val trackingParams: String?
                                    ) {
                                        data class ConnectionErrorHeader(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }

                                        data class ConnectionErrorMicrophoneLabel(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }

                                        data class DisabledHeader(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }

                                        data class DisabledSubtext(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }

                                        data class ExampleQuery1(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }

                                        data class ExampleQuery2(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }

                                        data class ExitButton(
                                            val buttonRenderer: ButtonRenderer?
                                        ) {
                                            data class ButtonRenderer(
                                                val accessibilityData: AccessibilityData?,
                                                val icon: Icon?,
                                                val isDisabled: Boolean?,
                                                val size: String?,
                                                val style: String?,
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

                                        data class LoadingHeader(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }

                                        data class MicrophoneButtonAriaLabel(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }

                                        data class MicrophoneOffPromptHeader(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }

                                        data class PermissionsHeader(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }

                                        data class PermissionsSubtext(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }

                                        data class PlaceholderHeader(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }

                                        data class PromptHeader(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }

                                        data class PromptMicrophoneLabel(
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

data class ItemSectionRenderer(
    val contents: List<Content?>?,
    val trackingParams: String?
) {
    data class Content(
        val shelfRenderer: ShelfRenderer?,
        val playlistVideoListRenderer: PlayListVideoRContent?
    ) {
        data class PlayListVideoRContent(
            val contents : List<PlaylistVideoRenderer?>?
        ) {
            data class PlaylistVideoRenderer(
                val playlistVideoRenderer: PlaylistVideoRendererTitle?
            )
        }
        data class PlaylistVideoRendererTitle(
            val title: PlaylistVideoRendererTitle?
        ) {
            data class PlaylistVideoRendererTitle(
                val runs: List<PlaylistVideoRendererTitleRun?>?
            ) {
                data class PlaylistVideoRendererTitleRun(
                    val text:String?
                )
            }
        }

        data class ShelfRenderer(
            val content: Content?,
            val endpoint: Endpoint?,
            val playAllButton: PlayAllButton?,
            val title: Title?,
            val trackingParams: String?
        ) {
            data class Content(
                val horizontalListRenderer: HorizontalListRenderer?
            ) {
                data class HorizontalListRenderer(
                    val items: List<Item?>?,
                    val nextButton: NextButton?,
                    val previousButton: PreviousButton?,
                    val trackingParams: String?,
                    val visibleItemCount: Int?
                ) {
                    data class Item(
                        val compactStationRenderer: CompactStationRenderer?,
                        val gridPlaylistRenderer: GridPlaylistRenderer?,
                        val gridVideoRenderer: GridVideoRenderer?
                    ) {
                        data class CompactStationRenderer(
                            val description: Description?,
                            val navigationEndpoint: NavigationEndpoint?,
                            val thumbnail: Thumbnail?,
                            val title: Title?,
                            val trackingParams: String?,
                            val videoCountText: VideoCountText?
                        ) {
                            data class Description(
                                val simpleText: String?
                            )

                            data class NavigationEndpoint(
                                val clickTrackingParams: String?,
                                val commandMetadata: CommandMetadata?,
                                val watchEndpoint: WatchEndpoint?
                            ) {
                                data class CommandMetadata(
                                    val webCommandMetadata: WebCommandMetadata?
                                ) {
                                    data class WebCommandMetadata(
                                        val rootVe: Int?,
                                        val url: String?,
                                        val webPageType: String?
                                    )
                                }

                                data class WatchEndpoint(
                                    val continuePlayback: Boolean?,
                                    val loggingContext: LoggingContext?,
                                    val params: String?,
                                    val playlistId: String?,
                                    val videoId: String?,
                                    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig?
                                ) {
                                    data class LoggingContext(
                                        val vssLoggingContext: VssLoggingContext?
                                    ) {
                                        data class VssLoggingContext(
                                            val serializedContextData: String?
                                        )
                                    }

                                    data class WatchEndpointSupportedOnesieConfig(
                                        val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig?
                                    ) {
                                        data class Html5PlaybackOnesieConfig(
                                            val commonConfig: CommonConfig?
                                        ) {
                                            data class CommonConfig(
                                                val url: String?
                                            )
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
                                val simpleText: String?
                            )

                            data class VideoCountText(
                                val runs: List<Run?>?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }
                        }

                        data class GridPlaylistRenderer(
                            val longBylineText: LongBylineText?,
                            val navigationEndpoint: NavigationEndpoint?,
                            val playlistId: String?,
                            val publishedTimeText: PublishedTimeText?,
                            val shortBylineText: ShortBylineText?,
                            val sidebarThumbnails: List<SidebarThumbnail?>?,
                            val thumbnail: Thumbnail?,
                            val thumbnailOverlays: List<ThumbnailOverlay?>?,
                            val thumbnailRenderer: ThumbnailRenderer?,
                            val thumbnailText: ThumbnailText?,
                            val title: Title?,
                            val trackingParams: String?,
                            val videoCountShortText: VideoCountShortText?,
                            val videoCountText: VideoCountText?,
                            val viewPlaylistText: ViewPlaylistText?
                        ) {
                            data class LongBylineText(
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

                            data class NavigationEndpoint(
                                val clickTrackingParams: String?,
                                val commandMetadata: CommandMetadata?,
                                val watchEndpoint: WatchEndpoint?
                            ) {
                                data class CommandMetadata(
                                    val webCommandMetadata: WebCommandMetadata?
                                ) {
                                    data class WebCommandMetadata(
                                        val rootVe: Int?,
                                        val url: String?,
                                        val webPageType: String?
                                    )
                                }

                                data class WatchEndpoint(
                                    val loggingContext: LoggingContext?,
                                    val params: String?,
                                    val playlistId: String?,
                                    val videoId: String?,
                                    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig?
                                ) {
                                    data class LoggingContext(
                                        val vssLoggingContext: VssLoggingContext?
                                    ) {
                                        data class VssLoggingContext(
                                            val serializedContextData: String?
                                        )
                                    }

                                    data class WatchEndpointSupportedOnesieConfig(
                                        val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig?
                                    ) {
                                        data class Html5PlaybackOnesieConfig(
                                            val commonConfig: CommonConfig?
                                        ) {
                                            data class CommonConfig(
                                                val url: String?
                                            )
                                        }
                                    }
                                }
                            }

                            data class PublishedTimeText(
                                val simpleText: String?
                            )

                            data class ShortBylineText(
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

                            data class SidebarThumbnail(
                                val thumbnails: List<Thumbnail?>?
                            ) {
                                data class Thumbnail(
                                    val height: Int?,
                                    val url: String?,
                                    val width: Int?
                                )
                            }

                            data class Thumbnail(
                                val darkColorPalette: DarkColorPalette?,
                                val sampledThumbnailColor: SampledThumbnailColor?,
                                val thumbnails: List<Thumbnail?>?,
                                val vibrantColorPalette: VibrantColorPalette?
                            ) {
                                data class DarkColorPalette(
                                    val iconDisabledColor: Int?,
                                    val iconInactiveColor: Int?,
                                    val section2Color: Int?
                                )

                                data class SampledThumbnailColor(
                                    val blue: Int?,
                                    val green: Int?,
                                    val red: Int?
                                )

                                data class Thumbnail(
                                    val height: Int?,
                                    val url: String?,
                                    val width: Int?
                                )

                                data class VibrantColorPalette(
                                    val iconInactiveColor: Int?
                                )
                            }

                            data class ThumbnailOverlay(
                                val thumbnailOverlayBottomPanelRenderer: ThumbnailOverlayBottomPanelRenderer?,
                                val thumbnailOverlayHoverTextRenderer: ThumbnailOverlayHoverTextRenderer?,
                                val thumbnailOverlayNowPlayingRenderer: ThumbnailOverlayNowPlayingRenderer?
                            ) {
                                data class ThumbnailOverlayBottomPanelRenderer(
                                    val icon: Icon?,
                                    val text: Text?
                                ) {
                                    data class Icon(
                                        val iconType: String?
                                    )

                                    data class Text(
                                        val simpleText: String?
                                    )
                                }

                                data class ThumbnailOverlayHoverTextRenderer(
                                    val icon: Icon?,
                                    val text: Text?
                                ) {
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

                                data class ThumbnailOverlayNowPlayingRenderer(
                                    val text: Text?
                                ) {
                                    data class Text(
                                        val runs: List<Run?>?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }
                                }
                            }

                            data class ThumbnailRenderer(
                                val playlistVideoThumbnailRenderer: PlaylistVideoThumbnailRenderer?
                            ) {
                                data class PlaylistVideoThumbnailRenderer(
                                    val thumbnail: Thumbnail?,
                                    val trackingParams: String?
                                ) {
                                    data class Thumbnail(
                                        val darkColorPalette: DarkColorPalette?,
                                        val sampledThumbnailColor: SampledThumbnailColor?,
                                        val thumbnails: List<Thumbnail?>?,
                                        val vibrantColorPalette: VibrantColorPalette?
                                    ) {
                                        data class DarkColorPalette(
                                            val iconDisabledColor: Int?,
                                            val iconInactiveColor: Int?,
                                            val section2Color: Int?
                                        )

                                        data class SampledThumbnailColor(
                                            val blue: Int?,
                                            val green: Int?,
                                            val red: Int?
                                        )

                                        data class Thumbnail(
                                            val height: Int?,
                                            val url: String?,
                                            val width: Int?
                                        )

                                        data class VibrantColorPalette(
                                            val iconInactiveColor: Int?
                                        )
                                    }
                                }
                            }

                            data class ThumbnailText(
                                val runs: List<Run?>?
                            ) {
                                data class Run(
                                    val bold: Boolean?,
                                    val text: String?
                                )
                            }

                            data class Title(
                                val runs: List<Run?>?
                            )

                            data class VideoCountShortText(
                                val simpleText: String?
                            )

                            data class VideoCountText(
                                val runs: List<Run?>?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class ViewPlaylistText(
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
                                            val browseId: String?
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
                        }

                        data class GridVideoRenderer(
                            val badges: List<Badge?>?,
                            val menu: Menu?,
                            val navigationEndpoint: NavigationEndpoint?,
                            val offlineability: Offlineability?,
                            val ownerBadges: List<OwnerBadge?>?,
                            val publishedTimeText: PublishedTimeText?,
                            val shortBylineText: ShortBylineText?,
                            val shortViewCountText: ShortViewCountText?,
                            val thumbnail: Thumbnail?,
                            val thumbnailOverlays: List<ThumbnailOverlay?>?,
                            val title: Title?,
                            val trackingParams: String?,
                            val videoId: String?,
                            val viewCountText: ViewCountText?
                        ) {
                            data class Badge(
                                val metadataBadgeRenderer: MetadataBadgeRenderer?
                            ) {
                                data class MetadataBadgeRenderer(
                                    val accessibilityData: AccessibilityData?,
                                    val label: String?,
                                    val style: String?,
                                    val trackingParams: String?
                                ) {
                                    data class AccessibilityData(
                                        val label: String?
                                    )
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
                                        val menuServiceItemDownloadRenderer: MenuServiceItemDownloadRenderer?,
                                        val menuServiceItemRenderer: MenuServiceItemRenderer?
                                    ) {
                                        data class MenuServiceItemDownloadRenderer(
                                            val serviceEndpoint: ServiceEndpoint?,
                                            val trackingParams: String?
                                        ) {
                                            data class ServiceEndpoint(
                                                val clickTrackingParams: String?,
                                                val offlineVideoEndpoint: OfflineVideoEndpoint?
                                            ) {
                                                data class OfflineVideoEndpoint(
                                                    val onAddCommand: OnAddCommand?,
                                                    val videoId: String?
                                                ) {
                                                    data class OnAddCommand(
                                                        val clickTrackingParams: String?,
                                                        val getDownloadActionCommand: GetDownloadActionCommand?
                                                    ) {
                                                        data class GetDownloadActionCommand(
                                                            val params: String?,
                                                            val videoId: String?
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            data class NavigationEndpoint(
                                val clickTrackingParams: String?,
                                val commandMetadata: CommandMetadata?,
                                val watchEndpoint: WatchEndpoint?
                            ) {
                                data class CommandMetadata(
                                    val webCommandMetadata: WebCommandMetadata?
                                ) {
                                    data class WebCommandMetadata(
                                        val rootVe: Int?,
                                        val url: String?,
                                        val webPageType: String?
                                    )
                                }

                                data class WatchEndpoint(
                                    val videoId: String?,
                                    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig?
                                ) {
                                    data class WatchEndpointSupportedOnesieConfig(
                                        val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig?
                                    ) {
                                        data class Html5PlaybackOnesieConfig(
                                            val commonConfig: CommonConfig?
                                        ) {
                                            data class CommonConfig(
                                                val url: String?
                                            )
                                        }
                                    }
                                }
                            }

                            data class Offlineability(
                                val offlineabilityRenderer: OfflineabilityRenderer?
                            ) {
                                data class OfflineabilityRenderer(
                                    val clickTrackingParams: String?,
                                    val formats: List<Format?>?,
                                    val infoRenderer: InfoRenderer?,
                                    val offlineable: Boolean?
                                ) {
                                    data class Format(
                                        val formatType: String?,
                                        val name: Name?
                                    ) {
                                        data class Name(
                                            val runs: List<Run?>?
                                        ) {
                                            data class Run(
                                                val text: String?
                                            )
                                        }
                                    }

                                    data class InfoRenderer(
                                        val dismissableDialogRenderer: DismissableDialogRenderer?
                                    ) {
                                        data class DismissableDialogRenderer(
                                            val dialogMessage: String?,
                                            val title: String?,
                                            val trackingParams: String?
                                        )
                                    }
                                }
                            }

                            data class OwnerBadge(
                                val metadataBadgeRenderer: MetadataBadgeRenderer?
                            ) {
                                data class MetadataBadgeRenderer(
                                    val accessibilityData: AccessibilityData?,
                                    val icon: Icon?,
                                    val style: String?,
                                    val tooltip: String?,
                                    val trackingParams: String?
                                ) {
                                    data class AccessibilityData(
                                        val label: String?
                                    )

                                    data class Icon(
                                        val iconType: String?
                                    )
                                }
                            }

                            data class PublishedTimeText(
                                val simpleText: String?
                            )

                            data class ShortBylineText(
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

                            data class ThumbnailOverlay(
                                val thumbnailOverlayNowPlayingRenderer: ThumbnailOverlayNowPlayingRenderer?,
                                val thumbnailOverlayTimeStatusRenderer: ThumbnailOverlayTimeStatusRenderer?,
                                val thumbnailOverlayToggleButtonRenderer: ThumbnailOverlayToggleButtonRenderer?
                            ) {
                                data class ThumbnailOverlayNowPlayingRenderer(
                                    val text: Text?
                                ) {
                                    data class Text(
                                        val runs: List<Run?>?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }
                                }

                                data class ThumbnailOverlayTimeStatusRenderer(
                                    val style: String?,
                                    val text: Text?
                                ) {
                                    data class Text(
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

                                data class ThumbnailOverlayToggleButtonRenderer(
                                    val isToggled: Boolean?,
                                    val toggledAccessibility: ToggledAccessibility?,
                                    val toggledIcon: ToggledIcon?,
                                    val toggledServiceEndpoint: ToggledServiceEndpoint?,
                                    val toggledTooltip: String?,
                                    val trackingParams: String?,
                                    val untoggledAccessibility: UntoggledAccessibility?,
                                    val untoggledIcon: UntoggledIcon?,
                                    val untoggledServiceEndpoint: UntoggledServiceEndpoint?,
                                    val untoggledTooltip: String?
                                ) {
                                    data class ToggledAccessibility(
                                        val accessibilityData: AccessibilityData?
                                    ) {
                                        data class AccessibilityData(
                                            val label: String?
                                        )
                                    }

                                    data class ToggledIcon(
                                        val iconType: String?
                                    )

                                    data class ToggledServiceEndpoint(
                                        val clickTrackingParams: String?,
                                        val commandMetadata: CommandMetadata?,
                                        val playlistEditEndpoint: PlaylistEditEndpoint?
                                    ) {
                                        data class CommandMetadata(
                                            val webCommandMetadata: WebCommandMetadata?
                                        ) {
                                            data class WebCommandMetadata(
                                                val apiUrl: String?,
                                                val sendPost: Boolean?
                                            )
                                        }

                                        data class PlaylistEditEndpoint(
                                            val actions: List<Action?>?,
                                            val playlistId: String?
                                        ) {
                                            data class Action(
                                                val action: String?,
                                                val removedVideoId: String?
                                            )
                                        }
                                    }

                                    data class UntoggledAccessibility(
                                        val accessibilityData: AccessibilityData?
                                    ) {
                                        data class AccessibilityData(
                                            val label: String?
                                        )
                                    }

                                    data class UntoggledIcon(
                                        val iconType: String?
                                    )

                                    data class UntoggledServiceEndpoint(
                                        val clickTrackingParams: String?,
                                        val commandMetadata: CommandMetadata?,
                                        val playlistEditEndpoint: PlaylistEditEndpoint?,
                                        val signalServiceEndpoint: SignalServiceEndpoint?
                                    ) {
                                        data class CommandMetadata(
                                            val webCommandMetadata: WebCommandMetadata?
                                        ) {
                                            data class WebCommandMetadata(
                                                val apiUrl: String?,
                                                val sendPost: Boolean?
                                            )
                                        }

                                        data class PlaylistEditEndpoint(
                                            val actions: List<Action?>?,
                                            val playlistId: String?
                                        ) {
                                            data class Action(
                                                val action: String?,
                                                val addedVideoId: String?
                                            )
                                        }
                                    }
                                }
                            }

                            data class Title(
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

                            data class ViewCountText(
                                val simpleText: String?
                            )
                        }
                    }

                    data class NextButton(
                        val buttonRenderer: ButtonRenderer?
                    ) {
                        data class ButtonRenderer(
                            val accessibility: Accessibility?,
                            val icon: Icon?,
                            val isDisabled: Boolean?,
                            val size: String?,
                            val style: String?,
                            val trackingParams: String?
                        ) {
                            data class Accessibility(
                                val label: String?
                            )

                            data class Icon(
                                val iconType: String?
                            )
                        }
                    }

                    data class PreviousButton(
                        val buttonRenderer: ButtonRenderer?
                    ) {
                        data class ButtonRenderer(
                            val accessibility: Accessibility?,
                            val icon: Icon?,
                            val isDisabled: Boolean?,
                            val size: String?,
                            val style: String?,
                            val trackingParams: String?
                        ) {
                            data class Accessibility(
                                val label: String?
                            )

                            data class Icon(
                                val iconType: String?
                            )
                        }
                    }
                }
            }

            data class Endpoint(
                val browseEndpoint: BrowseEndpoint?,
                val clickTrackingParams: String?,
                val commandMetadata: CommandMetadata?
            ) {
                data class BrowseEndpoint(
                    val browseId: String?,
                    val canonicalBaseUrl: String?,
                    val params: String?
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

            data class PlayAllButton(
                val buttonRenderer: ButtonRenderer?
            ) {
                data class ButtonRenderer(
                    val icon: Icon?,
                    val isDisabled: Boolean?,
                    val navigationEndpoint: NavigationEndpoint?,
                    val size: String?,
                    val style: String?,
                    val text: Text?,
                    val trackingParams: String?
                ) {
                    data class Icon(
                        val iconType: String?
                    )

                    data class NavigationEndpoint(
                        val clickTrackingParams: String?,
                        val commandMetadata: CommandMetadata?,
                        val watchEndpoint: WatchEndpoint?
                    ) {
                        data class CommandMetadata(
                            val webCommandMetadata: WebCommandMetadata?
                        ) {
                            data class WebCommandMetadata(
                                val rootVe: Int?,
                                val url: String?,
                                val webPageType: String?
                            )
                        }

                        data class WatchEndpoint(
                            val loggingContext: LoggingContext?,
                            val playlistId: String?,
                            val videoId: String?,
                            val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig?
                        ) {
                            data class LoggingContext(
                                val vssLoggingContext: VssLoggingContext?
                            ) {
                                data class VssLoggingContext(
                                    val serializedContextData: String?
                                )
                            }

                            data class WatchEndpointSupportedOnesieConfig(
                                val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig?
                            ) {
                                data class Html5PlaybackOnesieConfig(
                                    val commonConfig: CommonConfig?
                                ) {
                                    data class CommonConfig(
                                        val url: String?
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
                        val clickTrackingParams: String?,
                        val commandMetadata: CommandMetadata?
                    ) {
                        data class BrowseEndpoint(
                            val browseId: String?,
                            val canonicalBaseUrl: String?,
                            val params: String?
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
        }
    }
}

data class CarouselHeaderRenderer(
    val contents: List<Content?>?,
    val trackingParams: String?
) {
    data class Content(
        val carouselItemRenderer: CarouselItemRenderer?,
        val topicChannelDetailsRenderer: TopicChannelDetailsRenderer?
    ) {
        data class CarouselItemRenderer(
            val backgroundColor: Long?,
            val carouselItems: List<CarouselItem?>?,
            val layoutStyle: String?,
            val paginationThumbnails: List<PaginationThumbnail?>?,
            val paginatorAlignment: String?,
            val trackingParams: String?
        ) {
            data class CarouselItem(
                val defaultPromoPanelRenderer: DefaultPromoPanelRenderer?
            ) {
                data class DefaultPromoPanelRenderer(
                    val description: Description?,
                    val inlinePlaybackRenderer: InlinePlaybackRenderer?,
                    val largeFormFactorBackgroundThumbnail: LargeFormFactorBackgroundThumbnail?,
                    val metadataOrder: String?,
                    val minPanelDisplayDurationMs: Int?,
                    val minVideoPlayDurationMs: Int?,
                    val navigationEndpoint: NavigationEndpoint?,
                    val panelLayout: String?,
                    val scrimColorValues: List<Long?>?,
                    val scrimRotation: Int?,
                    val smallFormFactorBackgroundThumbnail: SmallFormFactorBackgroundThumbnail?,
                    val title: Title?,
                    val trackingParams: String?
                ) {
                    data class Description(
                        val runs: List<Run?>?
                    ) {
                        data class Run(
                            val text: String?
                        )
                    }

                    data class InlinePlaybackRenderer(
                        val inlinePlaybackRenderer: InlinePlaybackRenderer?
                    ) {
                        data class InlinePlaybackRenderer(
                            val inlinePlaybackEndpoint: InlinePlaybackEndpoint?,
                            val lengthText: LengthText?,
                            val navigationEndpoint: NavigationEndpoint?,
                            val thumbnail: Thumbnail?,
                            val trackingParams: String?,
                            val videoId: String?
                        ) {
                            data class InlinePlaybackEndpoint(
                                val clickTrackingParams: String?,
                                val commandMetadata: CommandMetadata?,
                                val watchEndpoint: WatchEndpoint?
                            ) {
                                data class CommandMetadata(
                                    val webCommandMetadata: WebCommandMetadata?
                                ) {
                                    data class WebCommandMetadata(
                                        val rootVe: Int?,
                                        val url: String?,
                                        val webPageType: String?
                                    )
                                }

                                data class WatchEndpoint(
                                    val playerParams: String?,
                                    val startTimeSeconds: Int?,
                                    val videoId: String?,
                                    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig?
                                ) {
                                    data class WatchEndpointSupportedOnesieConfig(
                                        val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig?
                                    ) {
                                        data class Html5PlaybackOnesieConfig(
                                            val commonConfig: CommonConfig?
                                        ) {
                                            data class CommonConfig(
                                                val url: String?
                                            )
                                        }
                                    }
                                }
                            }

                            data class LengthText(
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

                            data class NavigationEndpoint(
                                val clickTrackingParams: String?,
                                val commandMetadata: CommandMetadata?,
                                val watchEndpoint: WatchEndpoint?
                            ) {
                                data class CommandMetadata(
                                    val webCommandMetadata: WebCommandMetadata?
                                ) {
                                    data class WebCommandMetadata(
                                        val rootVe: Int?,
                                        val url: String?,
                                        val webPageType: String?
                                    )
                                }

                                data class WatchEndpoint(
                                    val playerParams: String?,
                                    val videoId: String?,
                                    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig?
                                ) {
                                    data class WatchEndpointSupportedOnesieConfig(
                                        val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig?
                                    ) {
                                        data class Html5PlaybackOnesieConfig(
                                            val commonConfig: CommonConfig?
                                        ) {
                                            data class CommonConfig(
                                                val url: String?
                                            )
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
                        }
                    }

                    data class LargeFormFactorBackgroundThumbnail(
                        val thumbnailLandscapePortraitRenderer: ThumbnailLandscapePortraitRenderer?
                    ) {
                        data class ThumbnailLandscapePortraitRenderer(
                            val landscape: Landscape?,
                            val portrait: Portrait?
                        ) {
                            data class Landscape(
                                val thumbnails: List<Thumbnail?>?
                            ) {
                                data class Thumbnail(
                                    val height: Int?,
                                    val url: String?,
                                    val width: Int?
                                )
                            }

                            data class Portrait(
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

                    data class NavigationEndpoint(
                        val clickTrackingParams: String?,
                        val commandMetadata: CommandMetadata?,
                        val watchEndpoint: WatchEndpoint?
                    ) {
                        data class CommandMetadata(
                            val webCommandMetadata: WebCommandMetadata?
                        ) {
                            data class WebCommandMetadata(
                                val rootVe: Int?,
                                val url: String?,
                                val webPageType: String?
                            )
                        }

                        data class WatchEndpoint(
                            val videoId: String?,
                            val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig?
                        ) {
                            data class WatchEndpointSupportedOnesieConfig(
                                val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig?
                            ) {
                                data class Html5PlaybackOnesieConfig(
                                    val commonConfig: CommonConfig?
                                ) {
                                    data class CommonConfig(
                                        val url: String?
                                    )
                                }
                            }
                        }
                    }

                    data class SmallFormFactorBackgroundThumbnail(
                        val thumbnailLandscapePortraitRenderer: ThumbnailLandscapePortraitRenderer?
                    ) {
                        data class ThumbnailLandscapePortraitRenderer(
                            val landscape: Landscape?,
                            val portrait: Portrait?
                        ) {
                            data class Landscape(
                                val thumbnails: List<Thumbnail?>?
                            ) {
                                data class Thumbnail(
                                    val height: Int?,
                                    val url: String?,
                                    val width: Int?
                                )
                            }

                            data class Portrait(
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
                }
            }

            data class PaginationThumbnail(
                val accessibility: Accessibility?,
                val thumbnails: List<Thumbnail?>?
            ) {
                data class Accessibility(
                    val accessibilityData: AccessibilityData?
                ) {
                    data class AccessibilityData(
                        val label: String?
                    )
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

data class SignalServiceEndpoint(
    val actions: List<Action?>?,
    val signal: String?
) {
    data class Action(
        val addToPlaylistCommand: AddToPlaylistCommand?,
        val clickTrackingParams: String?
    ) {
        data class AddToPlaylistCommand(
            val listType: String?,
            val onCreateListCommand: OnCreateListCommand?,
            val openMiniplayer: Boolean?,
            val videoId: String?,
            val videoIds: List<String?>?
        ) {
            data class OnCreateListCommand(
                val clickTrackingParams: String?,
                val commandMetadata: CommandMetadata?,
                val createPlaylistServiceEndpoint: CreatePlaylistServiceEndpoint?
            ) {
                data class CommandMetadata(
                    val webCommandMetadata: WebCommandMetadata?
                ) {
                    data class WebCommandMetadata(
                        val apiUrl: String?,
                        val sendPost: Boolean?
                    )
                }

                data class CreatePlaylistServiceEndpoint(
                    val params: String?,
                    val videoIds: List<String?>?
                )
            }
        }
    }
}

data class Run(
    val navigationEndpoint: NavigationEndpoint?,
    val text: String?
) {
    data class NavigationEndpoint(
        val clickTrackingParams: String?,
        val commandMetadata: CommandMetadata?,
        val watchEndpoint: WatchEndpoint?
    ) {
        data class CommandMetadata(
            val webCommandMetadata: WebCommandMetadata?
        ) {
            data class WebCommandMetadata(
                val rootVe: Int?,
                val url: String?,
                val webPageType: String?
            )
        }

        data class WatchEndpoint(
            val loggingContext: LoggingContext?,
            val params: String?,
            val playlistId: String?,
            val videoId: String?,
            val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig?
        ) {
            data class LoggingContext(
                val vssLoggingContext: VssLoggingContext?
            ) {
                data class VssLoggingContext(
                    val serializedContextData: String?
                )
            }

            data class WatchEndpointSupportedOnesieConfig(
                val html5PlaybackOnesieConfig: Html5PlaybackOnesieConfig?
            ) {
                data class Html5PlaybackOnesieConfig(
                    val commonConfig: CommonConfig?
                ) {
                    data class CommonConfig(
                        val url: String?
                    )
                }
            }
        }
    }
}

data class MenuServiceItemRenderer(
    val hasSeparator: Boolean?,
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
        val commandMetadata: CommandMetadata?,
        val shareEntityServiceEndpoint: ShareEntityServiceEndpoint?,
        val signalServiceEndpoint: SignalServiceEndpoint?
    ) {
        data class CommandMetadata(
            val webCommandMetadata: WebCommandMetadata?
        ) {
            data class WebCommandMetadata(
                val apiUrl: String?,
                val sendPost: Boolean?
            )
        }

        data class ShareEntityServiceEndpoint(
            val commands: List<Command?>?,
            val serializedShareEntity: String?
        ) {
            data class Command(
                val clickTrackingParams: String?,
                val openPopupAction: OpenPopupAction?
            ) {
                data class OpenPopupAction(
                    val beReused: Boolean?,
                    val popup: Popup?,
                    val popupType: String?
                ) {
                    data class Popup(
                        val unifiedSharePanelRenderer: UnifiedSharePanelRenderer?
                    ) {
                        data class UnifiedSharePanelRenderer(
                            val showLoadingSpinner: Boolean?,
                            val trackingParams: String?
                        )
                    }
                }
            }
        }

        data class SignalServiceEndpoint(
            val actions: List<Action?>?,
            val signal: String?
        ) {
            data class Action(
                val addToPlaylistCommand: AddToPlaylistCommand?,
                val clickTrackingParams: String?
            ) {
                data class AddToPlaylistCommand(
                    val listType: String?,
                    val onCreateListCommand: OnCreateListCommand?,
                    val openMiniplayer: Boolean?,
                    val videoId: String?,
                    val videoIds: List<String?>?
                ) {
                    data class OnCreateListCommand(
                        val clickTrackingParams: String?,
                        val commandMetadata: CommandMetadata?,
                        val createPlaylistServiceEndpoint: CreatePlaylistServiceEndpoint?
                    ) {
                        data class CommandMetadata(
                            val webCommandMetadata: WebCommandMetadata?
                        ) {
                            data class WebCommandMetadata(
                                val apiUrl: String?,
                                val sendPost: Boolean?
                            )
                        }

                        data class CreatePlaylistServiceEndpoint(
                            val params: String?,
                            val videoIds: List<String?>?
                        )
                    }
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

data class TopicChannelDetailsRenderer(
    val avatar: Avatar?,
    val navigationEndpoint: NavigationEndpoint?,
    val subscribeButton: SubscribeButton?,
    val subtitle: Subtitle?,
    val title: Title?,
    val trackingParams: String?
) {
    data class Avatar(
        val thumbnails: List<Thumbnail?>?
    ) {
        data class Thumbnail(
            val height: Int?,
            val url: String?,
            val width: Int?
        )
    }

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

    data class SubscribeButton(
        val subscribeButtonRenderer: SubscribeButtonRenderer?
    ) {
        data class SubscribeButtonRenderer(
            val buttonText: ButtonText?,
            val channelId: String?,
            val enabled: Boolean?,
            val onSubscribeEndpoints: List<OnSubscribeEndpoint?>?,
            val onUnsubscribeEndpoints: List<OnUnsubscribeEndpoint?>?,
            val showPreferences: Boolean?,
            val subscribeAccessibility: SubscribeAccessibility?,
            val subscribed: Boolean?,
            val subscribedButtonText: SubscribedButtonText?,
            val subscribedEntityKey: String?,
            val subscriberCountText: SubscriberCountText?,
            val trackingParams: String?,
            val type: String?,
            val unsubscribeAccessibility: UnsubscribeAccessibility?,
            val unsubscribeButtonText: UnsubscribeButtonText?,
            val unsubscribedButtonText: UnsubscribedButtonText?
        ) {
            data class ButtonText(
                val runs: List<Run?>?
            ) {
                data class Run(
                    val text: String?
                )
            }

            data class OnSubscribeEndpoint(
                val clickTrackingParams: String?,
                val commandMetadata: CommandMetadata?,
                val subscribeEndpoint: SubscribeEndpoint?
            ) {
                data class CommandMetadata(
                    val webCommandMetadata: WebCommandMetadata?
                ) {
                    data class WebCommandMetadata(
                        val apiUrl: String?,
                        val sendPost: Boolean?
                    )
                }

                data class SubscribeEndpoint(
                    val channelIds: List<String?>?,
                    val params: String?
                )
            }

            data class OnUnsubscribeEndpoint(
                val clickTrackingParams: String?,
                val commandMetadata: CommandMetadata?,
                val signalServiceEndpoint: SignalServiceEndpoint?
            ) {
                data class CommandMetadata(
                    val webCommandMetadata: WebCommandMetadata?
                ) {
                    data class WebCommandMetadata(
                        val sendPost: Boolean?
                    )
                }

                data class SignalServiceEndpoint(
                    val actions: List<Action?>?,
                    val signal: String?
                ) {
                    data class Action(
                        val clickTrackingParams: String?,
                        val openPopupAction: OpenPopupAction?
                    ) {
                        data class OpenPopupAction(
                            val popup: Popup?,
                            val popupType: String?
                        ) {
                            data class Popup(
                                val confirmDialogRenderer: ConfirmDialogRenderer?
                            ) {
                                data class ConfirmDialogRenderer(
                                    val cancelButton: CancelButton?,
                                    val confirmButton: ConfirmButton?,
                                    val dialogMessages: List<DialogMessage?>?,
                                    val primaryIsCancel: Boolean?,
                                    val trackingParams: String?
                                ) {
                                    data class CancelButton(
                                        val buttonRenderer: ButtonRenderer?
                                    ) {
                                        data class ButtonRenderer(
                                            val accessibility: Accessibility?,
                                            val isDisabled: Boolean?,
                                            val size: String?,
                                            val style: String?,
                                            val text: Text?,
                                            val trackingParams: String?
                                        ) {
                                            data class Accessibility(
                                                val label: String?
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

                                    data class ConfirmButton(
                                        val buttonRenderer: ButtonRenderer?
                                    ) {
                                        data class ButtonRenderer(
                                            val accessibility: Accessibility?,
                                            val isDisabled: Boolean?,
                                            val serviceEndpoint: ServiceEndpoint?,
                                            val size: String?,
                                            val style: String?,
                                            val text: Text?,
                                            val trackingParams: String?
                                        ) {
                                            data class Accessibility(
                                                val label: String?
                                            )

                                            data class ServiceEndpoint(
                                                val clickTrackingParams: String?,
                                                val commandMetadata: CommandMetadata?,
                                                val unsubscribeEndpoint: UnsubscribeEndpoint?
                                            ) {
                                                data class CommandMetadata(
                                                    val webCommandMetadata: WebCommandMetadata?
                                                ) {
                                                    data class WebCommandMetadata(
                                                        val apiUrl: String?,
                                                        val sendPost: Boolean?
                                                    )
                                                }

                                                data class UnsubscribeEndpoint(
                                                    val channelIds: List<String?>?,
                                                    val params: String?
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

                                    data class DialogMessage(
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

            data class SubscribeAccessibility(
                val accessibilityData: AccessibilityData?
            ) {
                data class AccessibilityData(
                    val label: String?
                )
            }

            data class SubscribedButtonText(
                val runs: List<Run?>?
            ) {
                data class Run(
                    val text: String?
                )
            }

            data class SubscriberCountText(
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

            data class UnsubscribeAccessibility(
                val accessibilityData: AccessibilityData?
            ) {
                data class AccessibilityData(
                    val label: String?
                )
            }

            data class UnsubscribeButtonText(
                val runs: List<Run?>?
            ) {
                data class Run(
                    val text: String?
                )
            }

            data class UnsubscribedButtonText(
                val runs: List<Run?>?
            ) {
                data class Run(
                    val text: String?
                )
            }
        }
    }

    data class Subtitle(
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

    data class Title(
        val simpleText: String?
    )
}