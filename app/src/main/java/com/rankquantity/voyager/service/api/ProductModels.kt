package com.rankquantity.voyager.service.api

import com.google.gson.annotations.SerializedName

/**
 * 产品信息
 */
data class Product(
    @SerializedName("id") val id: Int,
    @SerializedName("productId") val productId: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("type") val type: String,
    @SerializedName("platform") val platform: String,
    @SerializedName("price") val price: Double,
    @SerializedName("currency") val currency: String,
    @SerializedName("featured") val featured: Boolean,
    @SerializedName("active") val active: Boolean,
    @SerializedName("createdAt") val createdAt: Long,
    @SerializedName("updatedAt") val updatedAt: Long
)

/**
 * 产品列表数据
 */
data class ProductListData(
    @SerializedName("products") val products: List<Product>,
    @SerializedName("total") val total: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("pageSize") val pageSize: Int
)

/**
 * 产品详情数据
 */
data class ProductDetailData(
    @SerializedName("product") val product: Product,
    @SerializedName("purchaseCount") val purchaseCount: Int,
    @SerializedName("revenue") val revenue: Double,
    @SerializedName("rating") val rating: Double,
    @SerializedName("reviews") val reviews: Int
)

/**
 * 产品统计数据
 */
data class ProductStatsData(
    @SerializedName("totalProducts") val totalProducts: Int,
    @SerializedName("activeProducts") val activeProducts: Int,
    @SerializedName("totalRevenue") val totalRevenue: Double,
    @SerializedName("totalPurchases") val totalPurchases: Int,
    @SerializedName("topProducts") val topProducts: List<Product>
)
