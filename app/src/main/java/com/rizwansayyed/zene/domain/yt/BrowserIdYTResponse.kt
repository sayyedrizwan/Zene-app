package com.rizwansayyed.zene.domain.yt

data class BrowserIdYTResponse(
    val contents: Contents?,
    val currentVideoEndpoint: CurrentVideoEndpoint?,
    val playerOverlays: PlayerOverlays?,
    val queueContextParams: String?,
    val responseContext: ResponseContext?,
    val trackingParams: String?,
    val videoReporting: VideoReporting?
) {

    fun browseID(): String? {
        var id: String? = null
        contents?.singleColumnMusicWatchNextResultsRenderer?.tabbedRenderer?.watchNextTabbedResultsRenderer?.tabs?.forEach { t ->
            if (t?.tabRenderer?.endpoint?.browseEndpoint?.browseEndpointContextSupportedConfigs?.browseEndpointContextMusicConfig?.pageType == "MUSIC_PAGE_TYPE_TRACK_RELATED")
                id = t.tabRenderer.endpoint.browseEndpoint.browseId
        }

        return id
    }

    data class Contents(
        val singleColumnMusicWatchNextResultsRenderer: SingleColumnMusicWatchNextResultsRenderer?
    ) {
        data class SingleColumnMusicWatchNextResultsRenderer(
            val tabbedRenderer: TabbedRenderer?
        ) {
            data class TabbedRenderer(
                val watchNextTabbedResultsRenderer: WatchNextTabbedResultsRenderer?
            ) {
                data class WatchNextTabbedResultsRenderer(
                    val tabs: List<Tab?>?
                ) {
                    data class Tab(
                        val tabRenderer: TabRenderer?
                    ) {
                        data class TabRenderer(
                            val content: Content?,
                            val endpoint: Endpoint?,
                            val title: String?,
                            val trackingParams: String?
                        )
                    }
                }
            }
        }
    }

    data class CurrentVideoEndpoint(
        val clickTrackingParams: String?,
        val watchEndpoint: WatchEndpoint?
    ) {
        data class WatchEndpoint(
            val videoId: String?
        )
    }

    data class PlayerOverlays(
        val playerOverlayRenderer: PlayerOverlayRenderer?
    ) {
        data class PlayerOverlayRenderer(
            val actions: List<Action?>?,
            val browserMediaSession: BrowserMediaSession?
        ) {
            data class Action(
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

                    data class LikeCommand(
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

                    data class Target(
                        val videoId: String?
                    )
                }
            }

            data class BrowserMediaSession(
                val browserMediaSessionRenderer: BrowserMediaSessionRenderer?
            ) {
                data class BrowserMediaSessionRenderer(
                    val album: Album?,
                    val thumbnailDetails: ThumbnailDetails?
                ) {
                    data class Album(
                        val runs: List<Run?>?
                    ) {
                        data class Run(
                            val text: String?
                        )
                    }

                    data class ThumbnailDetails(
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

    data class VideoReporting(
        val reportFormModalRenderer: ReportFormModalRenderer?
    ) {
        data class ReportFormModalRenderer(
            val cancelButton: CancelButton?,
            val footer: Footer?,
            val optionsSupportedRenderers: OptionsSupportedRenderers?,
            val submitButton: SubmitButton?,
            val title: Title?,
            val trackingParams: String?
        ) {
            data class CancelButton(
                val buttonRenderer: ButtonRenderer?
            ) {
                data class ButtonRenderer(
                    val isDisabled: Boolean?,
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

            data class Footer(
                val runs: List<Run?>?
            ) {
                data class Run(
                    val navigationEndpoint: NavigationEndpoint?,
                    val text: String?
                ) {
                    data class NavigationEndpoint(
                        val clickTrackingParams: String?,
                        val urlEndpoint: UrlEndpoint?
                    ) {
                        data class UrlEndpoint(
                            val url: String?
                        )
                    }
                }
            }

            data class OptionsSupportedRenderers(
                val optionsRenderer: OptionsRenderer?
            ) {
                data class OptionsRenderer(
                    val items: List<Item?>?,
                    val trackingParams: String?
                ) {
                    data class Item(
                        val optionSelectableItemRenderer: OptionSelectableItemRenderer?
                    ) {
                        data class OptionSelectableItemRenderer(
                            val submitEndpoint: SubmitEndpoint?,
                            val text: Text?,
                            val trackingParams: String?
                        ) {
                            data class SubmitEndpoint(
                                val clickTrackingParams: String?,
                                val flagEndpoint: FlagEndpoint?
                            ) {
                                data class FlagEndpoint(
                                    val flagAction: String?
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
                }
            }

            data class SubmitButton(
                val buttonRenderer: ButtonRenderer?
            ) {
                data class ButtonRenderer(
                    val isDisabled: Boolean?,
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
                val modalEndpoint: ModalEndpoint?,
                val queueAddEndpoint: QueueAddEndpoint?,
                val removeFromQueueEndpoint: RemoveFromQueueEndpoint?
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

                data class RemoveFromQueueEndpoint(
                    val commands: List<Command?>?,
                    val videoId: String?
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

data class AutomixPreviewVideoRenderer(
    val content: Content?
) {
    data class Content(
        val automixPlaylistVideoRenderer: AutomixPlaylistVideoRenderer?
    ) {
        data class AutomixPlaylistVideoRenderer(
            val automixMode: String?,
            val navigationEndpoint: NavigationEndpoint?,
            val trackingParams: String?
        ) {
            data class NavigationEndpoint(
                val clickTrackingParams: String?,
                val watchPlaylistEndpoint: WatchPlaylistEndpoint?
            ) {
                data class WatchPlaylistEndpoint(
                    val params: String?,
                    val playlistId: String?
                )
            }
        }
    }
}

data class PlaylistPanelVideoRenderer(
    val canReorder: Boolean?,
    val lengthText: LengthText?,
    val longBylineText: LongBylineText?,
    val menu: Menu?,
    val navigationEndpoint: NavigationEndpoint?,
    val selected: Boolean?,
    val shortBylineText: ShortBylineText?,
    val thumbnail: Thumbnail?,
    val title: Title?,
    val trackingParams: String?,
    val videoId: String?
) {
    data class LengthText(
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

    data class LongBylineText(
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

    data class Menu(
        val menuRenderer: MenuRenderer?
    )

    data class NavigationEndpoint(
        val clickTrackingParams: String?,
        val watchEndpoint: WatchEndpoint?
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
                    val hasPersistentPlaylistPanel: Boolean?,
                    val musicVideoType: String?
                )
            }
        }
    }

    data class ShortBylineText(
        val runs: List<Run?>?
    ) {
        data class Run(
            val text: String?
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
        val runs: List<Run?>?
    ) {
        data class Run(
            val text: String?
        )
    }
}

data class Content(
    val musicQueueRenderer: MusicQueueRenderer?
) {
    data class MusicQueueRenderer(
        val content: Content?,
        val hack: Boolean?,
        val header: Header?
    ) {
        data class Content(
            val playlistPanelRenderer: PlaylistPanelRenderer?
        ) {
            data class PlaylistPanelRenderer(
                val contents: List<Content?>?,
                val isInfinite: Boolean?,
                val playlistId: String?,
                val trackingParams: String?
            ) {
                data class Content(
                    val automixPreviewVideoRenderer: AutomixPreviewVideoRenderer?,
                    val playlistPanelVideoRenderer: PlaylistPanelVideoRenderer?
                )
            }
        }

        data class Header(
            val musicQueueHeaderRenderer: MusicQueueHeaderRenderer?
        ) {
            data class MusicQueueHeaderRenderer(
                val buttons: List<Button?>?,
                val subtitle: Subtitle?,
                val title: Title?,
                val trackingParams: String?
            ) {
                data class Button(
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
                            val saveQueueToPlaylistCommand: SaveQueueToPlaylistCommand?
                        ) {
                            class SaveQueueToPlaylistCommand
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

                data class Subtitle(
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

data class Endpoint(
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