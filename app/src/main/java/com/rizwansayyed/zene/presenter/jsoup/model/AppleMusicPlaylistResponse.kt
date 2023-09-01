package com.rizwansayyed.zene.presenter.jsoup.model

import com.squareup.moshi.Json

data class AppleMusicPlaylistResponse(
    val `data`: Data?,
    val intent: Intent?
) {
    data class Data(
        val canonicalURL: String?,
        val invalidationRules: InvalidationRules?,
        val pageMetrics: PageMetrics?,
        val sections: List<Section?>?,
        val seoData: SeoData?
    ) {
        data class InvalidationRules(
            val eventTriggers: List<EventTrigger?>?
        ) {
            data class EventTrigger(
                val events: List<Event?>?,
                val intent: Intent?
            ) {
                data class Event(
                    val name: String?
                )

                data class Intent(
                    val `$kind`: String?,
                    val intent: Intent?,
                    val reason: String?
                ) {
                    data class Intent(
                        val `$kind`: String?,
                        val contentDescriptor: ContentDescriptor?,
                        val prominentItemIdentifier: Any?
                    ) {
                        data class ContentDescriptor(
                            val identifiers: Identifiers?,
                            val kind: String?,
                            val locale: Locale?
                        ) {
                            data class Identifiers(
                                val storeAdamID: String?
                            )

                            data class Locale(
                                val language: Any?,
                                val storefront: String?
                            )
                        }
                    }
                }
            }
        }

        data class PageMetrics(
            val custom: Custom?,
            val instructions: List<Instruction?>?,
            val pageFields: PageFields?
        ) {
            class Custom

            data class Instruction(
                val `data`: Data?,
                val invocationPoints: List<String?>?
            ) {
                data class Data(
                    val excludingFields: List<Any?>?,
                    val fields: Fields?,
                    val includingFields: List<String?>?,
                    val shouldFlush: Boolean?,
                    val topic: String?
                ) {
                    data class Fields(
                        val eventType: String?
                    )
                }
            }

            data class PageFields(
                val page: String?,
                val pageFeatureName: String?,
                val pageId: String?,
                val pageType: String?,
                val pageUrl: String?
            )
        }

        data class Section(
            val backgroundTreatment: String?,
            val displaySeparator: Boolean?,
            val header: Header?,
            val id: String?,
            val itemKind: String?,
            val items: List<Item?>?,
            val presentation: Presentation?
        ) {
            data class Header(
                val item: Item?,
                val kind: String?
            ) {
                data class Item(
                    val firstColumnText: String?,
                    val secondColumnText: String?,
                    val thirdColumnText: String?,
                    val titleLink: TitleLink?
                ) {
                    data class TitleLink(
                        val segue: Segue?,
                        val title: String?
                    ) {
                        data class Segue(
                            val `$kind`: String?,
                            val actionMetrics: ActionMetrics?,
                            val destination: Destination?
                        ) {
                            data class ActionMetrics(
                                val custom: Custom?,
                                val `data`: List<Data?>?
                            ) {
                                class Custom

                                data class Data(
                                    val excludingFields: List<Any?>?,
                                    val fields: Fields?,
                                    val includingFields: List<String?>?,
                                    val shouldFlush: Boolean?,
                                    val topic: String?
                                ) {
                                    data class Fields(
                                        val actionType: String?,
                                        val actionUrl: String?,
                                        val eventType: String?,
                                        val eventVersion: Int?,
                                        val targetId: String?,
                                        val targetType: String?
                                    )
                                }
                            }

                            data class Destination(
                                val intent: Intent?,
                                val kind: String?
                            ) {
                                data class Intent(
                                    val `$kind`: String?,
                                    val containerType: String?,
                                    val id: String?,
                                    val title: String?,
                                    val url: String?,
                                    val viewKind: String?
                                )
                            }
                        }
                    }
                }
            }

            data class Item(
                val accessibilityLabel: String?,
                val artistName: String?,
                val artwork: Artwork?,
                val canEdit: Boolean?,
                val composer: String?,
                val contentDescriptor: ContentDescriptor?,
                val description: String?,
                val duration: Int?,
                val hidden: Boolean?,
                val id: String?,
                val isDisabled: Boolean?,
                val isPreviewMode: Boolean?,
                val isProminent: Boolean?,
                val layoutStyle: LayoutStyle?,
                val modalPresentationDescriptor: ModalPresentationDescriptor?,
                val name: String?,
                val numberOfSocialBadges: Int?,
                val pauseAction: PauseAction?,
                val playAction: PlayAction?,
                val playButton: PlayButton?,
                val quaternaryTitle: Any?,
                val rankingText: String?,
                val resumeAction: ResumeAction?,
                val segue: Segue?,
                val showExplicitBadge: Boolean?,
                val showPopularityIndicator: Boolean?,
                val shuffleButton: Any?,
                val siriBannerConfiguration: Any?,
                val socialProfileContentDescriptor: Any?,
                val subtitleLinks: List<SubtitleLink?>?,
                val tertiaryLinks: List<TertiaryLink?>?,
                val tertiaryTitleLinks: List<Any?>?,
                val title: String?,
                val trackCount: Int?,
                val trackNumber: Any?,
                val videoArtwork: VideoArtwork?
            ) {
                data class Artwork(
                    val dictionary: Dictionary?
                ) {
                    data class Dictionary(
                        val bgColor: String?,
                        val hasP3: Boolean?,
                        val height: Int?,
                        val textColor1: String?,
                        val textColor2: String?,
                        val textColor3: String?,
                        val textColor4: String?,
                        val url: String?,
                        val width: Int?
                    )
                }

                data class ContentDescriptor(
                    val identifiers: Identifiers?,
                    val kind: String?,
                    val url: String?
                ) {
                    data class Identifiers(
                        val storeAdamID: String?
                    )
                }

                data class LayoutStyle(
                    val hasBadging: Boolean?,
                    val hasVideo: Boolean?,
                    val kind: String?
                )

                data class ModalPresentationDescriptor(
                    val headerSubtitle: String?,
                    val headerTitle: String?,
                    val paragraphText: String?
                )

                data class PauseAction(
                    val `$kind`: String?,
                    val actionMetrics: ActionMetrics?
                ) {
                    data class ActionMetrics(
                        val custom: Custom?,
                        val `data`: List<Any?>?
                    ) {
                        class Custom
                    }
                }

                data class PlayAction(
                    val `$kind`: String?,
                    val actionMetrics: ActionMetrics?,
                    val containerContentDescriptor: ContainerContentDescriptor?,
                    val groupingIdentifier: String?,
                    val items: List<Item?>?
                ) {
                    data class ActionMetrics(
                        val custom: Custom?,
                        val `data`: List<Data?>?
                    ) {
                        class Custom

                        data class Data(
                            val excludingFields: List<Any?>?,
                            val fields: Fields?,
                            val includingFields: List<String?>?,
                            val shouldFlush: Boolean?,
                            val topic: String?
                        ) {
                            data class Fields(
                                val actionDetails: ActionDetails?,
                                val actionType: String?,
                                val actionUrl: String?,
                                val eventType: String?,
                                val eventVersion: Int?,
                                val targetId: String?,
                                val targetType: String?
                            ) {
                                data class ActionDetails(
                                    val kind: String?
                                )
                            }
                        }
                    }

                    data class ContainerContentDescriptor(
                        val identifiers: Identifiers?,
                        val kind: String?,
                        val url: String?
                    ) {
                        data class Identifiers(
                            val storeAdamID: String?
                        )
                    }

                    data class Item(
                        val contentDescriptor: ContentDescriptor?
                    ) {
                        data class ContentDescriptor(
                            val identifiers: Identifiers?,
                            val kind: String?,
                            val url: String?
                        ) {
                            data class Identifiers(
                                val storeAdamID: String?
                            )
                        }
                    }
                }

                data class PlayButton(
                    val id: String?,
                    val segue: Segue?,
                    val title: String?
                ) {
                    data class Segue(
                        val `$kind`: String?,
                        val actionMetrics: ActionMetrics?,
                        val containerContentDescriptor: ContainerContentDescriptor?,
                        val groupingIdentifier: Any?,
                        val items: List<Item?>?
                    ) {
                        data class ActionMetrics(
                            val custom: Custom?,
                            val `data`: List<Data?>?
                        ) {
                            class Custom

                            data class Data(
                                val excludingFields: List<Any?>?,
                                val fields: Fields?,
                                val includingFields: List<String?>?,
                                val shouldFlush: Boolean?,
                                val topic: String?
                            ) {
                                data class Fields(
                                    val actionDetails: ActionDetails?,
                                    val actionType: String?,
                                    val actionUrl: String?,
                                    val eventType: String?,
                                    val eventVersion: Int?,
                                    val targetId: String?,
                                    val targetType: String?
                                ) {
                                    data class ActionDetails(
                                        val kind: String?
                                    )
                                }
                            }
                        }

                        data class ContainerContentDescriptor(
                            val identifiers: Identifiers?,
                            val kind: String?,
                            val url: String?
                        ) {
                            data class Identifiers(
                                val storeAdamID: String?
                            )
                        }

                        data class Item(
                            val contentDescriptor: ContentDescriptor?
                        ) {
                            data class ContentDescriptor(
                                val identifiers: Identifiers?,
                                val kind: String?,
                                val url: String?
                            ) {
                                data class Identifiers(
                                    val storeAdamID: String?
                                )
                            }
                        }
                    }
                }

                data class ResumeAction(
                    val `$kind`: String?,
                    val actionMetrics: ActionMetrics?
                ) {
                    data class ActionMetrics(
                        val custom: Custom?,
                        val `data`: List<Any?>?
                    ) {
                        class Custom
                    }
                }

                data class Segue(
                    val `$kind`: String?,
                    val actionMetrics: ActionMetrics?,
                    val destination: Destination?
                ) {
                    data class ActionMetrics(
                        val custom: Custom?,
                        val `data`: List<Data?>?
                    ) {
                        class Custom

                        data class Data(
                            val excludingFields: List<Any?>?,
                            val fields: Fields?,
                            val includingFields: List<String?>?,
                            val shouldFlush: Boolean?,
                            val topic: String?
                        ) {
                            data class Fields(
                                val actionDetails: ActionDetails?,
                                val actionType: String?,
                                val actionUrl: String?,
                                val eventType: String?,
                                val eventVersion: Int?,
                                val targetId: String?,
                                val targetType: String?
                            ) {
                                data class ActionDetails(
                                    val kind: String?
                                )
                            }
                        }
                    }

                    data class Destination(
                        val contentDescriptor: ContentDescriptor?,
                        val kind: String?,
                        val prominentItemIdentifier: Any?
                    ) {
                        data class ContentDescriptor(
                            val identifiers: Identifiers?,
                            val kind: String?,
                            val url: String?
                        ) {
                            data class Identifiers(
                                val storeAdamID: String?
                            )
                        }
                    }
                }

                data class SubtitleLink(
                    val segue: Segue?,
                    val title: String?
                ) {
                    data class Segue(
                        val `$kind`: String?,
                        val actionMetrics: ActionMetrics?,
                        val destination: Destination?
                    ) {
                        data class ActionMetrics(
                            val custom: Custom?,
                            val `data`: List<Data?>?
                        ) {
                            class Custom

                            data class Data(
                                val excludingFields: List<Any?>?,
                                val fields: Fields?,
                                val includingFields: List<String?>?,
                                val shouldFlush: Boolean?,
                                val topic: String?
                            ) {
                                data class Fields(
                                    val actionDetails: ActionDetails?,
                                    val actionType: String?,
                                    val actionUrl: String?,
                                    val eventType: String?,
                                    val eventVersion: Int?,
                                    val targetId: String?,
                                    val targetType: String?
                                ) {
                                    data class ActionDetails(
                                        val kind: String?
                                    )
                                }
                            }
                        }

                        data class Destination(
                            val contentDescriptor: ContentDescriptor?,
                            val kind: String?,
                            val prominentItemIdentifier: Any?
                        ) {
                            data class ContentDescriptor(
                                val identifiers: Identifiers?,
                                val kind: String?,
                                val url: String?
                            ) {
                                data class Identifiers(
                                    val storeAdamID: String?
                                )
                            }
                        }
                    }
                }

                data class TertiaryLink(
                    val segue: Segue?,
                    val title: String?
                ) {
                    data class Segue(
                        val `$kind`: String?,
                        val actionMetrics: ActionMetrics?,
                        val destination: Destination?
                    ) {
                        data class ActionMetrics(
                            val custom: Custom?,
                            val `data`: List<Data?>?
                        ) {
                            class Custom

                            data class Data(
                                val excludingFields: List<Any?>?,
                                val fields: Fields?,
                                val includingFields: List<String?>?,
                                val shouldFlush: Boolean?,
                                val topic: String?
                            ) {
                                data class Fields(
                                    val actionDetails: ActionDetails?,
                                    val actionType: String?,
                                    val actionUrl: String?,
                                    val eventType: String?,
                                    val eventVersion: Int?,
                                    val targetId: String?,
                                    val targetType: String?
                                ) {
                                    data class ActionDetails(
                                        val kind: String?
                                    )
                                }
                            }
                        }

                        data class Destination(
                            val contentDescriptor: ContentDescriptor?,
                            val kind: String?,
                            val prominentItemIdentifier: Any?
                        ) {
                            data class ContentDescriptor(
                                val identifiers: Identifiers?,
                                val kind: String?,
                                val locale: Locale?,
                                val url: String?
                            ) {
                                data class Identifiers(
                                    val storeAdamID: String?
                                )

                                data class Locale(
                                    val language: Any?,
                                    val storefront: String?
                                )
                            }
                        }
                    }
                }

                data class VideoArtwork(
                    val cropStyle: String?,
                    val dictionary: Dictionary?
                ) {
                    data class Dictionary(
                        val motionDetailSquare: MotionDetailSquare?
                    ) {
                        data class MotionDetailSquare(
                            val previewFrame: PreviewFrame?,
                            val video: String?
                        ) {
                            data class PreviewFrame(
                                val bgColor: String?,
                                val hasP3: Boolean?,
                                val height: Int?,
                                val textColor1: String?,
                                val textColor2: String?,
                                val textColor3: String?,
                                val textColor4: String?,
                                val url: String?,
                                val width: Int?
                            )
                        }
                    }
                }
            }

            data class Presentation(
                val kind: String?,
                val layout: Any?
            )
        }

        data class SeoData(
            val appleContentId: String?,
            val appleDescription: String?,
            val appleStoreId: String?,
            val appleStoreName: String?,
            val appleTitle: String?,
            val artworkUrl: String?,
            val crop: String?,
            val description: String?,
            val fileType: String?,
            val height: Int?,
            val keywords: String?,
            val noFollow: Boolean?,
            val noIndex: Boolean?,
            val oembedData: OembedData?,
            val ogSongs: List<OgSong?>?,
            val ogType: String?,
            val pageTitle: String?,
            val quality: Int?,
            val schemaContent: SchemaContent?,
            val schemaName: String?,
            val showOrgData: Boolean?,
            val siteName: String?,
            val socialDescription: String?,
            val socialTitle: String?,
            val songsCount: Int?,
            val twitterCardType: String?,
            val twitterCropCode: String?,
            val twitterHeight: Int?,
            val twitterSite: String?,
            val twitterWidth: Int?,
            val url: String?,
            val width: Int?
        ) {
            data class OembedData(
                val title: String?,
                val url: String?
            )

            data class OgSong(
                val attributes: Attributes?,
                val href: String?,
                val id: String?,
                val relationships: Relationships?,
                val type: String?
            ) {
                data class Attributes(
                    val albumName: String?,
                    val artistName: String?,
                    val artwork: Artwork?,
                    val audioTraits: List<String?>?,
                    val composerName: String?,
                    val contentRating: String?,
                    val durationInMillis: Int?,
                    val extendedAssetUrls: ExtendedAssetUrls?,
                    val name: String?,
                    val playParams: PlayParams?,
                    val url: String?
                ) {
                    data class Artwork(
                        val bgColor: String?,
                        val hasP3: Boolean?,
                        val height: Int?,
                        val textColor1: String?,
                        val textColor2: String?,
                        val textColor3: String?,
                        val textColor4: String?,
                        val url: String?,
                        val width: Int?
                    )

                    data class ExtendedAssetUrls(
                        val enhancedHls: String?,
                        val lightweight: String?,
                        val lightweightPlus: String?,
                        val plus: String?,
                        val superLightweight: String?
                    )

                    data class PlayParams(
                        val id: String?,
                        val kind: String?
                    )
                }

                data class Relationships(
                    val artists: Artists?
                ) {
                    data class Artists(
                        val `data`: List<Data?>?,
                        val href: String?
                    ) {
                        data class Data(
                            val href: String?,
                            val id: String?,
                            val type: String?
                        )
                    }
                }
            }

            data class SchemaContent(
                @Json(name = "@context")
                val context: String?,
                @Json(name = "@type")
                val type: String?,
                val author: Author?,
                val datePublished: String?,
                val description: String?,
                val name: String?,
                val numTracks: Int?,
                val offers: Offers?,
                val potentialAction: PotentialAction?,
                val track: List<Track?>?,
                val url: String?
            ) {
                data class Author(
                    @Json(name = "@type")
                    val type: String?,
                    val name: String?
                )

                data class Offers(
                    @Json(name = "@type")
                    val type: String?,
                    val category: String?
                )

                data class PotentialAction(
                    @Json(name = "@type")
                    val type: String?,
                    val expectsAcceptanceOf: ExpectsAcceptanceOf?,
                    val target: Target?
                ) {
                    data class ExpectsAcceptanceOf(
                        @Json(name = "@type")
                        val type: String?,
                        val category: String?
                    )

                    data class Target(
                        @Json(name = "@type")
                        val type: String?,
                        val actionPlatform: String?
                    )
                }

                data class Track(
                    @Json(name = "@type")
                    val type: String?,
                    val audio: Audio?,
                    val duration: String?,
                    val name: String?,
                    val offers: Offers?,
                    val url: String?
                ) {
                    data class Audio(
                        @Json(name = "@type")
                        val type: String?,
                        val duration: String?,
                        val name: String?,
                        val potentialAction: PotentialAction?,
                        val thumbnailUrl: String?
                    ) {
                        data class PotentialAction(
                            @Json(name = "@type")
                            val type: String?,
                            val expectsAcceptanceOf: ExpectsAcceptanceOf?,
                            val target: Target?
                        ) {
                            data class ExpectsAcceptanceOf(
                                @Json(name = "@type")
                                val type: String?,
                                val category: String?
                            )

                            data class Target(
                                @Json(name = "@type")
                                val type: String?,
                                val actionPlatform: String?
                            )
                        }
                    }

                    data class Offers(
                        @Json(name = "@type")
                        val type: String?,
                        val category: String?,
                        val price: Int?
                    )
                }
            }
        }
    }

    data class Intent(
        val `$kind`: String?,
        val contentDescriptor: ContentDescriptor?,
        val prominentItemIdentifier: Any?
    ) {
        data class ContentDescriptor(
            val identifiers: Identifiers?,
            val kind: String?,
            val locale: Locale?
        ) {
            data class Identifiers(
                val storeAdamID: String?
            )

            data class Locale(
                val language: Any?,
                val storefront: String?
            )
        }
    }
}