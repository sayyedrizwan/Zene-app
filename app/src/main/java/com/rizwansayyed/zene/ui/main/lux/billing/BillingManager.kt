package com.rizwansayyed.zene.ui.main.lux.billing

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.rizwansayyed.zene.R
import com.rizwansayyed.zene.utils.MainUtils.toast

class BillingManager(private val context: Activity) {

    var monthlyCost by mutableStateOf("$0.79")
    var yearlyCost by mutableStateOf("$8.99")
    var semiAnnualCost by mutableStateOf("$8.99")

    private val purchase = PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()


    private val purchasesListener = PurchasesUpdatedListener { billingResult, purchases ->
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                val purchaseToken = purchase.purchaseToken
                Log.d("TAG", "purcahse tokens: $purchaseToken")
            }
        }

    }


    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(purchasesListener)
        .enablePendingPurchases(purchase)
        .build()

    fun startConnection() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                    return
                }
                getSubscriptionPrices(listOf("monthly", "semiannual", "yearly"), false) { prices ->
                    monthlyCost = prices["monthly"] ?: "$0.79"
                    yearlyCost = prices["yearly"] ?: "$8.99"
                    semiAnnualCost = prices["semiannual"] ?: "$4.99"
                }
            }

            override fun onBillingServiceDisconnected() {
                context.resources.getString(R.string.error_connecting_to_play_service).toast()
            }
        })
    }

    fun buyMonthly() {
        getSubscriptionPrices(listOf("monthly"), true) {}
    }

    fun buySemiAnnual() {
        getSubscriptionPrices(listOf("semiannual"), true) {}
    }

    fun buyYearly() {
        getSubscriptionPrices(listOf("yearly"), true) {}
    }

    fun getSubscriptionPrices(
        productIds: List<String>,
        buy: Boolean,
        callback: (Map<String, String>) -> Unit
    ) {
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
                    if (buy) startBuying(productDetails)
                }
            }

            callback(result)
        }
    }

    private fun startBuying(productDetails: ProductDetails) {
        val token = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: ""
        val param = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(productDetails)
            .setOfferToken(token)
            .build()

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(param)).build()

        billingClient.launchBillingFlow(context, billingFlowParams)
    }
}