package com.rankquantity.voyager.service.api

import com.google.gson.annotations.SerializedName

/**
 * VIPPay API 统一响应格式
 * @param T 响应数据类型
 */
data class VipPayResponse<T>(
    /** 业务状态码，0表示成功 */
    @SerializedName("code") val code: Int,
    /** 响应消息 */
    @SerializedName("msg") val msg: String,
    /** 响应数据 */
    @SerializedName("data") val data: T?,
    /** 时间戳 */
    @SerializedName("timestamp") val timestamp: Long? = null
)

/**
 * 健康检查数据
 */
data class HealthCheckData(
    @SerializedName("status") val status: String,
    @SerializedName("version") val version: String,
    @SerializedName("uptime") val uptime: Long,
    @SerializedName("timestamp") val timestamp: Long
)

/**
 * 支付平台枚举
 */
enum class PaymentPlatform(val value: String) {
    APPLE("apple"),
    GOOGLE("google")
}

/**
 * 产品类型枚举
 */
enum class ProductType(val value: String) {
    CONSUMABLE("consumable"),
    NON_CONSUMABLE("non_consumable"),
    SUBSCRIPTION("subscription")
}
