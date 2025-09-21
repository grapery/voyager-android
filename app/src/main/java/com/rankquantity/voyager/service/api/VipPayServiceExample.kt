package com.rankquantity.voyager.service.api

import android.util.Log
import kotlinx.coroutines.*

/**
 * VipPayService 使用示例
 * 展示如何使用 VIPPay 支付系统服务
 */
class VipPayServiceExample {
    
    companion object {
        private const val TAG = "VipPayServiceExample"
    }
    
    /**
     * 健康检查示例
     */
    suspend fun checkHealthExample() {
        try {
            val healthData = VipPayService.getInstance().checkHealth()
            Log.d(TAG, "服务健康状态: ${healthData.status}")
            Log.d(TAG, "服务版本: ${healthData.version}")
            Log.d(TAG, "运行时间: ${healthData.uptime}ms")
        } catch (e: VipPayError) {
            Log.e(TAG, "健康检查失败: ${e.message}")
        }
    }
    
    /**
     * Apple IAP 验证示例
     */
    suspend fun appleIAPExample() {
        try {
            // 设置用户Token（实际使用时应该从登录接口获取）
            GlobalToken.setToken("your_user_token_here")
            
            // 验证 Apple 收据
            val receiptData = "base64_encoded_receipt_data"
            val verifyResult = VipPayService.getInstance().verifyAppleReceipt(
                receiptData = receiptData,
                sandbox = true // 沙盒环境
            )
            
            Log.d(TAG, "Apple 收据验证成功:")
            Log.d(TAG, "交易ID: ${verifyResult.transactionId}")
            Log.d(TAG, "产品ID: ${verifyResult.productId}")
            Log.d(TAG, "数量: ${verifyResult.quantity}")
            Log.d(TAG, "购买时间: ${verifyResult.purchaseDate}")
            
            // 获取订阅状态
            val subscriptionStatus = VipPayService.getInstance().getAppleSubscriptionStatus(
                verifyResult.originalTransactionId
            )
            
            Log.d(TAG, "Apple 订阅状态:")
            Log.d(TAG, "状态: ${subscriptionStatus.status}")
            Log.d(TAG, "过期时间: ${subscriptionStatus.expiresDate}")
            Log.d(TAG, "自动续费: ${subscriptionStatus.autoRenewStatus}")
            
        } catch (e: VipPayError) {
            when (e) {
                is VipPayError.Unauthorized -> Log.e(TAG, "未授权访问，请检查Token")
                is VipPayError.TokenExpired -> Log.e(TAG, "Token已过期，请重新登录")
                is VipPayError.ServerError -> Log.e(TAG, "服务器错误: ${e.statusCode} - ${e.message}")
                else -> Log.e(TAG, "Apple IAP 验证失败: ${e.message}")
            }
        }
    }
    
    /**
     * Google IAP 验证示例
     */
    suspend fun googleIAPExample() {
        try {
            // 设置用户Token
            GlobalToken.setToken("your_user_token_here")
            
            // 验证 Google 购买
            val purchaseToken = "google_purchase_token"
            val productId = "premium_subscription"
            
            val verifyResult = VipPayService.getInstance().verifyGooglePurchase(
                purchaseToken = purchaseToken,
                productId = productId
            )
            
            Log.d(TAG, "Google 购买验证成功:")
            Log.d(TAG, "订单ID: ${verifyResult.orderId}")
            Log.d(TAG, "产品ID: ${verifyResult.productId}")
            Log.d(TAG, "购买时间: ${verifyResult.purchaseTime}")
            Log.d(TAG, "购买状态: ${verifyResult.purchaseState}")
            
            // 获取订阅状态
            val subscriptionStatus = VipPayService.getInstance().getGoogleSubscriptionStatus(
                purchaseToken = purchaseToken,
                productId = productId
            )
            
            Log.d(TAG, "Google 订阅状态:")
            Log.d(TAG, "订阅ID: ${subscriptionStatus.subscriptionId}")
            Log.d(TAG, "自动续费: ${subscriptionStatus.autoRenewing}")
            Log.d(TAG, "过期时间: ${subscriptionStatus.expiryTimeMillis}")
            Log.d(TAG, "支付状态: ${subscriptionStatus.paymentState}")
            
        } catch (e: VipPayError) {
            Log.e(TAG, "Google IAP 验证失败: ${e.message}")
        }
    }
    
    /**
     * 产品管理示例
     */
    suspend fun productManagementExample() {
        try {
            // 获取产品列表
            val products = VipPayService.getInstance().getProducts(
                platform = PaymentPlatform.GOOGLE,
                type = ProductType.SUBSCRIPTION,
                featured = true
            )
            
            Log.d(TAG, "获取到 ${products.total} 个产品:")
            products.products.forEach { product ->
                Log.d(TAG, "产品: ${product.name} - ${product.price} ${product.currency}")
            }
            
            // 获取产品详情
            if (products.products.isNotEmpty()) {
                val productId = products.products.first().id
                val productDetail = VipPayService.getInstance().getProductDetail(productId)
                
                Log.d(TAG, "产品详情:")
                Log.d(TAG, "名称: ${productDetail.product.name}")
                Log.d(TAG, "描述: ${productDetail.product.description}")
                Log.d(TAG, "购买次数: ${productDetail.purchaseCount}")
                Log.d(TAG, "收入: ${productDetail.revenue}")
                Log.d(TAG, "评分: ${productDetail.rating}")
            }
            
        } catch (e: VipPayError) {
            Log.e(TAG, "产品管理失败: ${e.message}")
        }
    }
    
