package com.rankquantity.voyager.service.api

/**
 * VIPPay 错误类型枚举
 * 定义所有可能的错误类型和对应的错误信息
 * 增强的错误处理，与ApiService保持一致
 */
sealed class VipPayError(message: String, val code: Int = -1, val originalError: Throwable? = null) : Exception(message, originalError) {
    
    /** 无效的URL */
    object InvalidURL : VipPayError("无效的URL")
    
    /** 未授权访问 */
    object Unauthorized : VipPayError("未授权访问", 401)
    
    /** Token已过期 */
    object TokenExpired : VipPayError("Token已过期", 401)
    
    /** 无数据返回 */
    object NoData : VipPayError("无数据返回")
    
    /** 网络错误 */
    data class NetworkError(
        val originalError: Throwable,
        val httpStatusCode: Int? = null,
        val isTimeout: Boolean = false,
        val isConnectionError: Boolean = false
    ) : VipPayError(
        message = when {
            isTimeout -> "网络请求超时，请检查网络连接"
            isConnectionError -> "网络连接失败，请检查网络设置"
            httpStatusCode != null -> "网络错误 [$httpStatusCode]: ${originalError.message}"
            else -> "网络错误: ${originalError.message}"
        },
        originalError = originalError,
        code = httpStatusCode ?: -1
    )
    
    /** 服务器错误 */
    data class ServerError(
        val statusCode: Int, 
        val message: String,
        val details: String? = null
    ) : VipPayError(
        message = if (details != null) "服务器错误 [$statusCode]: $message - $details" else "服务器错误 [$statusCode]: $message",
        code = statusCode
    )
    
    /** 解析错误 */
    data class ParseError(val originalError: Throwable) : VipPayError("数据解析错误: ${originalError.message}", originalError = originalError)
    
    /** 认证错误 */
    data class AuthenticationError(
        val message: String,
        val isTokenExpired: Boolean = false
    ) : VipPayError(
        message = if (isTokenExpired) "认证失败：Token已过期" else "认证错误: $message",
        code = 401
    )
    
    /** 支付相关错误 */
    data class PaymentError(
        val message: String,
        val errorCode: String? = null
    ) : VipPayError("支付错误: $message")
    
    /** 产品相关错误 */
    data class ProductError(
        val message: String,
        val productId: String? = null
    ) : VipPayError("产品错误: $message")
    
    /** 未知错误 */
    data class UnknownError(val originalError: Throwable) : VipPayError("未知错误: ${originalError.message}", originalError = originalError)
    
    /**
     * 获取用户友好的错误消息
     */
    fun getUserFriendlyMessage(): String {
        return when (this) {
            is NetworkError -> {
                when {
                    isTimeout -> "请求超时，请稍后重试"
                    isConnectionError -> "网络连接失败，请检查网络"
                    else -> "网络错误，请稍后重试"
                }
            }
            is ServerError -> {
                when (statusCode) {
                    500 -> "服务器繁忙，请稍后重试"
                    503 -> "服务暂时不可用，请稍后重试"
                    else -> message
                }
            }
            is AuthenticationError -> {
                if (isTokenExpired) "登录已过期，请重新登录" else message
            }
            is PaymentError -> {
                when (errorCode) {
                    "INVALID_PURCHASE" -> "购买验证失败，请重试"
                    "ALREADY_OWNED" -> "您已拥有此产品"
                    "PRODUCT_NOT_AVAILABLE" -> "产品暂不可用"
                    else -> message
                }
            }
            is ProductError -> {
                when {
                    productId != null -> "产品 $productId 信息错误"
                    else -> message
                }
            }
            else -> message
        }
    }
}
