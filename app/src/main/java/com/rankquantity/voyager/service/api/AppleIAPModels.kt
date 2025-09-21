package com.rankquantity.voyager.service.api

import com.google.gson.annotations.SerializedName

/**
 * Apple 收据验证请求
 */
data class AppleReceiptVerifyRequest(
    @SerializedName("receiptData") val receiptData: String,
    @SerializedName("sandbox") val sandbox: Boolean = false
)

/**
 * Apple 收据验证响应数据
 */
data class AppleReceiptVerifyData(
    @SerializedName("transactionId") val transactionId: String,
    @SerializedName("productId") val productId: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("purchaseDate") val purchaseDate: Long,
    @SerializedName("originalTransactionId") val originalTransactionId: String,
    @SerializedName("isTrialPeriod") val isTrialPeriod: Boolean,
    @SerializedName("isInIntroOfferPeriod") val isInIntroOfferPeriod: Boolean
)

/**
 * Apple 订阅状态请求
 */
data class AppleSubscriptionStatusRequest(
    @SerializedName("originalTransactionId") val originalTransactionId: String
)

/**
 * Apple 订阅状态数据
 */
data class AppleSubscriptionStatusData(
    @SerializedName("originalTransactionId") val originalTransactionId: String,
    @SerializedName("productId") val productId: String,
    @SerializedName("status") val status: String,
    @SerializedName("expiresDate") val expiresDate: Long,
    @SerializedName("autoRenewStatus") val autoRenewStatus: Boolean,
    @SerializedName("isTrialPeriod") val isTrialPeriod: Boolean,
    @SerializedName("isInIntroOfferPeriod") val isInIntroOfferPeriod: Boolean
)

/**
 * Apple 通知请求
 */
data class AppleNotificationRequest(
    @SerializedName("signedPayload") val signedPayload: String
)

/**
 * Apple 通知数据
 */
data class AppleNotificationData(
    @SerializedName("notificationType") val notificationType: String,
    @SerializedName("transactionId") val transactionId: String,
    @SerializedName("originalTransactionId") val originalTransactionId: String,
    @SerializedName("productId") val productId: String,
    @SerializedName("processed") val processed: Boolean,
    @SerializedName("message") val message: String
)
