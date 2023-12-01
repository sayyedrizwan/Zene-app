package com.rizwansayyed.zene.domain.yt

import android.util.Log
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.artistsListToString

data class YoutubeMusicMainSearchResponse(
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
                                val musicShelfRenderer: MusicShelfRenderer?
                            ) {

                                data class MusicShelfRenderer(
                                    val contents: List<Content?>?,
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
                                                            if (a.lowercase().contains(" and ") ||
                                                                a.lowercase().contains(" & ")
                                                            ) list.add(a)
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        if (list.size == 0) {
                                            contents?.first()?.musicResponsiveListItemRenderer?.flexColumns?.forEachIndexed { index, flexColumn ->
                                                if (index == 1) flexColumn?.musicResponsiveListItemFlexColumnRenderer
                                                    ?.text?.runs?.first()?.text?.let { list.add(it) }
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

                                    fun getArtistsId(): String? {
                                        var id: String? = null
                                        contents?.forEachIndexed { index, content ->
                                            if (index == 0) id =
                                                content?.musicResponsiveListItemRenderer?.navigationEndpoint?.browseEndpoint?.browseId
                                        }

                                        return id
                                    }

                                    data class Content(
                                        val musicResponsiveListItemRenderer: MusicResponsiveListItemRendererMainSearch?
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
                }
            }
        }
    }
}


data class MusicResponsiveListItemRendererMainSearch(
    val flexColumnDisplayStyle: String?,
    val flexColumns: List<FlexColumn?>?,
    val itemHeight: String?,
    val navigationEndpoint: NavigationEndpoint?,
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