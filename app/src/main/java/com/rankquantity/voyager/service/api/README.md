# VipPayService - VIP 支付系统服务

## 概述

VipPayService 是一个完整的 In-App Purchase (IAP) 支付系统服务，支持 Apple App Store 和 Google Play Store 的支付功能。该服务提供了统一的 API 接口来处理支付验证、订阅管理、产品查询和 VIP 会员功能。

## 特性

- ✅ **单例模式** - 全局唯一的服务实例
- ✅ **网络重试机制** - 自动处理网络超时和重试
- ✅ **认证管理** - 自动添加用户Token到请求头
- ✅ **错误处理** - 完整的错误类型定义和处理
- ✅ **日志记录** - 详细的请求和响应日志
- ✅ **协程支持** - 基于 Kotlin 协程的异步操作
- ✅ **类型安全** - 完整的类型定义和数据模型

## 依赖

确保在 `build.gradle.kts` 中添加以下依赖：

```kotlin
// 网络请求相关依赖
implementation(libs.retrofit)
implementation(libs.retrofit.gson)
implementation(libs.okhttp)
implementation(libs.okhttp.logging)
implementation(libs.gson)
implementation(libs.kotlinx.coroutines.core)
implementation(libs.kotlinx.coroutines.android)
```

## 快速开始

### 1. 获取服务实例

```kotlin
val vipPayService = VipPayService.getInstance()
```

### 2. 设置用户Token

```kotlin
GlobalToken.setToken("your_user_token_here")
```

### 3. 执行操作

```kotlin
// 在协程中执行
lifecycleScope.launch {
    try {
        val healthData = vipPayService.checkHealth()
        Log.d("VipPay", "服务状态: ${healthData.status}")
    } catch (e: VipPayError) {
        Log.e("VipPay", "操作失败: ${e.message}")
    }
}
```

## API 接口

### 健康检查

```kotlin
suspend fun checkHealth(): HealthCheckData
```

### Apple IAP 接口

```kotlin
// 验证 Apple 收据
suspend fun verifyAppleReceipt(
    receiptData: String,
    sandbox: Boolean = false
): AppleReceiptVerifyData

// 获取 Apple 订阅状态
suspend fun getAppleSubscriptionStatus(
    originalTransactionId: String
): AppleSubscriptionStatusData

// 处理 Apple 通知
suspend fun handleAppleNotification(
    signedPayload: String
): AppleNotificationData
```

### Google IAP 接口

```kotlin
// 验证 Google 购买
suspend fun verifyGooglePurchase(
    purchaseToken: String,
    productId: String
): GooglePurchaseVerifyData

// 获取 Google 订阅状态
suspend fun getGoogleSubscriptionStatus(
    purchaseToken: String,
    productId: String
): GoogleSubscriptionStatusData

// 处理 Google 通知
suspend fun handleGoogleNotification(
    version: String,
    notificationType: String,
    eventTimeMillis: Long,
    subscriptionId: String,
    packageName: String
): GoogleNotificationData
```

### 通用 IAP 接口

```kotlin
// 确认购买
suspend fun acknowledgePurchase(
    platform: PaymentPlatform,
    purchaseToken: String,
    productId: String
): AcknowledgePurchaseData

// 消耗购买
suspend fun consumePurchase(
    platform: PaymentPlatform,
    purchaseToken: String,
    productId: String
): ConsumePurchaseData

// 同步订阅状态
suspend fun syncSubscription(platform: PaymentPlatform): SyncSubscriptionData
```

### 产品管理接口

```kotlin
// 获取产品列表
suspend fun getProducts(
    platform: PaymentPlatform,
    type: ProductType? = null,
    featured: Boolean? = null
): ProductListData

// 获取产品详情
suspend fun getProductDetail(productId: Int): ProductDetailData

// 获取产品统计
suspend fun getProductStats(platform: PaymentPlatform): ProductStatsData
```

### VIP 会员接口

```kotlin
// 获取 VIP 信息
suspend fun getVIPInfo(): VIPInfo

// 检查 VIP 状态
suspend fun checkVIPStatus(): VIPStatusData

// 获取配额信息
suspend fun getQuotaInfo(): QuotaInfo

// 获取最大角色数
suspend fun getMaxRoles(): MaxRolesData

// 获取最大上下文数
suspend fun getMaxContexts(): MaxContextsData
```

## 错误处理

VipPayService 使用 `VipPayError` 密封类来处理各种错误情况：

```kotlin
try {
    val result = vipPayService.someOperation()
} catch (e: VipPayError) {
    when (e) {
        is VipPayError.Unauthorized -> {
            // 未授权访问，需要重新登录
        }
        is VipPayError.TokenExpired -> {
            // Token已过期，需要刷新Token
        }
        is VipPayError.NetworkError -> {
            // 网络错误
        }
        is VipPayError.ServerError -> {
            // 服务器错误
        }
        is VipPayError.NoData -> {
            // 无数据返回
        }
        else -> {
            // 其他错误
        }
    }
}
```

## 使用示例

### 完整的 Apple IAP 验证流程

```kotlin
lifecycleScope.launch {
    try {
        // 1. 设置用户Token
        GlobalToken.setToken("user_token")
        
        // 2. 验证收据
        val verifyResult = vipPayService.verifyAppleReceipt(
            receiptData = "base64_receipt_data",
            sandbox = true
        )
        
        // 3. 确认购买
        vipPayService.acknowledgePurchase(
            platform = PaymentPlatform.APPLE,
            purchaseToken = verifyResult.transactionId,
            productId = verifyResult.productId
        )
        
        // 4. 获取订阅状态
        val subscriptionStatus = vipPayService.getAppleSubscriptionStatus(
            verifyResult.originalTransactionId
        )
        
        Log.d("IAP", "订阅状态: ${subscriptionStatus.status}")
        
    } catch (e: VipPayError) {
        Log.e("IAP", "验证失败: ${e.message}")
    }
}
```

### 获取 VIP 信息

```kotlin
lifecycleScope.launch {
    try {
        val vipInfo = vipPayService.getVIPInfo()
        
        if (vipInfo.isVip) {
            Log.d("VIP", "用户是VIP，等级: ${vipInfo.vipLevel}")
            Log.d("VIP", "剩余天数: ${vipInfo.remainingDays}")
        } else {
            Log.d("VIP", "用户不是VIP")
        }
        
        // 获取配额信息
        val quotaInfo = vipPayService.getQuotaInfo()
        Log.d("VIP", "剩余配额: ${quotaInfo.remainingQuota}")
        
    } catch (e: VipPayError) {
        Log.e("VIP", "获取VIP信息失败: ${e.message}")
    }
}
```

## 配置

### 服务器地址

默认服务器地址为 `http://192.168.1.7:8088/api/vippay`，可以通过修改 `VipPayService` 中的 `BASE_URL` 常量来更改。

### 超时设置

- 连接超时：30秒
- 读取超时：30秒
- 写入超时：30秒
- 最大重试次数：3次

### 日志级别

默认启用详细的HTTP请求和响应日志，可以通过修改 `createLoggingInterceptor()` 方法中的日志级别来调整。

## 注意事项

1. **Token 管理**：确保在使用需要认证的接口前设置有效的用户Token
2. **协程作用域**：所有API调用都是suspend函数，需要在协程作用域中调用
3. **错误处理**：建议为每个API调用添加适当的错误处理逻辑
4. **网络权限**：确保应用具有网络访问权限
5. **线程安全**：VipPayService 使用单例模式，是线程安全的

## 许可证

此项目遵循 MIT 许可证。
