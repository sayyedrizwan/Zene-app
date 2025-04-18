package com.rizwansayyed.zene.ui.main.lux

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.QueryProductDetailsParams
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.utils.MainUtils.toast

class BillingManager(private val context: Context) {

    private var monthlyCost by mutableStateOf("$0.79")
    private var yearlyCost by mutableStateOf("$8.99")

    private val purchase = PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener { billingResult, purchases ->
        }
        .enablePendingPurchases(purchase)
        .build()

    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                    return
                }
                getSubscriptionPrices(listOf("monthly", "yearly")) { prices ->
                    monthlyCost = prices["monthly"] ?: "$0.79"
                    yearlyCost = prices["yearly"] ?: "$8.99"
                }
            }

            override fun onBillingServiceDisconnected() {
                context.resources.getString(R.string.error_connecting_to_play_service).toast()
            }
        })
    }

    fun getSubscriptionPrices(productIds: List<String>, callback: (Map<String, String>) -> Unit) {
        val productList = productIds.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        }

        val queryParams = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(queryParams) { billingResult, productDetailsList ->
            val result = mutableMapOf<String, String>()

            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (productDetails in productDetailsList) {
                    val offer = productDetails.subscriptionOfferDetails?.firstOrNull()
                    val pricingPhase = offer?.pricingPhases?.pricingPhaseList?.firstOrNull()
                    val formattedPrice = pricingPhase?.formattedPrice ?: "Unavailable"
                    result[productDetails.productId] = formattedPrice
                }
            }

            callback(result)
        }
    }

}