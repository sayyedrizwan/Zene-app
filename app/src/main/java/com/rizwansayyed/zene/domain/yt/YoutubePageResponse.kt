package com.rizwansayyed.zene.domain.yt

data class YoutubePageResponse(
    val items: List<Item?>?,
    val responseContext: ResponseContext?,
    val trackingParams: String?
) {
    data class Item(
        val guideSectionRenderer: GuideSectionRenderer?,
        val guideSubscriptionsSectionRenderer: GuideSubscriptionsSectionRenderer?
    ) {
        data class GuideSectionRenderer(
            val formattedTitle: FormattedTitle?,
            val items: List<Item?>?,
            val trackingParams: String?
        ) {
            data class FormattedTitle(
                val simpleText: String?
            )

            data class Item(
                val guideCollapsibleSectionEntryRenderer: GuideCollapsibleSectionEntryRenderer?,
                val guideEntryRenderer: GuideEntryRenderer?
            ) {
                data class GuideCollapsibleSectionEntryRenderer(
                    val collapserIcon: CollapserIcon?,
                    val expanderIcon: ExpanderIcon?,
                    val handlerDatas: List<String?>?,
                    val headerEntry: HeaderEntry?,
                    val sectionItems: List<SectionItem?>?
                ) {
                    data class CollapserIcon(
                        val iconType: String?
                    )

                    data class ExpanderIcon(
                        val iconType: String?
                    )

                    data class HeaderEntry(
                        val guideEntryRenderer: GuideEntryRenderer?
                    ) {
                        data class GuideEntryRenderer(
                            val accessibility: Accessibility?,
                            val formattedTitle: FormattedTitle?,
                            val icon: Icon?,
                            val isPrimary: Boolean?,
                            val navigationEndpoint: NavigationEndpoint?,
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

                            data class FormattedTitle(
                                val simpleText: String?
                            )

                            data class Icon(
                                val iconType: String?
                            )

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

                    data class SectionItem(
                        val guideCollapsibleEntryRenderer: GuideCollapsibleEntryRenderer?,
                        val guideDownloadsEntryRenderer: GuideDownloadsEntryRenderer?,
                        val guideEntryRenderer: GuideEntryRenderer?
                    ) {
                        data class GuideCollapsibleEntryRenderer(
                            val collapserItem: CollapserItem?,
                            val expandableItems: List<ExpandableItem?>?,
                            val expanderItem: ExpanderItem?
                        ) {
                            data class CollapserItem(
                                val guideEntryRenderer: GuideEntryRenderer?
                            ) {
                                data class GuideEntryRenderer(
                                    val accessibility: Accessibility?,
                                    val formattedTitle: FormattedTitle?,
                                    val icon: Icon?,
                                    val trackingParams: String?
                                ) {
                                    data class Accessibility(
                                        val accessibilityData: AccessibilityData?
                                    ) {
                                        data class AccessibilityData(
                                            val label: String?
                                        )
                                    }

                                    data class FormattedTitle(
                                        val simpleText: String?
                                    )

                                    data class Icon(
                                        val iconType: String?
                                    )
                                }
                            }

                            data class ExpandableItem(
                                val guideEntryRenderer: GuideEntryRenderer?
                            ) {
                                data class GuideEntryRenderer(
                                    val accessibility: Accessibility?,
                                    val entryData: EntryData?,
                                    val formattedTitle: FormattedTitle?,
                                    val icon: Icon?,
                                    val navigationEndpoint: NavigationEndpoint?,
                                    val trackingParams: String?
                                ) {
                                    data class Accessibility(
                                        val accessibilityData: AccessibilityData?
                                    ) {
                                        data class AccessibilityData(
                                            val label: String?
                                        )
                                    }

                                    data class EntryData(
                                        val guideEntryData: GuideEntryData?
                                    ) {
                                        data class GuideEntryData(
                                            val guideEntryId: String?
                                        )
                                    }

                                    data class FormattedTitle(
                                        val simpleText: String?
                                    )

                                    data class Icon(
                                        val iconType: String?
                                    )

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

                            data class ExpanderItem(
                                val guideEntryRenderer: GuideEntryRenderer?
                            ) {
                                data class GuideEntryRenderer(
                                    val accessibility: Accessibility?,
                                    val formattedTitle: FormattedTitle?,
                                    val icon: Icon?,
                                    val trackingParams: String?
                                ) {
                                    data class Accessibility(
                                        val accessibilityData: AccessibilityData?
                                    ) {
                                        data class AccessibilityData(
                                            val label: String?
                                        )
                                    }

                                    data class FormattedTitle(
                                        val simpleText: String?
                                    )

                                    data class Icon(
                                        val iconType: String?
                                    )
                                }
                            }
                        }

                        data class GuideDownloadsEntryRenderer(
                            val alwaysShow: Boolean?,
                            val entryRenderer: EntryRenderer?
                        ) {
                            data class EntryRenderer(
                                val guideEntryRenderer: GuideEntryRenderer?
                            ) {
                                data class GuideEntryRenderer(
                                    val accessibility: Accessibility?,
                                    val formattedTitle: FormattedTitle?,
                                    val icon: Icon?,
                                    val isPrimary: Boolean?,
                                    val navigationEndpoint: NavigationEndpoint?,
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

                                    data class FormattedTitle(
                                        val simpleText: String?
                                    )

                                    data class Icon(
                                        val iconType: String?
                                    )

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

                        data class GuideEntryRenderer(
                            val accessibility: Accessibility?,
                            val entryData: EntryData?,
                            val formattedTitle: FormattedTitle?,
                            val icon: Icon?,
                            val navigationEndpoint: NavigationEndpoint?,
                            val trackingParams: String?
                        ) {
                            data class Accessibility(
                                val accessibilityData: AccessibilityData?
                            ) {
                                data class AccessibilityData(
                                    val label: String?
                                )
                            }

                            data class EntryData(
                                val guideEntryData: GuideEntryData?
                            ) {
                                data class GuideEntryData(
                                    val guideEntryId: String?
                                )
                            }

                            data class FormattedTitle(
                                val simpleText: String?
                            )

                            data class Icon(
                                val iconType: String?
                            )

                            data class NavigationEndpoint(
                                val browseEndpoint: BrowseEndpoint?,
                                val clickTrackingParams: String?,
                                val commandMetadata: CommandMetadata?,
                                val urlEndpoint: UrlEndpoint?
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

                                data class UrlEndpoint(
                                    val target: String?,
                                    val url: String?
                                )
                            }
                        }
                    }
                }

                data class GuideEntryRenderer(
                    val accessibility: Accessibility?,
                    val formattedTitle: FormattedTitle?,
                    val icon: Icon?,
                    val isPrimary: Boolean?,
                    val navigationEndpoint: NavigationEndpoint?,
                    val serviceEndpoint: ServiceEndpoint?,
                    val trackingParams: String?
                ) {
                    data class Accessibility(
                        val accessibilityData: AccessibilityData?
                    ) {
                        data class AccessibilityData(
                            val label: String?
                        )
                    }

                    data class FormattedTitle(
                        val simpleText: String?
                    )

                    data class Icon(
                        val iconType: String?
                    )

                    data class NavigationEndpoint(
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
                            val target: String?,
                            val url: String?
                        )
                    }

                    data class ServiceEndpoint(
                        val clickTrackingParams: String?,
                        val commandMetadata: CommandMetadata?,
                        val reelWatchEndpoint: ReelWatchEndpoint?,
                        val signalServiceEndpoint: SignalServiceEndpoint?
                    ) {
                        data class CommandMetadata(
                            val webCommandMetadata: WebCommandMetadata?
                        ) {
                            data class WebCommandMetadata(
                                val rootVe: Int?,
                                val sendPost: Boolean?,
                                val url: String?,
                                val webPageType: String?
                            )
                        }

                        data class ReelWatchEndpoint(
                            val inputType: String?,
                            val loggingContext: LoggingContext?,
                            val overlay: Overlay?,
                            val params: String?,
                            val playerParams: String?,
                            val sequenceProvider: String?,
                            val ustreamerConfig: String?
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
                        }

                        data class SignalServiceEndpoint(
                            val actions: List<Action?>?,
                            val signal: String?
                        ) {
                            data class Action(
                                val clickTrackingParams: String?,
                                val sendFeedbackAction: SendFeedbackAction?,
                                val signalAction: SignalAction?
                            ) {
                                data class SendFeedbackAction(
                                    val bucket: String?
                                )

                                data class SignalAction(
                                    val signal: String?
                                )
                            }
                        }
                    }
                }
            }
        }

        data class GuideSubscriptionsSectionRenderer(
            val formattedTitle: FormattedTitle?,
            val handlerDatas: List<String?>?,
            val items: List<Item?>?,
            val sort: String?,
            val trackingParams: String?
        ) {
            data class FormattedTitle(
                val simpleText: String?
            )

            data class Item(
                val guideCollapsibleEntryRenderer: GuideCollapsibleEntryRenderer?,
                val guideEntryRenderer: GuideEntryRenderer?
            ) {
                data class GuideCollapsibleEntryRenderer(
                    val collapserItem: CollapserItem?,
                    val expandableItems: List<ExpandableItem?>?,
                    val expanderItem: ExpanderItem?
                ) {
                    data class CollapserItem(
                        val guideEntryRenderer: GuideEntryRenderer?
                    ) {
                        data class GuideEntryRenderer(
                            val accessibility: Accessibility?,
                            val formattedTitle: FormattedTitle?,
                            val icon: Icon?,
                            val trackingParams: String?
                        ) {
                            data class Accessibility(
                                val accessibilityData: AccessibilityData?
                            ) {
                                data class AccessibilityData(
                                    val label: String?
                                )
                            }

                            data class FormattedTitle(
                                val simpleText: String?
                            )

                            data class Icon(
                                val iconType: String?
                            )
                        }
                    }

                    data class ExpandableItem(
                        val guideEntryRenderer: GuideEntryRenderer?
                    ) {
                        data class GuideEntryRenderer(
                            val accessibility: Accessibility?,
                            val badges: Badges?,
                            val entryData: EntryData?,
                            val formattedTitle: FormattedTitle?,
                            val icon: Icon?,
                            val navigationEndpoint: NavigationEndpoint?,
                            val presentationStyle: String?,
                            val thumbnail: Thumbnail?,
                            val trackingParams: String?
                        ) {
                            data class Accessibility(
                                val accessibilityData: AccessibilityData?
                            ) {
                                data class AccessibilityData(
                                    val label: String?
                                )
                            }

                            data class Badges(
                                val liveBroadcasting: Boolean?
                            )

                            data class EntryData(
                                val guideEntryData: GuideEntryData?
                            ) {
                                data class GuideEntryData(
                                    val guideEntryId: String?
                                )
                            }

                            data class FormattedTitle(
                                val simpleText: String?
                            )

                            data class Icon(
                                val iconType: String?
                            )

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
                                    val url: String?
                                )
                            }
                        }
                    }

                    data class ExpanderItem(
                        val guideEntryRenderer: GuideEntryRenderer?
                    ) {
                        data class GuideEntryRenderer(
                            val accessibility: Accessibility?,
                            val formattedTitle: FormattedTitle?,
                            val icon: Icon?,
                            val trackingParams: String?
                        ) {
                            data class Accessibility(
                                val accessibilityData: AccessibilityData?
                            ) {
                                data class AccessibilityData(
                                    val label: String?
                                )
                            }

                            data class FormattedTitle(
                                val simpleText: String?
                            )

                            data class Icon(
                                val iconType: String?
                            )
                        }
                    }
                }

                data class GuideEntryRenderer(
                    val accessibility: Accessibility?,
                    val badges: Badges?,
                    val entryData: EntryData?,
                    val formattedTitle: FormattedTitle?,
                    val navigationEndpoint: NavigationEndpoint?,
                    val presentationStyle: String?,
                    val thumbnail: Thumbnail?,
                    val trackingParams: String?
                ) {
                    data class Accessibility(
                        val accessibilityData: AccessibilityData?
                    ) {
                        data class AccessibilityData(
                            val label: String?
                        )
                    }

                    data class Badges(
                        val liveBroadcasting: Boolean?
                    )

                    data class EntryData(
                        val guideEntryData: GuideEntryData?
                    ) {
                        data class GuideEntryData(
                            val guideEntryId: String?
                        )
                    }

                    data class FormattedTitle(
                        val simpleText: String?
                    )

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
                            val url: String?
                        )
                    }
                }
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
            val datasyncId: String?,
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
            val hasDecorated: Boolean?
        )
    }
}