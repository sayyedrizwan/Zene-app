package com.rizwansayyed.zene.data.model


typealias StoreDealResponseList = List<StoreDealResponse>

data class StoreStripeResponse(val url: String?)

data class StoreDealResponse(
    val name: String?,
    val items: List<StoreDealResponseItem>?,
) {
    data class StoreDealResponseItem(
        val title: String?,
        val image: String?,
        val dealPrice: Double?,
        val mrp: Double?,
        val currency: String?,
        val brand: String?,
        val discountPercent: Int?,
        val link: String?,
    )
}