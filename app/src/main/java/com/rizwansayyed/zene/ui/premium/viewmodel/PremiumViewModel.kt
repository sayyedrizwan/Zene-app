package com.rizwansayyed.zene.ui.premium.viewmodel

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.google.common.collect.ImmutableList
import com.rizwansayyed.zene.data.api.zene.ZeneAPIInterface
import com.rizwansayyed.zene.di.BaseApp.Companion.context
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(private val zeneAPI: ZeneAPIInterface) : ViewModel() {

    var monthlyPricing by mutableStateOf("")
    var yearlyPricing by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var purchase by mutableStateOf<Purchase?>(null)

    private val purchasesUpdatedListener = PurchasesUpdatedListener { _, _ -> }

    private val billingClient =
        BillingClient.newBuilder(context).setListener(purchasesUpdatedListener)
            .setListener { _, purchases ->
                purchases?.forEach { p ->
                    purchase = p
                }
            }.enablePendingPurchases(
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
                    if (it.billingPeriod == "P1Y") yearlyPricing = it.formattedPrice
                }
            isLoading = false
        }
    }

    fun buySubscription(context: Activity, isYearly: Boolean) {
        val yearly = QueryProductDetailsParams.Product.newBuilder().setProductId("monthly")
            .setProductType(BillingClient.ProductType.SUBS).build()

        val list = QueryProductDetailsParams.newBuilder().setProductList(ImmutableList.of(yearly))

        val billingPeriod = if (isYearly) "P1Y" else "P4W"

        billingClient.queryProductDetailsAsync(list.build()) { _, details ->
            details.forEach { info ->
                info.subscriptionOfferDetails?.forEach { s ->
                    s.pricingPhases.pricingPhaseList.forEach { p ->
                        if (p.billingPeriod == billingPeriod) {
                            val productDetailsParamsList = listOf(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                    .setProductDetails(info).setOfferToken(s.offerToken).build()
                            )

                            val billingFlowParams = BillingFlowParams.newBuilder()
                                .setProductDetailsParamsList(productDetailsParamsList).build()
                            billingClient.launchBillingFlow(context, billingFlowParams)
                        }
                    }
                }
            }
        }
    }
}