    /**
     * VIP 会员示例
     */
    suspend fun vipMemberExample() {
        try {
            // 设置用户Token
            GlobalToken.setToken("your_user_token_here")
            
            // 获取 VIP 信息
            val vipInfo = VipPayService.getInstance().getVIPInfo()
            
            Log.d(TAG, "VIP 信息:")
            Log.d(TAG, "是否VIP: ${vipInfo.isVip}")
            Log.d(TAG, "VIP等级: ${vipInfo.vipLevel}")
            Log.d(TAG, "VIP类型: ${vipInfo.vipType}")
            Log.d(TAG, "开始时间: ${vipInfo.startDate}")
            Log.d(TAG, "过期时间: ${vipInfo.expireDate}")
            Log.d(TAG, "剩余天数: ${vipInfo.remainingDays}")
            
            // 检查 VIP 状态
            val vipStatus = VipPayService.getInstance().checkVIPStatus()
            
            Log.d(TAG, "VIP 状态:")
            Log.d(TAG, "是否激活: ${vipStatus.isActive}")
            Log.d(TAG, "状态: ${vipStatus.status}")
            Log.d(TAG, "消息: ${vipStatus.message}")
            
            // 获取配额信息
            val quotaInfo = VipPayService.getInstance().getQuotaInfo()
            
            Log.d(TAG, "配额信息:")
            Log.d(TAG, "每日配额: ${quotaInfo.dailyQuota}")
            Log.d(TAG, "已使用: ${quotaInfo.usedQuota}")
            Log.d(TAG, "剩余: ${quotaInfo.remainingQuota}")
            Log.d(TAG, "重置时间: ${quotaInfo.resetTime}")
            
            // 获取最大角色数
            val maxRoles = VipPayService.getInstance().getMaxRoles()
            
            Log.d(TAG, "角色限制:")
            Log.d(TAG, "最大角色数: ${maxRoles.maxRoles}")
            Log.d(TAG, "当前角色数: ${maxRoles.currentRoles}")
            Log.d(TAG, "可创建更多: ${maxRoles.canCreateMore}")
            
            // 获取最大上下文数
            val maxContexts = VipPayService.getInstance().getMaxContexts()
            
            Log.d(TAG, "上下文限制:")
            Log.d(TAG, "最大上下文数: ${maxContexts.maxContexts}")
            Log.d(TAG, "当前上下文数: ${maxContexts.currentContexts}")
            Log.d(TAG, "可创建更多: ${maxContexts.canCreateMore}")
            
        } catch (e: VipPayError) {
            Log.e(TAG, "VIP 会员信息获取失败: ${e.message}")
        }
    }
    
    /**
     * 通用 IAP 操作示例
     */
    suspend fun commonIAPExample() {
        try {
            // 设置用户Token
            GlobalToken.setToken("your_user_token_here")
            
            val platform = PaymentPlatform.GOOGLE
            val purchaseToken = "purchase_token"
            val productId = "product_id"
            
            // 确认购买
            val acknowledgeResult = VipPayService.getInstance().acknowledgePurchase(
                platform = platform,
                purchaseToken = purchaseToken,
                productId = productId
            )
            
            Log.d(TAG, "确认购买结果: ${acknowledgeResult.acknowledged}")
            Log.d(TAG, "消息: ${acknowledgeResult.message}")
            
            // 消耗购买（仅适用于可消耗产品）
            val consumeResult = VipPayService.getInstance().consumePurchase(
                platform = platform,
                purchaseToken = purchaseToken,
                productId = productId
            )
            
            Log.d(TAG, "消耗购买结果: ${consumeResult.consumed}")
            Log.d(TAG, "消息: ${consumeResult.message}")
            
            // 同步订阅状态
            val syncResult = VipPayService.getInstance().syncSubscription(platform)
            
            Log.d(TAG, "同步订阅结果:")
            Log.d(TAG, "同步数量: ${syncResult.syncedCount}")
            Log.d(TAG, "总数量: ${syncResult.totalCount}")
            Log.d(TAG, "消息: ${syncResult.message}")
            
        } catch (e: VipPayError) {
            Log.e(TAG, "通用 IAP 操作失败: ${e.message}")
        }
    }
    
    /**
     * 错误处理示例
     */
    suspend fun errorHandlingExample() {
        try {
            // 故意使用无效的Token来演示错误处理
            GlobalToken.setToken("invalid_token")
            
            val vipInfo = VipPayService.getInstance().getVIPInfo()
            Log.d(TAG, "VIP 信息: $vipInfo")
            
        } catch (e: VipPayError) {
            when (e) {
                is VipPayError.Unauthorized -> {
                    Log.e(TAG, "未授权访问，需要重新登录")
                    // 跳转到登录页面
                }
                is VipPayError.TokenExpired -> {
                    Log.e(TAG, "Token已过期，需要刷新Token")
                    // 刷新Token或重新登录
                }
                is VipPayError.NetworkError -> {
                    Log.e(TAG, "网络错误: ${e.originalError.message}")
                    // 显示网络错误提示
                }
                is VipPayError.ServerError -> {
                    Log.e(TAG, "服务器错误 [${e.statusCode}]: ${e.message}")
                    // 显示服务器错误提示
                }
                is VipPayError.NoData -> {
                    Log.e(TAG, "无数据返回")
                    // 显示无数据提示
                }
                else -> {
                    Log.e(TAG, "未知错误: ${e.message}")
                    // 显示通用错误提示
                }
            }
        }
    }
}
