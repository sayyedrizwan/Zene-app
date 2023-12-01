package com.rizwansayyed.zene.domain.yt

import android.util.Log
import com.rizwansayyed.zene.data.utils.moshi
import com.rizwansayyed.zene.utils.Utils.artistsListToString

data class YoutubeMusicAllSongsResponse(
    val contents: Contents?,
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
                            val trackingParams: String?
                        ) {
                            data class Content(
                                val musicShelfRenderer: MusicShelfRendererSongs?
                            )
                        }
                    }
                }
            }
        }
    }
}

data class MusicShelfRendererSongs(
    val contents: List<Content?>?,
    val continuations: List<Continuation?>?,
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
            if (list.size == 0) {
                musicResponsiveListItemRenderer?.flexColumns?.forEachIndexed { index, flexColumn ->
                    if (index == 1) flexColumn?.musicResponsiveListItemFlexColumnRenderer
                        ?.text?.runs?.first()?.text?.let { list.add(it) }
                }
            }

            return artistsListToString(list)
        }

        fun theName(): String {
            return try {
                musicResponsiveListItemRenderer?.flexColumns?.first()?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.first()?.text
                    ?: ""
            } catch (e: Exception) {
                ""
            }
        }

        data class MusicResponsiveListItemRenderer(
            val flexColumnDisplayStyle: String?,
            val flexColumns: List<FlexColumn?>?,
            val itemHeight: String?,
            val navigationEndpoint: NavigationEndpoint?,
            val thumbnail: Thumbnail?,
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

            data class NavigationEndpoint(
                val browseEndpoint: NavigationBrowserEndpoint?
            ) {
                data class NavigationBrowserEndpoint(
                    val browseId: String?
                )

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
}