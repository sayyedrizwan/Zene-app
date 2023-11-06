package com.rizwansayyed.zene.domain.yt

data class YoutubeLatestSearchVideoResponse(
    val contents: Contents?,
    val estimatedResults: String?,
    val header: Header?,
    val responseContext: ResponseContext?,
    val targetId: String?,
    val topbar: Topbar?,
    val trackingParams: String?
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
                    val subMenu: SubMenu?,
                    val targetId: String?,
                    val trackingParams: String?
                ) {
                    data class Content(
                        val continuationItemRenderer: ContinuationItemRendererYtSearch?,
                        val itemSectionRenderer: ItemSectionRendererYTSearch?
                    ) {
                        data class ContinuationItemRendererYtSearch(
                            val continuationEndpoint: ContinuationEndpoint?,
                            val loggingDirectives: LoggingDirectives?,
                            val trigger: String?
                        ) {
                            data class ContinuationEndpoint(
                                val clickTrackingParams: String?,
                                val commandMetadata: CommandMetadata?,
                                val continuationCommand: ContinuationCommand?
                            ) {
                                data class CommandMetadata(
                                    val webCommandMetadata: WebCommandMetadata?
                                ) {
                                    data class WebCommandMetadata(
                                        val apiUrl: String?,
                                        val sendPost: Boolean?
                                    )
                                }

                                data class ContinuationCommand(
                                    val request: String?,
                                    val token: String?
                                )
                            }

                            data class LoggingDirectives(
                                val trackingParams: String?
                            )
                        }
                    }

                    data class SubMenu(
                        val searchSubMenuRenderer: SearchSubMenuRenderer?
                    ) {
                        data class SearchSubMenuRenderer(
                            val trackingParams: String?
                        )
                    }
                }
            }
        }
    }

    data class Header(
        val searchHeaderRenderer: SearchHeaderRenderer?
    ) {
        data class SearchHeaderRenderer(
            val searchFilterButton: SearchFilterButton?,
            val trackingParams: String?
        ) {
            data class SearchFilterButton(
                val buttonRenderer: ButtonRenderer?
            ) {
                data class ButtonRenderer(
                    val accessibilityData: AccessibilityData?,
                    val command: Command?,
                    val icon: Icon?,
                    val iconPosition: String?,
                    val isDisabled: Boolean?,
                    val size: String?,
                    val style: String?,
                    val text: Text?,
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

                    data class Command(
                        val clickTrackingParams: String?,
                        val openPopupAction: OpenPopupAction?
                    ) {
                        data class OpenPopupAction(
                            val popup: Popup?,
                            val popupType: String?
                        ) {
                            data class Popup(
                                val searchFilterOptionsDialogRenderer: SearchFilterOptionsDialogRenderer?
                            ) {
                                data class SearchFilterOptionsDialogRenderer(
                                    val groups: List<Group?>?,
                                    val title: Title?
                                ) {
                                    data class Group(
                                        val searchFilterGroupRenderer: SearchFilterGroupRenderer?
                                    )

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
        }
    }

    data class ResponseContext(
        val mainAppWebResponseContext: MainAppWebResponseContext?,
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
}


data class ReelShelfRendererYtSearch(
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
            val accessibility: Accessibility?,
            val badge: Badge?,
            val headline: Headline?,
            val inlinePlaybackEndpoint: InlinePlaybackEndpoint?,
            val loggingDirectives: LoggingDirectives?,
            val menu: Menu?,
            val navigationEndpoint: NavigationEndpoint?,
            val style: String?,
            val thumbnail: Thumbnail?,
            val trackingParams: String?,
            val videoId: String?,
            val videoType: String?,
            val viewCountText: ViewCountText?
        ) {
            data class Accessibility(
                val accessibilityData: AccessibilityData?
            ) {
                data class AccessibilityData(
                    val label: String?
                )
            }

            data class Badge(
                val textBadgeRenderer: TextBadgeRenderer?
            ) {
                data class TextBadgeRenderer(
                    val label: Label?
                ) {
                    data class Label(
                        val simpleText: String?
                    )
                }
            }

            data class Headline(
                val simpleText: String?
            )

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
                    val playerExtraUrlParams: List<PlayerExtraUrlParam?>?,
                    val playerParams: String?,
                    val videoId: String?,
                    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig?
                ) {
                    data class PlayerExtraUrlParam(
                        val key: String?,
                        val value: String?
                    )

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

            data class LoggingDirectives(
                val enableDisplayloggerExperiment: Boolean?,
                val trackingParams: String?,
                val visibility: Visibility?
            ) {
                data class Visibility(
                    val types: String?
                )
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

            data class NavigationEndpoint(
                val clickTrackingParams: String?,
                val commandMetadata: CommandMetadata?,
                val reelWatchEndpoint: ReelWatchEndpoint?
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

                data class ReelWatchEndpoint(
                    val loggingContext: LoggingContext?,
                    val overlay: Overlay?,
                    val params: String?,
                    val playerParams: String?,
                    val sequenceParams: String?,
                    val sequenceProvider: String?,
                    val thumbnail: Thumbnail?,
                    val ustreamerConfig: String?,
                    val videoId: String?
                ) {
                    data class LoggingContext(
                        val qoeLoggingContext: QoeLoggingContext?,
                        val vssLoggingContext: VssLoggingContext?
                    ) {
                        data class QoeLoggingContext(
                            val serializedContextData: String?
                        )

                        data class VssLoggingContext(
                            val serializedContextData: String?
                        )
                    }

                    data class Overlay(
                        val reelPlayerOverlayRenderer: ReelPlayerOverlayRenderer?
                    ) {
                        data class ReelPlayerOverlayRenderer(
                            val reelPlayerNavigationModel: String?,
                            val style: String?,
                            val trackingParams: String?
                        )
                    }

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
                }
            }

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

data class ItemSectionRendererYTSearch(
    val contents: List<Content?>?,
    val trackingParams: String?
) {
    data class Content(
        val reelShelfRenderer: ReelShelfRendererYtSearch?,
        val videoRenderer: VideoRenderer?
    ) {


        data class VideoRenderer(
            val badges: List<Badge?>?,
            val channelThumbnailSupportedRenderers: ChannelThumbnailSupportedRenderers?,
            val detailedMetadataSnippets: List<DetailedMetadataSnippet?>?,
            val inlinePlaybackEndpoint: InlinePlaybackEndpoint?,
            val lengthText: LengthText?,
            val longBylineText: LongBylineText?,
            val menu: Menu?,
            val navigationEndpoint: NavigationEndpoint?,
            val ownerBadges: List<OwnerBadge?>?,
            val ownerText: OwnerText?,
            val publishedTimeText: PublishedTimeText?,
            val richThumbnail: RichThumbnail?,
            val searchVideoResultEntityKey: String?,
            val shortBylineText: ShortBylineText?,
            val shortViewCountText: ShortViewCountText?,
            val showActionMenu: Boolean?,
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

            data class ChannelThumbnailSupportedRenderers(
                val channelThumbnailWithLinkRenderer: ChannelThumbnailWithLinkRenderer?
            ) {
                data class ChannelThumbnailWithLinkRenderer(
                    val accessibility: Accessibility?,
                    val navigationEndpoint: NavigationEndpoint?,
                    val thumbnail: Thumbnail?
                ) {
                    data class Accessibility(
                        val accessibilityData: AccessibilityData?
                    ) {
                        data class AccessibilityData(
                            val label: String?
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

            data class DetailedMetadataSnippet(
                val maxOneLine: Boolean?,
                val snippetHoverText: SnippetHoverText?,
                val snippetText: SnippetText?
            ) {
                data class SnippetHoverText(
                    val runs: List<Run?>?
                ) {
                    data class Run(
                        val text: String?
                    )
                }

                data class SnippetText(
                    val runs: List<Run?>?
                ) {
                    data class Run(
                        val bold: Boolean?,
                        val text: String?
                    )
                }
            }

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
                    val params: String?,
                    val playerExtraUrlParams: List<PlayerExtraUrlParam?>?,
                    val playerParams: String?,
                    val videoId: String?,
                    val watchEndpointSupportedOnesieConfig: WatchEndpointSupportedOnesieConfig?
                ) {
                    data class PlayerExtraUrlParam(
                        val key: String?,
                        val value: String?
                    )

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
                        val menuServiceItemRenderer: MenuServiceItemRenderer?
                    ) {
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
                    val params: String?,
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

            data class PublishedTimeText(
                val simpleText: String?
            )

            data class RichThumbnail(
                val movingThumbnailRenderer: MovingThumbnailRenderer?
            ) {
                data class MovingThumbnailRenderer(
                    val enableHoveredLogging: Boolean?,
                    val enableOverlay: Boolean?,
                    val movingThumbnailDetails: MovingThumbnailDetails?
                ) {
                    data class MovingThumbnailDetails(
                        val logAsMovingThumbnail: Boolean?,
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
                val thumbnailOverlayLoadingPreviewRenderer: ThumbnailOverlayLoadingPreviewRenderer?,
                val thumbnailOverlayNowPlayingRenderer: ThumbnailOverlayNowPlayingRenderer?,
                val thumbnailOverlayTimeStatusRenderer: ThumbnailOverlayTimeStatusRenderer?,
                val thumbnailOverlayToggleButtonRenderer: ThumbnailOverlayToggleButtonRenderer?
            ) {
                data class ThumbnailOverlayLoadingPreviewRenderer(
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

            data class ViewCountText(
                val simpleText: String?
            )
        }
    }
}

data class SearchFilterGroupRenderer(
    val filters: List<Filter?>?,
    val title: Title?,
    val trackingParams: String?
) {
    data class Filter(
        val searchFilterRenderer: SearchFilterRenderer?
    ) {
        data class SearchFilterRenderer(
            val label: Label?,
            val navigationEndpoint: NavigationEndpoint?,
            val status: String?,
            val tooltip: String?,
            val trackingParams: String?
        ) {
            data class Label(
                val simpleText: String?
            )

            data class NavigationEndpoint(
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
                    val params: String?,
                    val query: String?
                )
            }
        }
    }

    data class Title(
        val simpleText: String?
    )
}