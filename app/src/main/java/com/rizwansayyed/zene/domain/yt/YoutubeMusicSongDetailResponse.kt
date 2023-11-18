package com.rizwansayyed.zene.domain.yt

data class YoutubeMusicSongDetailResponse(
    val adBreakHeartbeatParams: String?,
    val adPlacements: List<AdPlacement?>?,
    val attestation: Attestation?,
    val microformat: Microformat?,
    val playabilityStatus: PlayabilityStatus?,
    val playbackTracking: PlaybackTracking?,
    val playerAds: List<PlayerAd?>?,
    val playerConfig: PlayerConfig?,
    val responseContext: ResponseContext?,
    val storyboards: Storyboards?,
    val streamingData: StreamingData?,
    val trackingParams: String?,
    val videoDetails: VideoDetails?
) {
    data class AdPlacement(
        val adPlacementRenderer: AdPlacementRenderer?
    ) {
        data class AdPlacementRenderer(
            val adSlotLoggingData: AdSlotLoggingData?,
            val config: Config?,
            val renderer: Renderer?
        ) {
            data class AdSlotLoggingData(
                val serializedSlotAdServingDataEntry: String?
            )

            data class Config(
                val adPlacementConfig: AdPlacementConfig?
            ) {
                data class AdPlacementConfig(
                    val adTimeOffset: AdTimeOffset?,
                    val hideCueRangeMarker: Boolean?,
                    val kind: String?
                ) {
                    data class AdTimeOffset(
                        val offsetEndMilliseconds: String?,
                        val offsetStartMilliseconds: String?
                    )
                }
            }

            data class Renderer(
                val adBreakServiceRenderer: AdBreakServiceRenderer?,
                val clientForecastingAdRenderer: ClientForecastingAdRenderer?
            ) {
                data class AdBreakServiceRenderer(
                    val getAdBreakUrl: String?,
                    val prefetchMilliseconds: String?
                )

                class ClientForecastingAdRenderer
            }
        }
    }

    data class Attestation(
        val playerAttestationRenderer: PlayerAttestationRenderer?
    ) {
        data class PlayerAttestationRenderer(
            val botguardData: BotguardData?,
            val challenge: String?
        ) {
            data class BotguardData(
                val interpreterSafeUrl: InterpreterSafeUrl?,
                val program: String?,
                val serverEnvironment: Int?
            ) {
                data class InterpreterSafeUrl(
                    val privateDoNotAccessOrElseTrustedResourceUrlWrappedValue: String?
                )
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
            val category: String?,
            val description: String?,
            val familySafe: Boolean?,
            val iosAppArguments: String?,
            val iosAppStoreId: String?,
            val linkAlternates: List<LinkAlternate?>?,
            val noindex: Boolean?,
            val ogType: String?,
            val pageOwnerDetails: PageOwnerDetails?,
            val paid: Boolean?,
            val publishDate: String?,
            val schemaDotOrgType: String?,
            val siteName: String?,
            val tags: List<String?>?,
            val thumbnail: Thumbnail?,
            val title: String?,
            val twitterCardType: String?,
            val twitterSiteHandle: String?,
            val unlisted: Boolean?,
            val uploadDate: String?,
            val urlApplinksAndroid: String?,
            val urlApplinksIos: String?,
            val urlCanonical: String?,
            val urlTwitterAndroid: String?,
            val urlTwitterIos: String?,
            val videoDetails: VideoDetails?,
            val viewCount: String?
        ) {
            data class LinkAlternate(
                val alternateType: String?,
                val hrefUrl: String?,
                val title: String?
            )

            data class PageOwnerDetails(
                val externalChannelId: String?,
                val name: String?,
                val youtubeProfileUrl: String?
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

            data class VideoDetails(
                val durationIso8601: String?,
                val durationSeconds: String?,
                val externalVideoId: String?
            )
        }
    }

    data class PlayabilityStatus(
        val audioOnlyPlayability: AudioOnlyPlayability?,
        val contextParams: String?,
        val miniplayer: Miniplayer?,
        val playableInEmbed: Boolean?,
        val status: String?
    ) {
        data class AudioOnlyPlayability(
            val audioOnlyPlayabilityRenderer: AudioOnlyPlayabilityRenderer?
        ) {
            data class AudioOnlyPlayabilityRenderer(
                val audioOnlyAvailability: String?,
                val trackingParams: String?
            )
        }

        data class Miniplayer(
            val miniplayerRenderer: MiniplayerRenderer?
        ) {
            data class MiniplayerRenderer(
                val playbackMode: String?
            )
        }
    }

    data class PlaybackTracking(
        val atrUrl: AtrUrl?,
        val ptrackingUrl: PtrackingUrl?,
        val qoeUrl: QoeUrl?,
        val videostatsDefaultFlushIntervalSeconds: Int?,
        val videostatsDelayplayUrl: VideostatsDelayplayUrl?,
        val videostatsPlaybackUrl: VideostatsPlaybackUrl?,
        val videostatsScheduledFlushWalltimeSeconds: List<Int?>?,
        val videostatsWatchtimeUrl: VideostatsWatchtimeUrl?
    ) {
        data class AtrUrl(
            val baseUrl: String?,
            val elapsedMediaTimeSeconds: Int?,
            val headers: List<Header?>?
        ) {
            data class Header(
                val headerType: String?
            )
        }

        data class PtrackingUrl(
            val baseUrl: String?,
            val headers: List<Header?>?
        ) {
            data class Header(
                val headerType: String?
            )
        }

        data class QoeUrl(
            val baseUrl: String?,
            val headers: List<Header?>?
        ) {
            data class Header(
                val headerType: String?
            )
        }

        data class VideostatsDelayplayUrl(
            val baseUrl: String?,
            val headers: List<Header?>?
        ) {
            data class Header(
                val headerType: String?
            )
        }

        data class VideostatsPlaybackUrl(
            val baseUrl: String?,
            val headers: List<Header?>?
        ) {
            data class Header(
                val headerType: String?
            )
        }

        data class VideostatsWatchtimeUrl(
            val baseUrl: String?,
            val headers: List<Header?>?
        ) {
            data class Header(
                val headerType: String?
            )
        }
    }

    data class PlayerAd(
        val playerLegacyDesktopWatchAdsRenderer: PlayerLegacyDesktopWatchAdsRenderer?
    ) {
        data class PlayerLegacyDesktopWatchAdsRenderer(
            val gutParams: GutParams?,
            val playerAdParams: PlayerAdParams?,
            val showCompanion: Boolean?,
            val showInstream: Boolean?,
            val useGut: Boolean?
        ) {
            data class GutParams(
                val tag: String?
            )

            data class PlayerAdParams(
                val enabledEngageTypes: String?,
                val showContentThumbnail: Boolean?
            )
        }
    }

    data class PlayerConfig(
        val audioConfig: AudioConfig?,
        val mediaCommonConfig: MediaCommonConfig?,
        val streamSelectionConfig: StreamSelectionConfig?,
        val webPlayerConfig: WebPlayerConfig?
    ) {
        data class AudioConfig(
            val enablePerFormatLoudness: Boolean?,
            val loudnessDb: Double?,
            val perceptualLoudnessDb: Double?
        )

        data class MediaCommonConfig(
            val dynamicReadaheadConfig: DynamicReadaheadConfig?
        ) {
            data class DynamicReadaheadConfig(
                val maxReadAheadMediaTimeMs: Int?,
                val minReadAheadMediaTimeMs: Int?,
                val readAheadGrowthRateMs: Int?
            )
        }

        data class StreamSelectionConfig(
            val maxBitrate: String?
        )

        data class WebPlayerConfig(
            val useCobaltTvosDash: Boolean?,
            val webPlayerActionsPorting: WebPlayerActionsPorting?
        ) {
            data class WebPlayerActionsPorting(
                val addToWatchLaterCommand: AddToWatchLaterCommand?,
                val removeFromWatchLaterCommand: RemoveFromWatchLaterCommand?,
                val subscribeCommand: SubscribeCommand?,
                val unsubscribeCommand: UnsubscribeCommand?
            ) {
                data class AddToWatchLaterCommand(
                    val clickTrackingParams: String?,
                    val playlistEditEndpoint: PlaylistEditEndpoint?
                ) {
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

                data class RemoveFromWatchLaterCommand(
                    val clickTrackingParams: String?,
                    val playlistEditEndpoint: PlaylistEditEndpoint?
                ) {
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

                data class SubscribeCommand(
                    val clickTrackingParams: String?,
                    val subscribeEndpoint: SubscribeEndpoint?
                ) {
                    data class SubscribeEndpoint(
                        val channelIds: List<String?>?,
                        val params: String?
                    )
                }

                data class UnsubscribeCommand(
                    val clickTrackingParams: String?,
                    val unsubscribeEndpoint: UnsubscribeEndpoint?
                ) {
                    data class UnsubscribeEndpoint(
                        val channelIds: List<String?>?,
                        val params: String?
                    )
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

    data class Storyboards(
        val playerStoryboardSpecRenderer: PlayerStoryboardSpecRenderer?
    ) {
        data class PlayerStoryboardSpecRenderer(
            val recommendedLevel: Int?,
            val spec: String?
        )
    }

    data class StreamingData(
        val adaptiveFormats: List<AdaptiveFormat?>?,
        val expiresInSeconds: String?,
        val formats: List<Format?>?
    ) {
        data class AdaptiveFormat(
            val approxDurationMs: String?,
            val audioChannels: Int?,
            val audioQuality: String?,
            val audioSampleRate: String?,
            val averageBitrate: Int?,
            val bitrate: Int?,
            val colorInfo: ColorInfo?,
            val contentLength: String?,
            val fps: Int?,
            val height: Int?,
            val highReplication: Boolean?,
            val indexRange: IndexRange?,
            val initRange: InitRange?,
            val itag: Int?,
            val lastModified: String?,
            val loudnessDb: Double?,
            val mimeType: String?,
            val projectionType: String?,
            val quality: String?,
            val qualityLabel: String?,
            val signatureCipher: String?,
            val width: Int?
        ) {
            data class ColorInfo(
                val matrixCoefficients: String?,
                val primaries: String?,
                val transferCharacteristics: String?
            )

            data class IndexRange(
                val end: String?,
                val start: String?
            )

            data class InitRange(
                val end: String?,
                val start: String?
            )
        }

        data class Format(
            val approxDurationMs: String?,
            val audioChannels: Int?,
            val audioQuality: String?,
            val audioSampleRate: String?,
            val averageBitrate: Int?,
            val bitrate: Int?,
            val contentLength: String?,
            val fps: Int?,
            val height: Int?,
            val itag: Int?,
            val lastModified: String?,
            val mimeType: String?,
            val projectionType: String?,
            val quality: String?,
            val qualityLabel: String?,
            val signatureCipher: String?,
            val width: Int?
        )
    }

    data class VideoDetails(
        val allowRatings: Boolean?,
        val author: String?,
        val channelId: String?,
        val isCrawlable: Boolean?,
        val isLiveContent: Boolean?,
        val isOwnerViewing: Boolean?,
        val isPrivate: Boolean?,
        val isUnpluggedCorpus: Boolean?,
        val lengthSeconds: String?,
        val musicVideoType: String?,
        val thumbnail: Thumbnail?,
        val title: String?,
        val videoId: String?,
        val viewCount: String?
    ) {
        fun thumbnailURL(): String? {
            return try {
                thumbnail?.thumbnails?.first()?.url
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