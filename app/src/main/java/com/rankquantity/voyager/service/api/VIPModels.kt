package com.rankquantity.voyager.service.api

import com.google.gson.annotations.SerializedName

/**
 * VIP 信息
 */
data class VIPInfo(
    @SerializedName("isVip") val isVip: Boolean,
    @SerializedName("vipLevel") val vipLevel: Int,
    @SerializedName("vipType") val vipType: String,
    @SerializedName("startDate") val startDate: Long,
    @SerializedName("expireDate") val expireDate: Long,
    @SerializedName("autoRenew") val autoRenew: Boolean,
    @SerializedName("remainingDays") val remainingDays: Int
)

/**
 * VIP 状态数据
 */
data class VIPStatusData(
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("expiresAt") val expiresAt: Long
)

/**
 * 配额信息
 */
data class QuotaInfo(
    @SerializedName("dailyQuota") val dailyQuota: Int,
    @SerializedName("usedQuota") val usedQuota: Int,
    @SerializedName("remainingQuota") val remainingQuota: Int,
    @SerializedName("resetTime") val resetTime: Long,
    @SerializedName("quotaType") val quotaType: String
)

/**
 * 最大角色数数据
 */
data class MaxRolesData(
    @SerializedName("maxRoles") val maxRoles: Int,
    @SerializedName("currentRoles") val currentRoles: Int,
    @SerializedName("canCreateMore") val canCreateMore: Boolean
)

/**
 * 最大上下文数数据
 */
data class MaxContextsData(
    @SerializedName("maxContexts") val maxContexts: Int,
    @SerializedName("currentContexts") val currentContexts: Int,
    @SerializedName("canCreateMore") val canCreateMore: Boolean
)
