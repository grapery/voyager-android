package com.rankquantity.voyager.service.api

/**
 * VIPPay 错误类型枚举
 * 定义所有可能的错误类型和对应的错误信息
 */
sealed class VipPayError(message: String, val code: Int = -1) : Exception(message) {
    
    /** 无效的URL */
    object InvalidURL : VipPayError("无效的URL")
    
    /** 未授权访问 */
    object Unauthorized : VipPayError("未授权访问", 401)
    
    /** Token已过期 */
    object TokenExpired : VipPayError("Token已过期", 403)
    
    /** 无数据返回 */
    object NoData : VipPayError("无数据返回")
    
    /** 网络错误 */
    data class NetworkError(val originalError: Throwable) : VipPayError("网络错误: ${originalError.message}")
    
    /** 服务器错误 */
    data class ServerError(val statusCode: Int, val message: String) : VipPayError("服务器错误 [$statusCode]: $message", statusCode)
    
    /** 解析错误 */
    data class ParseError(val originalError: Throwable) : VipPayError("数据解析错误: ${originalError.message}")
    
    /** 未知错误 */
    data class UnknownError(val originalError: Throwable) : VipPayError("未知错误: ${originalError.message}")
}
