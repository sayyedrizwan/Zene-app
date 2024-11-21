package com.rizwansayyed.zene.ui.premium.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.google.common.collect.ImmutableList
import com.rizwansayyed.zene.di.BaseApp.Companion.context

class PremiumUtils : ViewModel() {

    var monthlyPricing by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    private val purchasesUpdatedListener = PurchasesUpdatedListener { _, _ -> }

    private val billingClient =
        BillingClient.newBuilder(context).setListener(purchasesUpdatedListener)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder().enableOneTimeProducts().build()
            ).build()


    fun start() {
        isLoading = true
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    getProductPriceDetails()
                }
            }

            override fun onBillingServiceDisconnected() {
                isLoading = false
            }
        })
    }

    fun getProductPriceDetails() {
        val monthly = QueryProductDetailsParams.Product.newBuilder().setProductId("monthly")
            .setProductType(BillingClient.ProductType.SUBS).build()
        val list = QueryProductDetailsParams.newBuilder().setProductList(ImmutableList.of(monthly))

        billingClient.queryProductDetailsAsync(list.build()) { _, details ->
            details.flatMap { it.subscriptionOfferDetails ?: emptyList() }
                .flatMap { it.pricingPhases.pricingPhaseList }.forEach {
                    if (it.billingPeriod == "P4W") monthlyPricing = it.formattedPrice
                }

            isLoading = false
        }
    }
}