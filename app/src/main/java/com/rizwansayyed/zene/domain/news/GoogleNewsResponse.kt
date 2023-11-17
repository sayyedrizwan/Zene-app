package com.rizwansayyed.zene.domain.news

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
data class GoogleNewsResponse constructor(
    @field:Element(name = "channel")
    var channel: Channel? = null
) {

    @Root(name = "channel", strict = false)
    data class Channel constructor(
        @field:ElementList(name = "item", inline = true)
        var items: List<Item>? = null
    ) {
        @Root(name = "item", strict = false)
        data class Item constructor(
            @field:Element(name = "title")
            var title: String? = null,

            @field:Element(name = "link")
            var link: String? = null,

            @field:Element(name = "guid")
            var guid: String? = null,

            @field:Element(name = "pubDate")
            var pubDate: String? = null,

            @field:Element(name = "description")
            var description: String? = null,

            @field:Element(name = "source")
            var source: Source? = null
        ) {
            @Root(name = "source", strict = false)
            data class Source constructor(
                @field:Attribute(name = "url")
                var url: String? = null
            )
        }
    }
}
