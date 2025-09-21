package com.rankquantity.voyager.service.api

import com.google.gson.annotations.SerializedName

/**
 * Google 购买验证请求
 */
data class GooglePurchaseVerifyRequest(
    @SerializedName("purchaseToken") val purchaseToken: String,
    @SerializedName("productId") val productId: String
)

/**
 * Google 购买验证数据
 */
data class GooglePurchaseVerifyData(
    @SerializedName("orderId") val orderId: String,
    @SerializedName("purchaseToken") val purchaseToken: String,
    @SerializedName("productId") val productId: String,
    @SerializedName("purchaseTime") val purchaseTime: Long,
    @SerializedName("purchaseState") val purchaseState: Int,
    @SerializedName("acknowledgementState") val acknowledgementState: Int,
    @SerializedName("consumptionState") val consumptionState: Int
)

/**
 * Google 订阅状态请求
 */
data class GoogleSubscriptionStatusRequest(
    @SerializedName("purchaseToken") val purchaseToken: String,
    @SerializedName("productId") val productId: String
)

/**
 * Google 订阅状态数据
 */
data class GoogleSubscriptionStatusData(
    @SerializedName("subscriptionId") val subscriptionId: String,
    @SerializedName("productId") val productId: String,
    @SerializedName("purchaseToken") val purchaseToken: String,
    @SerializedName("autoRenewing") val autoRenewing: Boolean,
    @SerializedName("priceCurrencyCode") val priceCurrencyCode: String,
    @SerializedName("priceAmountMicros") val priceAmountMicros: Long,
    @SerializedName("countryCode") val countryCode: String,
    @SerializedName("paymentState") val paymentState: Int,
    @SerializedName("startTimeMillis") val startTimeMillis: Long,
    @SerializedName("expiryTimeMillis") val expiryTimeMillis: Long,
    @SerializedName("userCancellationTimeMillis") val userCancellationTimeMillis: Long?,
    @SerializedName("cancelReason") val cancelReason: Int?,
    @SerializedName("orderId") val orderId: String,
    @SerializedName("linkedPurchaseToken") val linkedPurchaseToken: String?,
    @SerializedName("orderType") val orderType: Int
)

/**
 * Google 通知请求
 */
data class GoogleNotificationRequest(
    @SerializedName("version") val version: String,
    @SerializedName("notificationType") val notificationType: String,
    @SerializedName("eventTimeMillis") val eventTimeMillis: Long,
    @SerializedName("subscriptionId") val subscriptionId: String,
    @SerializedName("packageName") val packageName: String
)

/**
 * Google 通知数据
 */
data class GoogleNotificationData(
    @SerializedName("notificationType") val notificationType: String,
    @SerializedName("subscriptionId") val subscriptionId: String,
    @SerializedName("processed") val processed: Boolean,
    @SerializedName("message") val message: String
)
