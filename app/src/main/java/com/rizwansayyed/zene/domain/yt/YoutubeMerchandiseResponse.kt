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
                        val videoPrimaryInfoRenderer: Any?,
                        val videoSecondaryInfoRenderer: Any?
                    ) {
                        data class MerchandiseShelfRenderer(
                            val hideText: String?,
                            val items: List<Item?>?,
                            val shelfType: String?,
                            val showText: String?,
                            val title: String?,
                            val trackingParams: String?
                        ) {
                            data class Item(
                                val merchandiseItemRenderer: MerchandiseItemRenderer?
                            )
                        }
                    }
                }
            }
        }
    }
}

data class MerchandiseItemRenderer(
    val buttonCommand: ButtonCommand?,
    val buttonText: String?,
    val description: String?,
    val price: String?,
    val thumbnail: Thumbnail?,
    val title: String?
) {
    data class ButtonCommand(
        val clickTrackingParams: String?,
        val commandExecutorCommand: CommandExecutorCommand?
    ) {
        data class CommandExecutorCommand(
            val commands: List<Command?>?
        ) {
            data class Command(
                val clickTrackingParams: String?,
                val commandMetadata: CommandMetadata?
            ) {
                data class CommandMetadata(
                    val webCommandMetadata: WebCommandMetadata?
                ) {
                    data class WebCommandMetadata(
                        val apiUrl: String?,
                        val rootVe: Int?,
                        val sendPost: Boolean?,
                        val url: String?,
                        val webPageType: String?
                    )
                }
            }
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