package com.rizwansayyed.zene.domain.yt

data class YoutubeMerchandiseResponse(
    val contents: Contents?,
    val trackingParams: String?
) {
    data class Contents(
        val twoColumnWatchNextResults: TwoColumnWatchNextResults?
    ) {
        data class TwoColumnWatchNextResults(
            val results: Results?
        ) {
            data class Results(
                val results: Results?
            ) {
                data class Results(
                    val contents: List<Content?>?,
                    val trackingParams: String?
                ) {
                    data class Content(
                        val merchandiseShelfRenderer: MerchandiseShelfRenderer?,
                    ) {

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
                                                val openPopupAction: Any?
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
                            }

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
                    }
                }
            }
        }
    }
}