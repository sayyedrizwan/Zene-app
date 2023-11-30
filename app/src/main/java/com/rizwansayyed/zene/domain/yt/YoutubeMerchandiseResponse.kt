package com.rizwansayyed.zene.domain.yt

data class YoutubeMerchandiseResponse(
    val contents: Contents?,
    val currentVideoEndpoint: CurrentVideoEndpoint?,
    val engagementPanels: List<EngagementPanel?>?,
    val frameworkUpdates: FrameworkUpdates?,
    val pageVisualEffects: List<PageVisualEffect?>?,
    val playerOverlays: PlayerOverlays?,
    val responseContext: ResponseContext?,
    val topbar: Topbar?,
    val trackingParams: String?
) {
    data class Contents(
        val twoColumnWatchNextResults: TwoColumnWatchNextResults?
    )
    data class CurrentVideoEndpoint(
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

    data class EngagementPanel(
        val engagementPanelSectionListRenderer: EngagementPanelSectionListRenderer?
    ) {
        data class EngagementPanelSectionListRenderer(
            val content: Content?,
            val header: Header?,
            val loggingDirectives: LoggingDirectives?,
            val panelIdentifier: String?,
            val targetId: String?,
            val veType: Int?,
            val visibility: String?
        ) {
            data class Content(
                val adsEngagementPanelContentRenderer: AdsEngagementPanelContentRenderer?,
                val productListRenderer: ProductListRenderer?,
                val sectionListRenderer: SectionListRenderer?,
                val structuredDescriptionContentRenderer: StructuredDescriptionContentRenderer?
            ) {
                data class AdsEngagementPanelContentRenderer(
                    val hack: Boolean?
                )

                data class ProductListRenderer(
                    val contents: List<Content?>?,
                    val trackingParams: String?
                ) {
                    data class Content(
                        val productListHeaderRenderer: ProductListHeaderRenderer?,
                        val productListItemRenderer: ProductListItemRenderer?
                    ) {

                        data class ProductListItemRenderer(
                            val accessibilityTitle: String?,
                            val loggingDirectives: LoggingDirectives?,
                            val merchantName: String?,
                            val onClickCommand: OnClickCommand?,
                            val price: String?,
                            val priceReplacementText: String?,
                            val stayInApp: Boolean?,
                            val thumbnail: Thumbnail?,
                            val title: Title?,
                            val trackingParams: String?,
                            val viewButton: ViewButton?
                        ) {
                            data class LoggingDirectives(
                                val gestures: Gestures?,
                                val trackingParams: String?,
                                val visibility: Visibility?
                            ) {
                                data class Gestures(
                                    val types: String?
                                )

                                data class Visibility(
                                    val types: String?
                                )
                            }

                            data class OnClickCommand(
                                val clickTrackingParams: String?,
                                val commandMetadata: CommandMetadata?,
                                val urlEndpoint: UrlEndpoint?
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

                                data class UrlEndpoint(
                                    val nofollow: Boolean?,
                                    val target: String?,
                                    val url: String?
                                )
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

                            data class ViewButton(
                                val buttonRenderer: ButtonRenderer?
                            ) {
                                data class ButtonRenderer(
                                    val icon: Icon?,
                                    val iconPosition: String?,
                                    val size: String?,
                                    val style: String?,
                                    val text: Text?,
                                    val trackingParams: String?
                                ) {
                                    data class Icon(
                                        val iconType: String?
                                    )

                                    data class Text(
                                        val simpleText: String?
                                    )
                                }
                            }
                        }
                    }
                }

                data class SectionListRenderer(
                    val contents: List<Content?>?,
                    val trackingParams: String?
                ) {
                    data class Content(
                        val itemSectionRenderer: ItemSectionRenderer?
                    ) {
                        data class ItemSectionRenderer(
                            val contents: List<Content?>?,
                            val sectionIdentifier: String?,
                            val targetId: String?,
                            val trackingParams: String?
                        ) {
                            data class Content(
                                val continuationItemRenderer: ContinuationItemRenderer?
                            ) {
                                data class ContinuationItemRenderer(
                                    val continuationEndpoint: ContinuationEndpoint?,
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
                                }
                            }
                        }
                    }
                }

                data class StructuredDescriptionContentRenderer(
                    val items: List<Item?>?
                ) {
                    data class Item(
                        val expandableVideoDescriptionBodyRenderer: ExpandableVideoDescriptionBodyRenderer?,
                        val horizontalCardListRenderer: HorizontalCardListRenderer?,
                        val reelShelfRenderer: ReelShelfRenderer?,
                        val videoDescriptionHeaderRenderer: VideoDescriptionHeaderRenderer?,
                        val videoDescriptionInfocardsSectionRenderer: VideoDescriptionInfocardsSectionRenderer?
                    ) {

                        data class VideoDescriptionHeaderRenderer(
                            val channel: Channel?,
                            val channelNavigationEndpoint: ChannelNavigationEndpoint?,
                            val channelThumbnail: ChannelThumbnail?,
                            val factoid: List<Factoid?>?,
                            val publishDate: PublishDate?,
                            val title: Title?,
                            val views: Views?
                        ) {
                            data class Channel(
                                val simpleText: String?
                            )

                            data class ChannelNavigationEndpoint(
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

                            data class ChannelThumbnail(
                                val thumbnails: List<Thumbnail?>?
                            ) {
                                data class Thumbnail(
                                    val url: String?
                                )
                            }

                            data class Factoid(
                                val factoidRenderer: FactoidRenderer?,
                                val viewCountFactoidRenderer: ViewCountFactoidRenderer?
                            ) {
                                data class FactoidRenderer(
                                    val accessibilityText: String?,
                                    val label: Label?,
                                    val value: Value?
                                ) {
                                    data class Label(
                                        val simpleText: String?
                                    )

                                    data class Value(
                                        val simpleText: String?
                                    )
                                }

                                data class ViewCountFactoidRenderer(
                                    val factoid: Factoid?,
                                    val viewCountEntityKey: String?,
                                    val viewCountType: String?
                                ) {
                                    data class Factoid(
                                        val factoidRenderer: FactoidRenderer?
                                    ) {
                                        data class FactoidRenderer(
                                            val accessibilityText: String?,
                                            val label: Label?,
                                            val value: Value?
                                        ) {
                                            data class Label(
                                                val simpleText: String?
                                            )

                                            data class Value(
                                                val simpleText: String?
                                            )
                                        }
                                    }
                                }
                            }

                            data class PublishDate(
                                val simpleText: String?
                            )

                            data class Title(
                                val runs: List<Run?>?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class Views(
                                val simpleText: String?
                            )
                        }

                        data class VideoDescriptionInfocardsSectionRenderer(
                            val channelAvatar: ChannelAvatar?,
                            val channelEndpoint: ChannelEndpoint?,
                            val creatorAboutButton: CreatorAboutButton?,
                            val creatorVideosButton: CreatorVideosButton?,
                            val sectionSubtitle: SectionSubtitle?,
                            val sectionTitle: SectionTitle?,
                            val trackingParams: String?
                        ) {
                            data class ChannelAvatar(
                                val thumbnails: List<Thumbnail?>?
                            ) {
                                data class Thumbnail(
                                    val url: String?
                                )
                            }

                            data class ChannelEndpoint(
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

                            data class CreatorAboutButton(
                                val buttonRenderer: ButtonRenderer?
                            ) {
                                data class ButtonRenderer(
                                    val command: Command?,
                                    val icon: Icon?,
                                    val isDisabled: Boolean?,
                                    val size: String?,
                                    val style: String?,
                                    val text: Text?,
                                    val trackingParams: String?
                                ) {
                                    data class Command(
                                        val browseEndpoint: BrowseEndpoint?,
                                        val clickTrackingParams: String?,
                                        val commandMetadata: CommandMetadata?
                                    ) {
                                        data class BrowseEndpoint(
                                            val browseId: String?,
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

                                    data class Icon(
                                        val iconType: String?
                                    )

                                    data class Text(
                                        val simpleText: String?
                                    )
                                }
                            }

                            data class CreatorVideosButton(
                                val buttonRenderer: ButtonRenderer?
                            ) {
                                data class ButtonRenderer(
                                    val command: Command?,
                                    val icon: Icon?,
                                    val isDisabled: Boolean?,
                                    val size: String?,
                                    val style: String?,
                                    val text: Text?,
                                    val trackingParams: String?
                                ) {
                                    data class Command(
                                        val browseEndpoint: BrowseEndpoint?,
                                        val clickTrackingParams: String?,
                                        val commandMetadata: CommandMetadata?
                                    ) {
                                        data class BrowseEndpoint(
                                            val browseId: String?,
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

                                    data class Icon(
                                        val iconType: String?
                                    )

                                    data class Text(
                                        val simpleText: String?
                                    )
                                }
                            }

                            data class SectionSubtitle(
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

                            data class SectionTitle(
                                val simpleText: String?
                            )
                        }
                    }
                }
            }

            data class Header(
                val engagementPanelTitleHeaderRenderer: EngagementPanelTitleHeaderRenderer?
            ) {
                data class EngagementPanelTitleHeaderRenderer(
                    val contextualInfo: ContextualInfo?,
                    val menu: Menu?,
                    val title: Title?,
                    val trackingParams: String?,
                    val visibilityButton: VisibilityButton?
                ) {
                    data class ContextualInfo(
                        val runs: List<Run?>?
                    ) {
                        data class Run(
                            val text: String?
                        )
                    }

                    data class Menu(
                        val sortFilterSubMenuRenderer: SortFilterSubMenuRenderer?
                    ) {
                        data class SortFilterSubMenuRenderer(
                            val accessibility: Accessibility?,
                            val icon: Icon?,
                            val subMenuItems: List<SubMenuItem?>?,
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

                            data class SubMenuItem(
                                val selected: Boolean?,
                                val serviceEndpoint: ServiceEndpoint?,
                                val title: String?,
                                val trackingParams: String?
                            ) {
                                data class ServiceEndpoint(
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
                                        val command: Command?,
                                        val request: String?,
                                        val token: String?
                                    ) {
                                        data class Command(
                                            val clickTrackingParams: String?,
                                            val showReloadUiCommand: ShowReloadUiCommand?
                                        ) {
                                            data class ShowReloadUiCommand(
                                                val targetId: String?
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    data class Title(
                        val accessibility: Accessibility?,
                        val runs: List<Run?>?,
                        val simpleText: String?
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

                    data class VisibilityButton(
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
                                val changeEngagementPanelVisibilityAction: ChangeEngagementPanelVisibilityAction?,
                                val clickTrackingParams: String?,
                                val commandExecutorCommand: CommandExecutorCommand?,
                                val hideEngagementPanelEndpoint: HideEngagementPanelEndpoint?
                            ) {
                                data class ChangeEngagementPanelVisibilityAction(
                                    val targetId: String?,
                                    val visibility: String?
                                )

                                data class CommandExecutorCommand(
                                    val commands: List<Command?>?
                                ) {
                                    data class Command(
                                        val changeEngagementPanelVisibilityAction: ChangeEngagementPanelVisibilityAction?,
                                        val clickTrackingParams: String?,
                                        val updateToggleButtonStateCommand: UpdateToggleButtonStateCommand?
                                    ) {
                                        data class ChangeEngagementPanelVisibilityAction(
                                            val targetId: String?,
                                            val visibility: String?
                                        )

                                        data class UpdateToggleButtonStateCommand(
                                            val buttonId: String?,
                                            val toggled: Boolean?
                                        )
                                    }
                                }

                                data class HideEngagementPanelEndpoint(
                                    val panelIdentifier: String?
                                )
                            }

                            data class Icon(
                                val iconType: String?
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
                val options: Options?,
                val payload: Payload?,
                val type: String?
            ) {
                data class Options(
                    val persistenceOption: String?
                )

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

    data class PageVisualEffect(
        val cinematicContainerRenderer: CinematicContainerRenderer?
    ) {
        data class CinematicContainerRenderer(
            val config: Config?,
            val gradientColorConfig: List<GradientColorConfig?>?,
            val presentationStyle: String?
        ) {
            data class Config(
                val animationConfig: AnimationConfig?,
                val applyClientImageBlur: Boolean?,
                val blurStrength: Int?,
                val bottomColorSourceHeightMultiplier: Double?,
                val colorSourceHeightMultiplier: Int?,
                val colorSourceSizeMultiplier: Double?,
                val colorSourceWidthMultiplier: Double?,
                val darkThemeBackgroundColor: Long?,
                val lightThemeBackgroundColor: Long?,
                val maxBottomColorSourceHeight: Int?
            ) {
                data class AnimationConfig(
                    val crossfadeDurationMs: Int?,
                    val crossfadeStartOffset: Int?,
                    val maxFrameRate: Int?,
                    val minImageUpdateIntervalMs: Int?
                )
            }

            data class GradientColorConfig(
                val darkThemeColor: Long?,
                val startLocation: Int?
            )
        }
    }

    data class PlayerOverlays(
        val playerOverlayRenderer: PlayerOverlayRenderer?
    ) {
        data class PlayerOverlayRenderer(
            val addToMenu: AddToMenu?,
            val autonavToggle: AutonavToggle?,
            val autoplay: Autoplay?,
            val endScreen: EndScreen?,
            val shareButton: ShareButton?,
            val speedmasterUserEdu: SpeedmasterUserEdu?,
            val videoDetails: VideoDetails?
        ) {
            data class AddToMenu(
                val menuRenderer: MenuRenderer?
            ) {
                data class MenuRenderer(
                    val trackingParams: String?
                )
            }

            data class AutonavToggle(
                val autoplaySwitchButtonRenderer: AutoplaySwitchButtonRenderer?
            ) {
                data class AutoplaySwitchButtonRenderer(
                    val disabledAccessibilityData: DisabledAccessibilityData?,
                    val enabled: Boolean?,
                    val enabledAccessibilityData: EnabledAccessibilityData?,
                    val onDisabledCommand: OnDisabledCommand?,
                    val onEnabledCommand: OnEnabledCommand?,
                    val trackingParams: String?
                ) {
                    data class DisabledAccessibilityData(
                        val accessibilityData: AccessibilityData?
                    ) {
                        data class AccessibilityData(
                            val label: String?
                        )
                    }

                    data class EnabledAccessibilityData(
                        val accessibilityData: AccessibilityData?
                    ) {
                        data class AccessibilityData(
                            val label: String?
                        )
                    }

                    data class OnDisabledCommand(
                        val clickTrackingParams: String?,
                        val commandMetadata: CommandMetadata?,
                        val setSettingEndpoint: SetSettingEndpoint?
                    ) {
                        data class CommandMetadata(
                            val webCommandMetadata: WebCommandMetadata?
                        ) {
                            data class WebCommandMetadata(
                                val apiUrl: String?,
                                val sendPost: Boolean?
                            )
                        }

                        data class SetSettingEndpoint(
                            val boolValue: Boolean?,
                            val settingItemId: String?,
                            val settingItemIdForClient: String?
                        )
                    }

                    data class OnEnabledCommand(
                        val clickTrackingParams: String?,
                        val commandMetadata: CommandMetadata?,
                        val setSettingEndpoint: SetSettingEndpoint?
                    ) {
                        data class CommandMetadata(
                            val webCommandMetadata: WebCommandMetadata?
                        ) {
                            data class WebCommandMetadata(
                                val apiUrl: String?,
                                val sendPost: Boolean?
                            )
                        }

                        data class SetSettingEndpoint(
                            val boolValue: Boolean?,
                            val settingItemId: String?,
                            val settingItemIdForClient: String?
                        )
                    }
                }
            }

            data class Autoplay(
                val playerOverlayAutoplayRenderer: PlayerOverlayAutoplayRenderer?
            ) {
                data class PlayerOverlayAutoplayRenderer(
                    val background: Background?,
                    val byline: Byline?,
                    val cancelButton: CancelButton?,
                    val closeButton: CloseButton?,
                    val countDownSecs: Int?,
                    val countDownSecsForFullscreen: Int?,
                    val nextButton: NextButton?,
                    val pauseText: PauseText?,
                    val preferImmediateRedirect: Boolean?,
                    val publishedTimeText: PublishedTimeText?,
                    val shortViewCountText: ShortViewCountText?,
                    val thumbnailOverlays: List<ThumbnailOverlay?>?,
                    val title: Title?,
                    val trackingParams: String?,
                    val videoId: String?,
                    val videoTitle: VideoTitle?,
                    val webShowBigThumbnailEndscreen: Boolean?,
                    val webShowNewAutonavCountdown: Boolean?
                ) {
                    data class Background(
                        val thumbnails: List<Thumbnail?>?
                    ) {
                        data class Thumbnail(
                            val height: Int?,
                            val url: String?,
                            val width: Int?
                        )
                    }

                    data class Byline(
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

                    data class CancelButton(
                        val buttonRenderer: ButtonRenderer?
                    ) {
                        data class ButtonRenderer(
                            val accessibility: Accessibility?,
                            val accessibilityData: AccessibilityData?,
                            val command: Command?,
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
                                val commandMetadata: CommandMetadata?,
                                val getSurveyCommand: GetSurveyCommand?
                            ) {
                                data class CommandMetadata(
                                    val webCommandMetadata: WebCommandMetadata?
                                ) {
                                    data class WebCommandMetadata(
                                        val apiUrl: String?,
                                        val sendPost: Boolean?
                                    )
                                }

                                data class GetSurveyCommand(
                                    val action: String?,
                                    val endpoint: Endpoint?
                                ) {
                                    data class Endpoint(
                                        val watch: Watch?
                                    ) {
                                        data class Watch(
                                            val hack: Boolean?
                                        )
                                    }
                                }
                            }

                            data class Text(
                                val simpleText: String?
                            )
                        }
                    }

                    data class CloseButton(
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

                    data class NextButton(
                        val buttonRenderer: ButtonRenderer?
                    ) {
                        data class ButtonRenderer(
                            val accessibility: Accessibility?,
                            val accessibilityData: AccessibilityData?,
                            val isDisabled: Boolean?,
                            val navigationEndpoint: NavigationEndpoint?,
                            val size: String?,
                            val style: String?,
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
                        }
                    }

                    data class PauseText(
                        val simpleText: String?
                    )

                    data class PublishedTimeText(
                        val simpleText: String?
                    )

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

                    data class ThumbnailOverlay(
                        val thumbnailOverlayTimeStatusRenderer: ThumbnailOverlayTimeStatusRenderer?
                    ) {
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
                    }

                    data class Title(
                        val simpleText: String?
                    )

                    data class VideoTitle(
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

            data class EndScreen(
                val watchNextEndScreenRenderer: WatchNextEndScreenRenderer?
            ) {
                data class WatchNextEndScreenRenderer(
                    val results: List<Result?>?,
                    val title: Title?,
                    val trackingParams: String?
                ) {
                    data class Result(
                        val endScreenVideoRenderer: EndScreenVideoRenderer?
                    ) {
                        data class EndScreenVideoRenderer(
                            val lengthInSeconds: Int?,
                            val lengthText: LengthText?,
                            val navigationEndpoint: NavigationEndpoint?,
                            val publishedTimeText: PublishedTimeText?,
                            val shortBylineText: ShortBylineText?,
                            val shortViewCountText: ShortViewCountText?,
                            val thumbnail: Thumbnail?,
                            val thumbnailOverlays: List<ThumbnailOverlay?>?,
                            val title: Title?,
                            val trackingParams: String?,
                            val videoId: String?
                        ) {
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
                                val reelWatchEndpoint: ReelWatchEndpoint?,
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
                                val thumbnailOverlayTimeStatusRenderer: ThumbnailOverlayTimeStatusRenderer?
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
                        }
                    }

                    data class Title(
                        val simpleText: String?
                    )
                }
            }

            data class ShareButton(
                val buttonRenderer: ButtonRenderer?
            ) {
                data class ButtonRenderer(
                    val icon: Icon?,
                    val isDisabled: Boolean?,
                    val navigationEndpoint: NavigationEndpoint?,
                    val size: String?,
                    val style: String?,
                    val tooltip: String?,
                    val trackingParams: String?
                ) {
                    data class Icon(
                        val iconType: String?
                    )

                    data class NavigationEndpoint(
                        val clickTrackingParams: String?,
                        val commandMetadata: CommandMetadata?,
                        val shareEntityServiceEndpoint: ShareEntityServiceEndpoint?
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
                    }
                }
            }

            data class SpeedmasterUserEdu(
                val speedmasterEduViewModel: SpeedmasterEduViewModel?
            ) {
                data class SpeedmasterEduViewModel(
                    val bodyText: BodyText?
                ) {
                    data class BodyText(
                        val content: String?
                    )
                }
            }

            data class VideoDetails(
                val playerOverlayVideoDetailsRenderer: PlayerOverlayVideoDetailsRenderer?
            ) {
                data class PlayerOverlayVideoDetailsRenderer(
                    val subtitle: Subtitle?,
                    val title: Title?
                ) {
                    data class Subtitle(
                        val runs: List<Run?>?
                    ) {
                        data class Run(
                            val text: String?
                        )
                    }

                    data class Title(
                        val simpleText: String?
                    )
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
            val webPrefetchData: WebPrefetchData?
        ) {
            data class WebPrefetchData(
                val navigationEndpoints: List<NavigationEndpoint?>?
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
                        val params: String?,
                        val playerParams: String?,
                        val videoId: String?,
                        val watchEndpointSupportedPrefetchConfig: WatchEndpointSupportedPrefetchConfig?
                    ) {
                        data class WatchEndpointSupportedPrefetchConfig(
                            val prefetchHintConfig: PrefetchHintConfig?
                        ) {
                            data class PrefetchHintConfig(
                                val countdownUiRelativeSecondsPrefetchCondition: Int?,
                                val prefetchPriority: Int?
                            )
                        }
                    }
                }
            }
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

data class MenuRendererMerchandise(
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
        val menuServiceItemRenderer: MenuServiceItemRenderer?
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
                val clickTrackingParams: String?
            )

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

        data class MenuServiceItemRenderer(
            val icon: Icon?,
            val loggingDirectives: LoggingDirectives?,
            val serviceEndpoint: ServiceEndpoint?,
            val text: Text?,
            val trackingParams: String?
        ) {
            data class Icon(
                val iconType: String?
            )

            data class LoggingDirectives(
                val gestures: Gestures?,
                val trackingParams: String?,
                val visibility: Visibility?
            ) {
                data class Gestures(
                    val types: String?
                )

                data class Visibility(
                    val types: String?
                )
            }

            data class ServiceEndpoint(
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
                    val nextEndpoint: NextEndpoint?
                ) {
                    data class NextEndpoint(
                        val clickTrackingParams: String?,
                        val commandMetadata: CommandMetadata?,
                        val getReportFormEndpoint: GetReportFormEndpoint?
                    ) {
                        data class CommandMetadata(
                            val webCommandMetadata: WebCommandMetadata?
                        ) {
                            data class WebCommandMetadata(
                                val apiUrl: String?,
                                val sendPost: Boolean?
                            )
                        }

                        data class GetReportFormEndpoint(
                            val params: String?
                        )
                    }
                }
            }

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
    }
}

data class TwoColumnWatchNextResults(
    val autoplay: Autoplay?,
    val results: Results?,
    val secondaryResults: SecondaryResults?
) {
    data class Autoplay(
        val autoplay: Autoplay?
    ) {
        data class Autoplay(
            val countDownSecs: Int?,
            val sets: List<Set?>?,
            val trackingParams: String?
        ) {
            data class Set(
                val autoplayVideo: AutoplayVideo?,
                val mode: String?
            ) {
                data class AutoplayVideo(
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
                        val watchEndpointSupportedPrefetchConfig: WatchEndpointSupportedPrefetchConfig?
                    ) {
                        data class WatchEndpointSupportedPrefetchConfig(
                            val prefetchHintConfig: PrefetchHintConfig?
                        ) {
                            data class PrefetchHintConfig(
                                val countdownUiRelativeSecondsPrefetchCondition: Int?,
                                val prefetchPriority: Int?
                            )
                        }
                    }
                }
            }
        }
    }

    data class Results(
        val results: Results?
    ) {
        data class Results(
            val contents: List<Content?>?,
            val trackingParams: String?
        )
        {
            data class Content(
                val itemSectionRenderer: ItemSectionRenderer?,
                val merchandiseShelfRenderer: MerchandiseShelfRenderer?,
                val videoPrimaryInfoRenderer: VideoPrimaryInfoRenderer?,
                val videoSecondaryInfoRenderer: VideoSecondaryInfoRenderer?
            ) {
                data class ItemSectionRenderer(
                    val contents: List<Content?>?,
                    val sectionIdentifier: String?,
                    val targetId: String?,
                    val trackingParams: String?
                ) {
                    data class Content(
                        val commentsEntryPointHeaderRenderer: CommentsEntryPointHeaderRenderer?,
                        val continuationItemRenderer: ContinuationItemRenderer?
                    ) {
                        data class CommentsEntryPointHeaderRenderer(
                            val commentCount: CommentCount?,
                            val contentRenderer: ContentRenderer?,
                            val headerText: HeaderText?,
                            val onTap: OnTap?,
                            val targetId: String?,
                            val trackingParams: String?
                        ) {
                            data class CommentCount(
                                val simpleText: String?
                            )

                            data class ContentRenderer(
                                val commentsEntryPointTeaserRenderer: CommentsEntryPointTeaserRenderer?
                            ) {
                                data class CommentsEntryPointTeaserRenderer(
                                    val teaserAvatar: TeaserAvatar?,
                                    val teaserContent: TeaserContent?,
                                    val trackingParams: String?
                                ) {
                                    data class TeaserAvatar(
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

                                    data class TeaserContent(
                                        val simpleText: String?
                                    )
                                }
                            }

                            data class HeaderText(
                                val runs: List<Run?>?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }

                            data class OnTap(
                                val clickTrackingParams: String?,
                                val commandExecutorCommand: CommandExecutorCommand?
                            ) {
                                data class CommandExecutorCommand(
                                    val commands: List<Command?>?
                                ) {
                                    data class Command(
                                        val changeEngagementPanelVisibilityAction: ChangeEngagementPanelVisibilityAction?,
                                        val clickTrackingParams: String?,
                                        val scrollToEngagementPanelCommand: ScrollToEngagementPanelCommand?
                                    ) {
                                        data class ChangeEngagementPanelVisibilityAction(
                                            val targetId: String?,
                                            val visibility: String?
                                        )

                                        data class ScrollToEngagementPanelCommand(
                                            val targetId: String?
                                        )
                                    }
                                }
                            }
                        }

                        data class ContinuationItemRenderer(
                            val continuationEndpoint: ContinuationEndpoint?,
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
                        }
                    }
                }

                data class MerchandiseShelfRenderer(
                    val actionButton: ActionButton?,
                    val hideText: String?,
                    val items: List<Item?>?,
                    val shelfType: String?,
                    val showText: String?,
                    val title: String?,
                    val trackingParams: String?
                ) {
                    data class ActionButton(
                        val menuRenderer: MenuRendererMerchandise?
                    )

                    data class Item(
                        val merchandiseItemRenderer: MerchandiseItemRenderer?
                    ) {
                        data class MerchandiseItemRenderer(
                            val accessibilityTitle: String?,
                            val additionalFeesText: String?,
                            val buttonAccessibilityText: String?,
                            val buttonCommand: ButtonCommand?,
                            val buttonText: String?,
                            val description: String?,
                            val fromVendorText: String?,
                            val price: String?,
                            val priceReplacementText: String?,
                            val showOpenInNewIcon: Boolean?,
                            val thumbnail: Thumbnail?,
                            val title: String?,
                            val trackingParams: String?,
                            val vendorName: String?
                        ) {
                            data class ButtonCommand(
                                val clickTrackingParams: String?,
                                val commandMetadata: CommandMetadata?,
                                val urlEndpoint: UrlEndpoint?
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

                                data class UrlEndpoint(
                                    val nofollow: Boolean?,
                                    val target: String?,
                                    val url: String?
                                )
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
                }

                data class VideoPrimaryInfoRenderer(
                    val dateText: DateText?,
                    val relativeDateText: RelativeDateText?,
                    val superTitleLink: SuperTitleLink?,
                    val title: Title?,
                    val trackingParams: String?,
                    val videoActions: VideoActions?,
                    val viewCount: ViewCount?
                ) {
                    data class DateText(
                        val simpleText: String?
                    )

                    data class RelativeDateText(
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

                    data class SuperTitleLink(
                        val runs: List<Run?>?
                    ) {
                        data class Run(
                            val loggingDirectives: LoggingDirectives?,
                            val navigationEndpoint: NavigationEndpoint?,
                            val text: String?
                        ) {
                            data class LoggingDirectives(
                                val enableDisplayloggerExperiment: Boolean?,
                                val trackingParams: String?,
                                val visibility: Visibility?
                            ) {
                                data class Visibility(
                                    val types: String?
                                )
                            }

                            data class NavigationEndpoint(
                                val browseEndpoint: BrowseEndpoint?,
                                val clickTrackingParams: String?,
                                val commandMetadata: CommandMetadata?
                            ) {
                                data class BrowseEndpoint(
                                    val browseId: String?,
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

                    data class Title(
                        val runs: List<Run?>?
                    ) {
                        data class Run(
                            val text: String?
                        )
                    }

                    data class VideoActions(
                        val menuRenderer: MenuRenderer?
                    ) {
                        data class MenuRenderer(
                            val accessibility: Accessibility?,
                            val flexibleItems: List<FlexibleItem?>?,
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

                            data class FlexibleItem(
                                val menuFlexibleItemRenderer: MenuFlexibleItemRenderer?
                            )

                            data class Item(
                                val menuNavigationItemRenderer: MenuNavigationItemRenderer?
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
                                        val clickTrackingParams: String?,
                                        val commandMetadata: CommandMetadata?,
                                        val modalEndpoint: ModalEndpoint?
                                    ) {
                                        data class CommandMetadata(
                                            val webCommandMetadata: WebCommandMetadata?
                                        ) {
                                            data class WebCommandMetadata(
                                                val ignoreNavigation: Boolean?
                                            )
                                        }

                                        data class ModalEndpoint(
                                            val modal: Modal?
                                        ) {
                                            data class Modal(
                                                val modalWithTitleAndButtonRenderer: ModalWithTitleAndButtonRenderer?
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

                            data class TopLevelButton(
                                val buttonRenderer: ButtonRenderer?,
                                val segmentedLikeDislikeButtonRenderer: SegmentedLikeDislikeButtonRenderer?
                            ) {
                                data class ButtonRenderer(
                                    val accessibilityData: AccessibilityData?,
                                    val icon: Icon?,
                                    val isDisabled: Boolean?,
                                    val serviceEndpoint: ServiceEndpoint?,
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

                                    data class Icon(
                                        val iconType: String?
                                    )

                                    data class ServiceEndpoint(
                                        val clickTrackingParams: String?,
                                        val commandMetadata: CommandMetadata?,
                                        val shareEntityServiceEndpoint: ShareEntityServiceEndpoint?
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
                                    }

                                    data class Text(
                                        val runs: List<Run?>?
                                    ) {
                                        data class Run(
                                            val text: String?
                                        )
                                    }
                                }

                                data class SegmentedLikeDislikeButtonRenderer(
                                    val dislikeButton: DislikeButton?,
                                    val likeButton: LikeButton?,
                                    val likeCount: String?
                                ) {
                                    data class DislikeButton(
                                        val toggleButtonRenderer: ToggleButtonRenderer?
                                    ) {
                                        data class ToggleButtonRenderer(
                                            val accessibility: Accessibility?,
                                            val accessibilityData: AccessibilityData?,
                                            val defaultIcon: DefaultIcon?,
                                            val defaultNavigationEndpoint: DefaultNavigationEndpoint?,
                                            val defaultTooltip: String?,
                                            val isDisabled: Boolean?,
                                            val isToggled: Boolean?,
                                            val style: Style?,
                                            val targetId: String?,
                                            val toggleButtonSupportedData: ToggleButtonSupportedData?,
                                            val toggledStyle: ToggledStyle?,
                                            val toggledTooltip: String?,
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

                                            data class DefaultIcon(
                                                val iconType: String?
                                            )

                                            data class DefaultNavigationEndpoint(
                                                val clickTrackingParams: String?,
                                                val commandMetadata: CommandMetadata?,
                                                val modalEndpoint: ModalEndpoint?
                                            ) {
                                                data class CommandMetadata(
                                                    val webCommandMetadata: WebCommandMetadata?
                                                ) {
                                                    data class WebCommandMetadata(
                                                        val ignoreNavigation: Boolean?
                                                    )
                                                }

                                                data class ModalEndpoint(
                                                    val modal: Modal?
                                                ) {
                                                    data class Modal(
                                                        val modalWithTitleAndButtonRenderer: ModalWithTitleAndButtonRenderer?
                                                    )
                                                }
                                            }

                                            data class Style(
                                                val styleType: String?
                                            )

                                            data class ToggleButtonSupportedData(
                                                val toggleButtonIdData: ToggleButtonIdData?
                                            ) {
                                                data class ToggleButtonIdData(
                                                    val id: String?
                                                )
                                            }

                                            data class ToggledStyle(
                                                val styleType: String?
                                            )
                                        }
                                    }

                                    data class LikeButton(
                                        val toggleButtonRenderer: ToggleButtonRenderer?
                                    ) {
                                        data class ToggleButtonRenderer(
                                            val accessibility: Accessibility?,
                                            val accessibilityData: AccessibilityData?,
                                            val defaultIcon: DefaultIcon?,
                                            val defaultNavigationEndpoint: DefaultNavigationEndpoint?,
                                            val defaultText: DefaultText?,
                                            val defaultTooltip: String?,
                                            val isDisabled: Boolean?,
                                            val isToggled: Boolean?,
                                            val style: Style?,
                                            val targetId: String?,
                                            val toggleButtonSupportedData: ToggleButtonSupportedData?,
                                            val toggledStyle: ToggledStyle?,
                                            val toggledText: ToggledText?,
                                            val toggledTooltip: String?,
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

                                            data class DefaultIcon(
                                                val iconType: String?
                                            )

                                            data class DefaultNavigationEndpoint(
                                                val clickTrackingParams: String?,
                                                val commandMetadata: CommandMetadata?,
                                                val modalEndpoint: ModalEndpoint?
                                            ) {
                                                data class CommandMetadata(
                                                    val webCommandMetadata: WebCommandMetadata?
                                                ) {
                                                    data class WebCommandMetadata(
                                                        val ignoreNavigation: Boolean?
                                                    )
                                                }

                                                data class ModalEndpoint(
                                                    val modal: Modal?
                                                ) {
                                                    data class Modal(
                                                        val modalWithTitleAndButtonRenderer: ModalWithTitleAndButtonRenderer?
                                                    )
                                                }
                                            }

                                            data class DefaultText(
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

                                            data class Style(
                                                val styleType: String?
                                            )

                                            data class ToggleButtonSupportedData(
                                                val toggleButtonIdData: ToggleButtonIdData?
                                            ) {
                                                data class ToggleButtonIdData(
                                                    val id: String?
                                                )
                                            }

                                            data class ToggledStyle(
                                                val styleType: String?
                                            )

                                            data class ToggledText(
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
                                }
                            }
                        }
                    }

                    data class ViewCount(
                        val videoViewCountRenderer: VideoViewCountRenderer?
                    ) {
                        data class VideoViewCountRenderer(
                            val originalViewCount: String?,
                            val shortViewCount: ShortViewCount?,
                            val viewCount: ViewCount?
                        ) {
                            data class ShortViewCount(
                                val simpleText: String?
                            )

                            data class ViewCount(
                                val simpleText: String?
                            )
                        }
                    }
                }
            }
        }
    }

    data class SecondaryResults(
        val secondaryResults: SecondaryResults?
    ) {
        data class SecondaryResults(
            val results: List<Result?>?,
            val targetId: String?,
            val trackingParams: String?
        ) {
            data class Result(
                val compactVideoRenderer: CompactVideoRenderer?,
                val continuationItemRenderer: ContinuationItemRenderer?
            ) {
                data class ContinuationItemRenderer(
                    val button: Button?,
                    val continuationEndpoint: ContinuationEndpoint?,
                    val trigger: String?
                ) {
                    data class Button(
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

                            data class Text(
                                val runs: List<Run?>?
                            ) {
                                data class Run(
                                    val text: String?
                                )
                            }
                        }
                    }

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
                }
            }
        }
    }
}

data class ProductListHeaderRenderer(
    val suppressPaddingDisclaimer: Boolean?,
    val title: Title?,
    val trackingParams: String?
) {
    data class Title(
        val runs: List<Run?>?
    ) {
        data class Run(
            val navigationEndpoint: NavigationEndpoint?,
            val text: String?
        ) {
            data class NavigationEndpoint(
                val clickTrackingParams: String?,
                val commandMetadata: CommandMetadata?,
                val openPopupAction: OpenPopupAction?
            ) {
                data class CommandMetadata(
                    val webCommandMetadata: WebCommandMetadata?
                ) {
                    data class WebCommandMetadata(
                        val url: String?
                    )
                }

                data class OpenPopupAction(
                    val popup: Popup?,
                    val popupType: String?
                ) {
                    data class Popup(
                        val menuPopupRenderer: MenuPopupRenderer?
                    ) {
                        data class MenuPopupRenderer(
                            val items: List<Item?>?,
                            val menuPopupAccessibility: MenuPopupAccessibility?
                        ) {
                            data class Item(
                                val menuNavigationItemRenderer: MenuNavigationItemRenderer?,
                                val menuServiceItemRenderer: MenuServiceItemRenderer?
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
                                        val clickTrackingParams: String?,
                                        val openPopupAction: OpenPopupAction?
                                    ) {
                                        data class OpenPopupAction(
                                            val popup: Popup?,
                                            val popupType: String?
                                        )
                                    }

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

                                data class MenuServiceItemRenderer(
                                    val icon: Icon?,
                                    val loggingDirectives: LoggingDirectives?,
                                    val serviceEndpoint: ServiceEndpoint?,
                                    val text: Text?,
                                    val trackingParams: String?
                                ) {
                                    data class Icon(
                                        val iconType: String?
                                    )

                                    data class LoggingDirectives(
                                        val gestures: Gestures?,
                                        val trackingParams: String?,
                                        val visibility: Visibility?
                                    ) {
                                        data class Gestures(
                                            val types: String?
                                        )

                                        data class Visibility(
                                            val types: String?
                                        )
                                    }

                                    data class ServiceEndpoint(
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
                                            val nextEndpoint: NextEndpoint?
                                        ) {
                                            data class NextEndpoint(
                                                val clickTrackingParams: String?,
                                                val commandMetadata: CommandMetadata?,
                                                val getReportFormEndpoint: GetReportFormEndpoint?
                                            ) {
                                                data class CommandMetadata(
                                                    val webCommandMetadata: WebCommandMetadata?
                                                ) {
                                                    data class WebCommandMetadata(
                                                        val apiUrl: String?,
                                                        val sendPost: Boolean?
                                                    )
                                                }

                                                data class GetReportFormEndpoint(
                                                    val params: String?
                                                )
                                            }
                                        }
                                    }

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
                            }

                            data class MenuPopupAccessibility(
                                val label: String?
                            )
                        }
                    }
                }
            }
        }
    }
}

data class ExpandableVideoDescriptionBodyRenderer(
    val attributedDescriptionBodyText: AttributedDescriptionBodyText?,
    val headerRuns: List<HeaderRun?>?,
    val showLessText: ShowLessText?,
    val showMoreText: ShowMoreText?
) {
    data class AttributedDescriptionBodyText(
        val attachmentRuns: List<AttachmentRun?>?,
        val commandRuns: List<CommandRun?>?,
        val content: String?,
        val decorationRuns: List<DecorationRun?>?,
        val styleRuns: List<StyleRun?>?
    ) {
        data class AttachmentRun(
            val alignment: String?,
            val element: Element?,
            val length: Int?,
            val startIndex: Int?
        ) {
            data class Element(
                val properties: Properties?,
                val type: Type?
            ) {
                data class Properties(
                    val layoutProperties: LayoutProperties?
                ) {
                    data class LayoutProperties(
                        val height: Height?,
                        val margin: Margin?,
                        val width: Width?
                    ) {
                        data class Height(
                            val unit: String?,
                            val value: Int?
                        )

                        data class Margin(
                            val top: Top?
                        ) {
                            data class Top(
                                val unit: String?,
                                val value: Double?
                            )
                        }

                        data class Width(
                            val unit: String?,
                            val value: Int?
                        )
                    }
                }

                data class Type(
                    val imageType: ImageType?
                ) {
                    data class ImageType(
                        val image: Image?
                    ) {
                        data class Image(
                            val sources: List<Source?>?
                        ) {
                            data class Source(
                                val url: String?
                            )
                        }
                    }
                }
            }
        }

        data class CommandRun(
            val length: Int?,
            val loggingDirectives: LoggingDirectives?,
            val onTap: OnTap?,
            val startIndex: Int?
        ) {
            data class LoggingDirectives(
                val enableDisplayloggerExperiment: Boolean?,
                val trackingParams: String?
            )

            data class OnTap(
                val innertubeCommand: InnertubeCommand?
            ) {
                data class InnertubeCommand(
                    val browseEndpoint: BrowseEndpoint?,
                    val clickTrackingParams: String?,
                    val commandMetadata: CommandMetadata?,
                    val urlEndpoint: UrlEndpoint?
                ) {
                    data class BrowseEndpoint(
                        val browseId: String?,
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

                    data class UrlEndpoint(
                        val nofollow: Boolean?,
                        val target: String?,
                        val url: String?
                    )
                }
            }
        }

        data class DecorationRun(
            val textDecorator: TextDecorator?
        ) {
            data class TextDecorator(
                val highlightTextDecorator: HighlightTextDecorator?
            ) {
                data class HighlightTextDecorator(
                    val backgroundCornerRadius: Int?,
                    val bottomPadding: Int?,
                    val highlightTextDecoratorExtensions: HighlightTextDecoratorExtensions?,
                    val length: Int?,
                    val startIndex: Int?
                ) {
                    data class HighlightTextDecoratorExtensions(
                        val highlightTextDecoratorColorMapExtension: HighlightTextDecoratorColorMapExtension?
                    ) {
                        data class HighlightTextDecoratorColorMapExtension(
                            val colorMap: List<ColorMap?>?
                        ) {
                            data class ColorMap(
                                val key: String?,
                                val value: Int?
                            )
                        }
                    }
                }
            }
        }

        data class StyleRun(
            val length: Int?,
            val startIndex: Int?,
            val styleRunExtensions: StyleRunExtensions?
        ) {
            data class StyleRunExtensions(
                val styleRunColorMapExtension: StyleRunColorMapExtension?
            ) {
                data class StyleRunColorMapExtension(
                    val colorMap: List<ColorMap?>?
                ) {
                    data class ColorMap(
                        val key: String?,
                        val value: Long?
                    )
                }
            }
        }
    }

    data class HeaderRun(
        val headerMapping: String?,
        val length: Int?,
        val startIndex: Int?
    )

    data class ShowLessText(
        val simpleText: String?
    )

    data class ShowMoreText(
        val simpleText: String?
    )
}

data class HorizontalCardListRenderer(
    val cards: List<Card?>?,
    val footerButton: FooterButton?,
    val header: Header?,
    val style: Style?,
    val trackingParams: String?
) {
    data class Card(
        val videoAttributeViewModel: VideoAttributeViewModel?
    ) {
        data class VideoAttributeViewModel(
            val image: Image?,
            val imageStyle: String?,
            val loggingDirectives: LoggingDirectives?,
            val orientation: String?,
            val overflowMenuA11yLabel: String?,
            val overflowMenuOnTap: OverflowMenuOnTap?,
            val secondarySubtitle: SecondarySubtitle?,
            val sizingRule: String?,
            val subtitle: String?,
            val title: String?
        ) {
            data class Image(
                val sources: List<Source?>?
            ) {
                data class Source(
                    val url: String?
                )
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

            data class OverflowMenuOnTap(
                val innertubeCommand: InnertubeCommand?
            ) {
                data class InnertubeCommand(
                    val clickTrackingParams: String?,
                    val commandMetadata: CommandMetadata?,
                    val confirmDialogEndpoint: ConfirmDialogEndpoint?
                ) {
                    data class CommandMetadata(
                        val webCommandMetadata: WebCommandMetadata?
                    ) {
                        data class WebCommandMetadata(
                            val ignoreNavigation: Boolean?
                        )
                    }

                    data class ConfirmDialogEndpoint(
                        val content: Content?
                    ) {
                        data class Content(
                            val confirmDialogRenderer: ConfirmDialogRenderer?
                        ) {
                            data class ConfirmDialogRenderer(
                                val confirmButton: ConfirmButton?,
                                val dialogMessages: List<DialogMessage?>?,
                                val primaryIsCancel: Boolean?,
                                val title: Title?,
                                val trackingParams: String?
                            ) {
                                data class ConfirmButton(
                                    val buttonRenderer: ButtonRenderer?
                                ) {
                                    data class ButtonRenderer(
                                        val accessibilityData: AccessibilityData?,
                                        val isDisabled: Boolean?,
                                        val size: String?,
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
                                        val bold: Boolean?,
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
            }

            data class SecondarySubtitle(
                val content: String?
            )
        }
    }

    data class FooterButton(
        val buttonViewModel: ButtonViewModel?
    ) {
        data class ButtonViewModel(
            val buttonSize: String?,
            val iconName: String?,
            val onTap: OnTap?,
            val style: String?,
            val titleFormatted: TitleFormatted?,
            val trackingParams: String?,
            val type: String?
        ) {
            data class OnTap(
                val innertubeCommand: InnertubeCommand?
            ) {
                data class InnertubeCommand(
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

            data class TitleFormatted(
                val content: String?
            )
        }
    }

    data class Header(
        val richListHeaderRenderer: RichListHeaderRenderer?
    ) {
        data class RichListHeaderRenderer(
            val subtitle: Subtitle?,
            val title: Title?,
            val trackingParams: String?
        ) {
            data class Subtitle(
                val simpleText: String?
            )

            data class Title(
                val simpleText: String?
            )
        }
    }

    data class Style(
        val type: String?
    )
}


data class ReelShelfRenderer(
    val items: List<Item?>?,
    val title: Title?,
    val trackingParams: String?
)
{
    data class Item(
        val reelItemRenderer: ReelItemRenderer?
    ) {
        data class ReelItemRenderer(
            val accessibility: Accessibility?,
            val headline: Headline?,
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

            data class Headline(
                val simpleText: String?
            )

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
        val runs: List<Run?>?
    ) {
        data class Run(
            val text: String?
        )
    }
}

data class MenuFlexibleItemRenderer(
    val menuItem: MenuItem?,
    val topLevelButton: TopLevelButton?
) {
    data class MenuItem(
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
                            val offlineabilityEntityKey: String?,
                            val params: String?,
                            val videoId: String?
                        )
                    }
                }
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
                val commandMetadata: CommandMetadata?,
                val modalEndpoint: ModalEndpoint?
            ) {
                data class CommandMetadata(
                    val webCommandMetadata: WebCommandMetadata?
                ) {
                    data class WebCommandMetadata(
                        val ignoreNavigation: Boolean?
                    )
                }

                data class ModalEndpoint(
                    val modal: Modal?
                ) {
                    data class Modal(
                        val modalWithTitleAndButtonRenderer: ModalWithTitleAndButtonRenderer?
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

    data class TopLevelButton(
        val buttonRenderer: ButtonRenderer?,
        val downloadButtonRenderer: DownloadButtonRenderer?
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
            val tooltip: String?,
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
                val commandMetadata: CommandMetadata?,
                val modalEndpoint: ModalEndpoint?
            ) {
                data class CommandMetadata(
                    val webCommandMetadata: WebCommandMetadata?
                ) {
                    data class WebCommandMetadata(
                        val ignoreNavigation: Boolean?
                    )
                }

                data class ModalEndpoint(
                    val modal: Modal?
                ) {
                    data class Modal(
                        val modalWithTitleAndButtonRenderer: ModalWithTitleAndButtonRenderer?
                    )
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

        data class DownloadButtonRenderer(
            val command: Command?,
            val size: String?,
            val style: String?,
            val targetId: String?,
            val trackingParams: String?
        ) {
            data class Command(
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
                            val offlineabilityEntityKey: String?,
                            val params: String?,
                            val videoId: String?
                        )
                    }
                }
            }
        }
    }
}

data class SubscriptionNotificationToggleButtonRenderer(
    val command: Command?,
    val currentStateId: Int?,
    val secondaryIcon: SecondaryIcon?,
    val states: List<State?>?,
    val targetId: String?,
    val trackingParams: String?
) {
    data class Command(
        val clickTrackingParams: String?,
        val commandExecutorCommand: CommandExecutorCommand?
    ) {
        data class CommandExecutorCommand(
            val commands: List<Command?>?
        ) {
            data class Command(
                val clickTrackingParams: String?,
                val openPopupAction: OpenPopupAction?
            ) {
                data class OpenPopupAction(
                    val popup: Popup?,
                    val popupType: String?
                ) {
                    data class Popup(
                        val menuPopupRenderer: MenuPopupRenderer?
                    ) {
                        data class MenuPopupRenderer(
                            val items: List<Item?>?
                        ) {
                            data class Item(
                                val menuServiceItemRenderer: MenuServiceItemRenderer?
                            )
                        }
                    }
                }
            }
        }
    }

    data class SecondaryIcon(
        val iconType: String?
    )

    data class State(
        val nextStateId: Int?,
        val state: State?,
        val stateId: Int?
    ) {
        data class State(
            val buttonRenderer: ButtonRenderer?
        ) {
            data class ButtonRenderer(
                val accessibility: Accessibility?,
                val accessibilityData: AccessibilityData?,
                val icon: Icon?,
                val isDisabled: Boolean?,
                val size: String?,
                val style: String?,
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

                data class Icon(
                    val iconType: String?
                )
            }
        }
    }
}


data class VideoSecondaryInfoRenderer(
    val attributedDescription: AttributedDescription?,
    val defaultExpanded: Boolean?,
    val descriptionCollapsedLines: Int?,
    val headerRuns: List<HeaderRun?>?,
    val metadataRowContainer: MetadataRowContainer?,
    val owner: Owner?,
    val showLessCommand: ShowLessCommand?,
    val showLessText: ShowLessText?,
    val showMoreCommand: ShowMoreCommand?,
    val showMoreText: ShowMoreText?,
    val subscribeButton: SubscribeButton?,
    val trackingParams: String?
)
{
    data class AttributedDescription(
        val attachmentRuns: List<AttachmentRun?>?,
        val commandRuns: List<CommandRun?>?,
        val content: String?,
        val decorationRuns: List<DecorationRun?>?,
        val styleRuns: List<StyleRun?>?
    ) {
        data class AttachmentRun(
            val alignment: String?,
            val element: Element?,
            val length: Int?,
            val startIndex: Int?
        ) {
            data class Element(
                val properties: Properties?,
                val type: Type?
            ) {
                data class Properties(
                    val layoutProperties: LayoutProperties?
                ) {
                    data class LayoutProperties(
                        val height: Height?,
                        val margin: Margin?,
                        val width: Width?
                    ) {
                        data class Height(
                            val unit: String?,
                            val value: Int?
                        )

                        data class Margin(
                            val top: Top?
                        ) {
                            data class Top(
                                val unit: String?,
                                val value: Double?
                            )
                        }

                        data class Width(
                            val unit: String?,
                            val value: Int?
                        )
                    }
                }

                data class Type(
                    val imageType: ImageType?
                ) {
                    data class ImageType(
                        val image: Image?
                    ) {
                        data class Image(
                            val sources: List<Source?>?
                        ) {
                            data class Source(
                                val url: String?
                            )
                        }
                    }
                }
            }
        }

        data class CommandRun(
            val length: Int?,
            val loggingDirectives: LoggingDirectives?,
            val onTap: OnTap?,
            val startIndex: Int?
        ) {
            data class LoggingDirectives(
                val enableDisplayloggerExperiment: Boolean?,
                val trackingParams: String?
            )

            data class OnTap(
                val innertubeCommand: InnertubeCommand?
            ) {
                data class InnertubeCommand(
                    val browseEndpoint: BrowseEndpoint?,
                    val clickTrackingParams: String?,
                    val commandMetadata: CommandMetadata?,
                    val urlEndpoint: UrlEndpoint?
                ) {
                    data class BrowseEndpoint(
                        val browseId: String?,
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

                    data class UrlEndpoint(
                        val nofollow: Boolean?,
                        val target: String?,
                        val url: String?
                    )
                }
            }
        }

        data class DecorationRun(
            val textDecorator: TextDecorator?
        ) {
            data class TextDecorator(
                val highlightTextDecorator: HighlightTextDecorator?
            ) {
                data class HighlightTextDecorator(
                    val backgroundCornerRadius: Int?,
                    val bottomPadding: Int?,
                    val highlightTextDecoratorExtensions: HighlightTextDecoratorExtensions?,
                    val length: Int?,
                    val startIndex: Int?
                ) {
                    data class HighlightTextDecoratorExtensions(
                        val highlightTextDecoratorColorMapExtension: HighlightTextDecoratorColorMapExtension?
                    ) {
                        data class HighlightTextDecoratorColorMapExtension(
                            val colorMap: List<ColorMap?>?
                        ) {
                            data class ColorMap(
                                val key: String?,
                                val value: Int?
                            )
                        }
                    }
                }
            }
        }

        data class StyleRun(
            val length: Int?,
            val startIndex: Int?,
            val styleRunExtensions: StyleRunExtensions?
        ) {
            data class StyleRunExtensions(
                val styleRunColorMapExtension: StyleRunColorMapExtension?
            ) {
                data class StyleRunColorMapExtension(
                    val colorMap: List<ColorMap?>?
                ) {
                    data class ColorMap(
                        val key: String?,
                        val value: Long?
                    )
                }
            }
        }
    }

    data class HeaderRun(
        val headerMapping: String?,
        val length: Int?,
        val startIndex: Int?
    )

    data class MetadataRowContainer(
        val metadataRowContainerRenderer: MetadataRowContainerRenderer?
    ) {
        data class MetadataRowContainerRenderer(
            val collapsedItemCount: Int?,
            val trackingParams: String?
        )
    }

    data class Owner(
        val videoOwnerRenderer: VideoOwnerRenderer?
    ) {
        data class VideoOwnerRenderer(
            val badges: List<Badge?>?,
            val navigationEndpoint: NavigationEndpoint?,
            val subscriberCountText: SubscriberCountText?,
            val subscriptionButton: SubscriptionButton?,
            val thumbnail: Thumbnail?,
            val title: Title?,
            val trackingParams: String?
        ) {
            data class Badge(
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

            data class SubscriptionButton(
                val type: String?
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
        }
    }

    data class ShowLessCommand(
        val changeEngagementPanelVisibilityAction: ChangeEngagementPanelVisibilityAction?,
        val clickTrackingParams: String?
    ) {
        data class ChangeEngagementPanelVisibilityAction(
            val targetId: String?,
            val visibility: String?
        )
    }

    data class ShowLessText(
        val simpleText: String?
    )

    data class ShowMoreCommand(
        val clickTrackingParams: String?,
        val commandExecutorCommand: CommandExecutorCommand?
    ) {
        data class CommandExecutorCommand(
            val commands: List<Command?>?
        ) {
            data class Command(
                val changeEngagementPanelVisibilityAction: ChangeEngagementPanelVisibilityAction?,
                val clickTrackingParams: String?,
                val scrollToEngagementPanelCommand: ScrollToEngagementPanelCommand?
            ) {
                data class ChangeEngagementPanelVisibilityAction(
                    val targetId: String?,
                    val visibility: String?
                )

                data class ScrollToEngagementPanelCommand(
                    val targetId: String?
                )
            }
        }
    }

    data class ShowMoreText(
        val simpleText: String?
    )

    data class SubscribeButton(
        val subscribeButtonRenderer: SubscribeButtonRenderer?
    ) {
        data class SubscribeButtonRenderer(
            val buttonText: ButtonText?,
            val channelId: String?,
            val enabled: Boolean?,
            val notificationPreferenceButton: NotificationPreferenceButton?,
            val onSubscribeEndpoints: List<OnSubscribeEndpoint?>?,
            val onUnsubscribeEndpoints: List<OnUnsubscribeEndpoint?>?,
            val showPreferences: Boolean?,
            val signInEndpoint: SignInEndpoint?,
            val subscribeAccessibility: SubscribeAccessibility?,
            val subscribed: Boolean?,
            val subscribedButtonText: SubscribedButtonText?,
            val subscribedEntityKey: String?,
            val targetId: String?,
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

            data class NotificationPreferenceButton(
                val subscriptionNotificationToggleButtonRenderer: SubscriptionNotificationToggleButtonRenderer?
            )

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

            data class SignInEndpoint(
                val clickTrackingParams: String?,
                val commandMetadata: CommandMetadata?,
                val modalEndpoint: ModalEndpoint?
            ) {
                data class CommandMetadata(
                    val webCommandMetadata: WebCommandMetadata?
                ) {
                    data class WebCommandMetadata(
                        val ignoreNavigation: Boolean?
                    )
                }

                data class ModalEndpoint(
                    val modal: Modal?
                ) {
                    data class Modal(
                        val modalWithTitleAndButtonRenderer: ModalWithTitleAndButtonRenderer?
                    )
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
}


data class CompactVideoRenderer(
    val accessibility: Accessibility?,
    val badges: List<Badge?>?,
    val channelThumbnail: ChannelThumbnail?,
    val lengthText: LengthText?,
    val longBylineText: LongBylineText?,
    val menu: Menu?,
    val navigationEndpoint: NavigationEndpoint?,
    val ownerBadges: List<OwnerBadge?>?,
    val publishedTimeText: PublishedTimeText?,
    val richThumbnail: RichThumbnail?,
    val shortBylineText: ShortBylineText?,
    val shortViewCountText: ShortViewCountText?,
    val thumbnail: Thumbnail?,
    val thumbnailOverlays: List<ThumbnailOverlay?>?,
    val title: Title?,
    val trackingParams: String?,
    val videoId: String?,
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
        val metadataBadgeRenderer: MetadataBadgeRenderer?
    ) {
        data class MetadataBadgeRenderer(
            val label: String?,
            val style: String?,
            val trackingParams: String?
        )
    }

    data class ChannelThumbnail(
        val thumbnails: List<Thumbnail?>?
    ) {
        data class Thumbnail(
            val height: Int?,
            val url: String?,
            val width: Int?
        )
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
            val targetId: String?,
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
                                val clickTrackingParams: String?,
                                val openPopupAction: OpenPopupAction?
                            ) {
                                data class AddToPlaylistCommand(
                                    val listType: String?,
                                    val onCreateListCommand: OnCreateListCommand?,
                                    val openListPanel: Boolean?,
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

                                data class OpenPopupAction(
                                    val popup: Popup?,
                                    val popupType: String?
                                ) {
                                    data class Popup(
                                        val notificationActionRenderer: NotificationActionRenderer?
                                    ) {
                                        data class NotificationActionRenderer(
                                            val responseText: ResponseText?,
                                            val trackingParams: String?
                                        ) {
                                            data class ResponseText(
                                                val simpleText: String?
                                            )
                                        }
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
        val reelWatchEndpoint: ReelWatchEndpoint?,
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

        data class WatchEndpoint(
            val nofollow: Boolean?,
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
                            val openListPanel: Boolean?,
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

data class MerchandiseItems(
    val description: String?,
    val title: String?,
    val thumbnail: String?,
    val price: String?,
    val link: String?,
)