package com.rankquantity.voyager.service.api

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/**
 * VIPPay 支付系统服务类
 * 提供完整的 In-App Purchase (IAP) 支付功能，支持 Apple App Store 和 Google Play Store
 */
class VipPayService private constructor() {
    
    companion object {
        private const val TAG = "VipPayService"
        private const val BASE_URL = "http://192.168.1.7:8088/api/vippay"
        private const val REQUEST_TIMEOUT = 30L
        private const val MAX_RETRY_COUNT = 3
        
        @Volatile
        private var INSTANCE: VipPayService? = null
        
        /**
         * 获取单例实例
         */
        fun getInstance(): VipPayService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: VipPayService().also { INSTANCE = it }
            }
        }
    }
    
    // MARK: - 私有属性
    
    private val gson: Gson = GsonBuilder()
        .setLenient()
        .create()
    
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(createAuthInterceptor())
        .addInterceptor(createLoggingInterceptor())
        .build()
    
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    
    private val apiInterface = retrofit.create(VipPayApiInterface::class.java)
    
    // MARK: - 私有初始化方法
    
    private init() {
        Log.d(TAG, "VipPayService 初始化完成")
    }
    
    // MARK: - 私有辅助方法
    
    /**
     * 创建认证拦截器
     */
    private fun createAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
            
            // 添加认证头
            if (GlobalToken.hasValidToken()) {
                requestBuilder.addHeader("Authorization", "Bearer ${GlobalToken.userToken}")
            }
            
            // 添加通用请求头
            requestBuilder.addHeader("Content-Type", "application/json")
            requestBuilder.addHeader("Accept", "application/json")
            
            chain.proceed(requestBuilder.build())
        }
    }
    
    /**
     * 创建日志拦截器
     */
    private fun createLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Log.d(TAG, "HTTP: $message")
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    
    /**
     * 执行网络请求，带重试机制
     * @param request 请求函数
     * @param retryCount 重试次数
     * @return 响应数据
     */
    private suspend fun <T> executeRequestWithRetry(
        request: suspend () -> T,
        retryCount: Int = MAX_RETRY_COUNT
    ): T {
        return try {
            request()
        } catch (e: SocketTimeoutException) {
            Log.w(TAG, "网络超时，剩余重试次数: $retryCount")
            if (retryCount > 0) {
                delay((MAX_RETRY_COUNT - retryCount + 1) * 1000L)
                executeRequestWithRetry(request, retryCount - 1)
            } else {
                throw VipPayError.NetworkError(e)
            }
        } catch (e: Exception) {
            Log.e(TAG, "网络请求失败: ${e.message}")
            throw VipPayError.NetworkError(e)
        }
    }
    
    // MARK: - 健康检查
    
    /**
     * 检查服务健康状态
     * @return 健康检查数据
     */
    suspend fun checkHealth(): HealthCheckData {
        Log.d(TAG, "执行健康检查")
        return executeRequestWithRetry {
            try {
                val response = apiInterface.checkHealth()
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    // MARK: - Apple IAP 接口
    
    /**
     * 验证 Apple 收据
     * @param receiptData Base64 编码的收据数据
     * @param sandbox 是否为沙盒环境
     * @return 收据验证结果
     */
    suspend fun verifyAppleReceipt(
        receiptData: String,
        sandbox: Boolean = false
    ): AppleReceiptVerifyData {
        Log.d(TAG, "验证 Apple 收据")
        return executeRequestWithRetry {
            try {
                val request = AppleReceiptVerifyRequest(receiptData, sandbox)
                val response = apiInterface.verifyAppleReceipt(request)
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    /**
     * 获取 Apple 订阅状态
     * @param originalTransactionId 原始交易ID
     * @return 订阅状态信息
     */
    suspend fun getAppleSubscriptionStatus(
        originalTransactionId: String
    ): AppleSubscriptionStatusData {
        Log.d(TAG, "获取 Apple 订阅状态: $originalTransactionId")
        return executeRequestWithRetry {
            try {
                val request = AppleSubscriptionStatusRequest(originalTransactionId)
                val response = apiInterface.getAppleSubscriptionStatus(request)
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    /**
     * 处理 Apple 通知
     * @param signedPayload Apple 签名的通知数据
     * @return 通知处理结果
     */
    suspend fun handleAppleNotification(
        signedPayload: String
    ): AppleNotificationData {
        Log.d(TAG, "处理 Apple 通知")
        return executeRequestWithRetry {
            try {
                val request = AppleNotificationRequest(signedPayload)
                val response = apiInterface.handleAppleNotification(request)
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    // MARK: - Google IAP 接口
    
    /**
     * 验证 Google 购买
     * @param purchaseToken 购买令牌
     * @param productId 产品ID
     * @return 购买验证结果
     */
    suspend fun verifyGooglePurchase(
        purchaseToken: String,
        productId: String
    ): GooglePurchaseVerifyData {
        Log.d(TAG, "验证 Google 购买: $productId")
        return executeRequestWithRetry {
            try {
                val request = GooglePurchaseVerifyRequest(purchaseToken, productId)
                val response = apiInterface.verifyGooglePurchase(request)
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    /**
     * 获取 Google 订阅状态
     * @param purchaseToken 购买令牌
     * @param productId 产品ID
     * @return 订阅状态信息
     */
    suspend fun getGoogleSubscriptionStatus(
        purchaseToken: String,
        productId: String
    ): GoogleSubscriptionStatusData {
        Log.d(TAG, "获取 Google 订阅状态: $productId")
        return executeRequestWithRetry {
            try {
                val request = GoogleSubscriptionStatusRequest(purchaseToken, productId)
                val response = apiInterface.getGoogleSubscriptionStatus(request)
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    /**
     * 处理 Google 通知
     * @param version 通知版本
     * @param notificationType 通知类型
     * @param eventTimeMillis 事件时间戳
     * @param subscriptionId 订阅ID
     * @param packageName 包名
     * @return 通知处理结果
     */
    suspend fun handleGoogleNotification(
        version: String,
        notificationType: String,
        eventTimeMillis: Long,
        subscriptionId: String,
        packageName: String
    ): GoogleNotificationData {
        Log.d(TAG, "处理 Google 通知: $subscriptionId")
        return executeRequestWithRetry {
            try {
                val request = GoogleNotificationRequest(
                    version, notificationType, eventTimeMillis, subscriptionId, packageName
                )
                val response = apiInterface.handleGoogleNotification(request)
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    // MARK: - 通用 IAP 接口
    
    /**
     * 确认购买
     * @param platform 支付平台
     * @param purchaseToken 购买令牌
     * @param productId 产品ID
     * @return 确认购买结果
     */
    suspend fun acknowledgePurchase(
        platform: PaymentPlatform,
        purchaseToken: String,
        productId: String
    ): AcknowledgePurchaseData {
        Log.d(TAG, "确认购买: $platform - $productId")
        return executeRequestWithRetry {
            try {
                val request = AcknowledgePurchaseRequest(platform.value, purchaseToken, productId)
                val response = apiInterface.acknowledgePurchase(request)
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    /**
     * 消耗购买
     * @param platform 支付平台
     * @param purchaseToken 购买令牌
     * @param productId 产品ID
     * @return 消耗购买结果
     */
    suspend fun consumePurchase(
        platform: PaymentPlatform,
        purchaseToken: String,
        productId: String
    ): ConsumePurchaseData {
        Log.d(TAG, "消耗购买: $platform - $productId")
        return executeRequestWithRetry {
            try {
                val request = ConsumePurchaseRequest(platform.value, purchaseToken, productId)
                val response = apiInterface.consumePurchase(request)
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    /**
     * 同步订阅状态
     * @param platform 支付平台
     * @return 同步结果
     */
    suspend fun syncSubscription(platform: PaymentPlatform): SyncSubscriptionData {
        Log.d(TAG, "同步订阅状态: $platform")
        return executeRequestWithRetry {
            try {
                val request = SyncSubscriptionRequest(platform.value)
                val response = apiInterface.syncSubscription(request)
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    // MARK: - 产品管理接口
    
    /**
     * 获取产品列表
     * @param platform 支付平台
     * @param type 产品类型（可选）
     * @param featured 是否只获取推荐产品（可选）
     * @return 产品列表
     */
    suspend fun getProducts(
        platform: PaymentPlatform,
        type: ProductType? = null,
        featured: Boolean? = null
    ): ProductListData {
        Log.d(TAG, "获取产品列表: $platform")
        return executeRequestWithRetry {
            try {
                val response = apiInterface.getProducts(
                    platform.value,
                    type?.value,
                    featured
                )
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    /**
     * 获取产品详情
     * @param productId 产品ID
     * @return 产品详情
     */
    suspend fun getProductDetail(productId: Int): ProductDetailData {
        Log.d(TAG, "获取产品详情: $productId")
        return executeRequestWithRetry {
            try {
                val response = apiInterface.getProductDetail(productId)
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    /**
     * 获取产品统计
     * @param platform 支付平台
     * @return 产品统计信息
     */
    suspend fun getProductStats(platform: PaymentPlatform): ProductStatsData {
        Log.d(TAG, "获取产品统计: $platform")
        return executeRequestWithRetry {
            try {
                val response = apiInterface.getProductStats(platform.value)
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    // MARK: - VIP 会员接口
    
    /**
     * 获取 VIP 信息
     * @return VIP 会员信息
     */
    suspend fun getVIPInfo(): VIPInfo {
        Log.d(TAG, "获取 VIP 信息")
        return executeRequestWithRetry {
            try {
                val response = apiInterface.getVIPInfo()
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    /**
     * 检查 VIP 状态
     * @return VIP 状态信息
     */
    suspend fun checkVIPStatus(): VIPStatusData {
        Log.d(TAG, "检查 VIP 状态")
        return executeRequestWithRetry {
            try {
                val response = apiInterface.checkVIPStatus()
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    /**
     * 获取配额信息
     * @return 配额使用情况
     */
    suspend fun getQuotaInfo(): QuotaInfo {
        Log.d(TAG, "获取配额信息")
        return executeRequestWithRetry {
            try {
                val response = apiInterface.getQuotaInfo()
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    /**
     * 获取最大角色数
     * @return 最大角色数信息
     */
    suspend fun getMaxRoles(): MaxRolesData {
        Log.d(TAG, "获取最大角色数")
        return executeRequestWithRetry {
            try {
                val response = apiInterface.getMaxRoles()
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
    
    /**
     * 获取最大上下文数
     * @return 最大上下文数信息
     */
    suspend fun getMaxContexts(): MaxContextsData {
        Log.d(TAG, "获取最大上下文数")
        return executeRequestWithRetry {
            try {
                val response = apiInterface.getMaxContexts()
                if (response.code != 0) {
                    throw VipPayError.ServerError(response.code, response.msg)
                }
                response.data ?: throw VipPayError.NoData
            } catch (e: VipPayError) {
                throw e
            } catch (e: Exception) {
                throw VipPayError.ParseError(e)
            }
        }
    }
}