package com.rankquantity.voyager.service.api

import com.google.gson.annotations.SerializedName

/**
 * 确认购买请求
 */
data class AcknowledgePurchaseRequest(
    @SerializedName("platform") val platform: String,
    @SerializedName("purchaseToken") val purchaseToken: String,
    @SerializedName("productId") val productId: String
)

/**
 * 确认购买数据
 */
data class AcknowledgePurchaseData(
    @SerializedName("acknowledged") val acknowledged: Boolean,
    @SerializedName("message") val message: String
)

/**
 * 消耗购买请求
 */
data class ConsumePurchaseRequest(
    @SerializedName("platform") val platform: String,
    @SerializedName("purchaseToken") val purchaseToken: String,
    @SerializedName("productId") val productId: String
)

/**
 * 消耗购买数据
 */
data class ConsumePurchaseData(
    @SerializedName("consumed") val consumed: Boolean,
    @SerializedName("message") val message: String
)

/**
 * 同步订阅请求
 */
data class SyncSubscriptionRequest(
    @SerializedName("platform") val platform: String
)

/**
 * 同步订阅数据
 */
data class SyncSubscriptionData(
    @SerializedName("syncedCount") val syncedCount: Int,
    @SerializedName("totalCount") val totalCount: Int,
    @SerializedName("message") val message: String
)
