package com.rizwansayyed.zene.presenter.converter

import android.util.Log
import com.rizwansayyed.zene.presenter.model.AlbumsHeadersResponse
import com.rizwansayyed.zene.presenter.model.MusicAlbumsItem
import com.rizwansayyed.zene.presenter.model.MusicsHeader
import com.rizwansayyed.zene.presenter.model.MusicHeaderAlbumResponse
import com.rizwansayyed.zene.presenter.model.MusicsAlbum
import com.rizwansayyed.zene.presenter.model.Thumbnail13
import com.rizwansayyed.zene.utils.Utils.moshi
import com.rizwansayyed.zene.utils.Utils.showToast

class SongsAlbumsHeaderConverter(jsonData: String) {

    private var json: MusicHeaderAlbumResponse? =
        moshi.adapter(MusicHeaderAlbumResponse::class.java).fromJson(jsonData)

    fun get(): AlbumsHeadersResponse {
        val headers = makeHeaderData()
        val albums = makeAlbumsData()

        return AlbumsHeadersResponse(albums, headers)
    }

    private fun makeAlbumsData(): ArrayList<MusicsAlbum> {
        val albumsLists = ArrayList<MusicsAlbum>(30)
        json?.contents?.twoColumnBrowseResultsRenderer?.tabs?.get(0)?.tabRenderer?.content?.sectionListRenderer?.contents?.forEach { album ->
            album.itemSectionRenderer?.contents?.forEach { albumItems ->
                val itemsLists = ArrayList<MusicAlbumsItem>(30)

                val parentName = albumItems.shelfRenderer?.title?.runs?.get(0)?.text
                albumItems.shelfRenderer?.content?.horizontalListRenderer?.items?.forEach { items ->
                    val title = items.compactStationRenderer?.title?.simpleText
                    val desc = items.compactStationRenderer?.description?.simpleText
                    val playID =
                        items.compactStationRenderer?.navigationEndpoint?.watchEndpoint?.playlistId
                    val thumbnail = greaterThumbnail(
                        items.compactStationRenderer?.thumbnail?.thumbnails ?: emptyList()
                    )
                    itemsLists.add(MusicAlbumsItem(title, desc, playID, thumbnail))
                }

                albumsLists.add(MusicsAlbum(parentName, itemsLists))
            }
        }

        return albumsLists
    }

    private fun makeHeaderData(): ArrayList<MusicsHeader> {
        val headerList = ArrayList<MusicsHeader>(6)
        json?.header?.carouselHeaderRenderer?.contents?.get(0)?.carouselItemRenderer?.carouselItems?.forEach { header ->
            val title = header.defaultPromoPanelRenderer?.title?.runs?.get(0)?.text
            var views = ""
            var thumbnail = ""
            header.defaultPromoPanelRenderer?.description?.runs?.forEach { v ->
                if (v.text?.lowercase()?.contains("views") == true) views = v.text.lowercase()
            }

            if (header.defaultPromoPanelRenderer?.largeFormFactorBackgroundThumbnail?.thumbnailLandscapePortraitRenderer?.landscape != null) {
                val t =
                    header.defaultPromoPanelRenderer.largeFormFactorBackgroundThumbnail.thumbnailLandscapePortraitRenderer.landscape.thumbnails
                thumbnail = greaterThumbnail(t ?: emptyList())
            } else if (header.defaultPromoPanelRenderer?.largeFormFactorBackgroundThumbnail?.thumbnailLandscapePortraitRenderer?.portrait != null) {
                val t =
                    header.defaultPromoPanelRenderer.largeFormFactorBackgroundThumbnail.thumbnailLandscapePortraitRenderer.portrait.thumbnails
                thumbnail = greaterThumbnail(t ?: emptyList())
            }
            headerList.add(MusicsHeader(title, thumbnail, views))
        }

        return headerList
    }


    private fun greaterThumbnail(albumsThumbnails: List<Thumbnail13>): String {
        var greatestWidth = 0
        var greatestHeight = 0
        var greatestThumbnailURL = ""

        albumsThumbnails.forEach { thumbnail ->
            if (thumbnail.width!! > greatestWidth) {
                greatestWidth = thumbnail.width.toInt()
                greatestThumbnailURL = thumbnail.url.toString()
            }

            if (thumbnail.height!! > greatestHeight) {
                greatestHeight = thumbnail.height.toInt()
                greatestThumbnailURL = thumbnail.url.toString()
            }
        }
        return greatestThumbnailURL
    }
}