package com.rizwansayyed.zene.data.onlinesongs.youtube.implementation.userplaylist


import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.userIpDetails
import com.rizwansayyed.zene.data.db.datastore.DataStorageManager.ytMusicToken
import com.rizwansayyed.zene.data.onlinesongs.youtube.YoutubeMusicPlaylistService
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicBrowseSuggestJsonBody
import com.rizwansayyed.zene.data.utils.YoutubeAPI.ytMusicUserPlaylistJsonBody
import com.rizwansayyed.zene.data.utils.config.RemoteConfigInterface
import com.rizwansayyed.zene.domain.ImportPlaylistInfoData
import com.rizwansayyed.zene.domain.ImportPlaylistTrackInfoData
import com.rizwansayyed.zene.presenter.ui.home.mymusic.playlistimport.PlaylistImportersType
import com.rizwansayyed.zene.utils.Utils
import com.rizwansayyed.zene.utils.Utils.artistsListToString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class YoutubeMusicPlaylistImpl @Inject constructor(
    private val youtubeMusic: YoutubeMusicPlaylistService,
    private val remoteConfig: RemoteConfigInterface
) : YoutubeMusicPlaylistImplInterface {
    override suspend fun usersPlaylists() = flow {
        val list = mutableListOf<ImportPlaylistInfoData>()
        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()
        val token = ytMusicToken.first()

        val playlist = youtubeMusic.youtubePlaylistsResponse(
            token?.authorization, token?.cookie, ytMusicUserPlaylistJsonBody(ip), key?.music ?: ""
        )

        playlist.contents?.singleColumnBrowseResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { content ->
            content?.gridRenderer?.items?.forEach { items ->
                val thumbnail = items?.musicTwoRowItemRenderer?.thumbnailRenderer
                    ?.musicThumbnailRenderer?.thumbnail?.thumbnails?.maxBy { it?.height ?: 2 }?.url
                val title =
                    items?.musicTwoRowItemRenderer?.title?.runs?.map { it?.text }?.joinToString(" ")
                val playlistId =
                    items?.musicTwoRowItemRenderer?.title?.runs?.first()?.navigationEndpoint?.browseEndpoint?.browseId

                if (playlistId != null) {
                    val d = ImportPlaylistInfoData(
                        thumbnail, title, "", playlistId, PlaylistImportersType.YOUTUBE_MUSIC
                    )

                    list.add(d)
                }
            }
        }
        emit(list)
    }.flowOn(Dispatchers.IO)

    override suspend fun playlistsTracker(id: String) = flow {
        val list = mutableListOf<ImportPlaylistTrackInfoData>()
        val ip = userIpDetails.first()
        val key = remoteConfig.allApiKeys()
        val token = ytMusicToken.first()

        val tracks = youtubeMusic.youtubePlaylistsTracksResponse(
            token?.authorization, token?.cookie,
            ytMusicBrowseSuggestJsonBody(ip, id), key?.music ?: ""
        )

        tracks.contents?.singleColumnBrowseResultsRenderer?.tabs?.first()?.tabRenderer?.content?.sectionListRenderer?.contents?.first()?.musicPlaylistShelfRenderer?.contents?.forEach {
            val thumbnail = it?.musicResponsiveListItemRenderer?.thumbnail
                ?.musicThumbnailRenderer?.thumbnail?.thumbnails?.maxBy { i -> i?.height ?: 2 }?.url
            val title =
                it?.musicResponsiveListItemRenderer?.flexColumns?.first()?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.first()?.text
            val artists = mutableListOf<String>()
            it?.musicResponsiveListItemRenderer?.flexColumns?.forEach { a ->
                a?.musicResponsiveListItemFlexColumnRenderer?.text?.runs?.forEach { r ->
                    if (r?.navigationEndpoint?.watchEndpoint?.watchEndpointMusicSupportedConfigs?.watchEndpointMusicConfig?.musicVideoType == "MUSIC_PAGE_TYPE_ARTIST" ||
                        r?.navigationEndpoint?.browseEndpoint?.browseEndpointContextSupportedConfigs?.browseEndpointContextMusicConfig?.pageType == "MUSIC_PAGE_TYPE_ARTIST"
                    ) artists.add(r.text ?: "")
                }

            }
            list.add(ImportPlaylistTrackInfoData(thumbnail, artistsListToString(artists), title))
        }

        emit(list)
    }.flowOn(Dispatchers.IO)
}
