package com.rankquantity.voyager.service.api

/**
 * 错误处理器
 * 用于处理API响应码和错误信息
 */
object ErrorHandler {
    
    /**
     * 检查响应码是否表示成功
     */
    fun isSuccess(code: Int): Boolean {
        return code == ResponseCode.SUCCESS.value
    }
    
    /**
     * 处理登录错误
     */
    fun handleLoginError(code: Int): String {
        return when (code) {
            ResponseCode.INVALID_PARAMS.value -> "登录失败：参数无效"
            ResponseCode.UNAUTHORIZED.value -> "登录失败：用户名或密码错误"
            ResponseCode.FORBIDDEN.value -> "登录失败：账户已被禁用"
            ResponseCode.NOT_FOUND.value -> "登录失败：用户不存在"
            ResponseCode.INTERNAL_ERROR.value -> "登录失败：服务器内部错误"
            else -> "登录失败：未知错误 (错误码: $code)"
        }
    }
    
    /**
     * 处理注册错误
     */
    fun handleRegisterError(code: Int): String {
        return when (code) {
            ResponseCode.INVALID_PARAMS.value -> "注册失败：参数无效"
            ResponseCode.ERROR.value -> "注册失败：账户已存在"
            ResponseCode.FORBIDDEN.value -> "注册失败：账户格式不正确"
            ResponseCode.INTERNAL_ERROR.value -> "注册失败：服务器内部错误"
            else -> "注册失败：未知错误 (错误码: $code)"
        }
    }
    
    /**
     * 处理用户信息获取错误
     */
    fun handleUserInfoError(code: Int): String {
        return when (code) {
            ResponseCode.INVALID_PARAMS.value -> "获取用户信息失败：参数无效"
            ResponseCode.UNAUTHORIZED.value -> "获取用户信息失败：未授权访问"
            ResponseCode.NOT_FOUND.value -> "获取用户信息失败：用户不存在"
            ResponseCode.INTERNAL_ERROR.value -> "获取用户信息失败：服务器内部错误"
            else -> "获取用户信息失败：未知错误 (错误码: $code)"
        }
    }
    
    /**
     * 处理Token刷新错误
     */
    fun handleRefreshTokenError(code: Int): String {
        return when (code) {
            ResponseCode.INVALID_PARAMS.value -> "刷新Token失败：Token无效"
            ResponseCode.UNAUTHORIZED.value -> "刷新Token失败：Token已过期"
            ResponseCode.INTERNAL_ERROR.value -> "刷新Token失败：服务器内部错误"
            else -> "刷新Token失败：未知错误 (错误码: $code)"
        }
    }
    
    /**
     * 处理登出错误
     */
    fun handleLogoutError(code: Int): String {
        return when (code) {
            ResponseCode.INVALID_PARAMS.value -> "登出失败：参数无效"
            ResponseCode.UNAUTHORIZED.value -> "登出失败：未授权访问"
            ResponseCode.INTERNAL_ERROR.value -> "登出失败：服务器内部错误"
            else -> "登出失败：未知错误 (错误码: $code)"
        }
    }
    
    /**
     * 处理密码重置错误
     */
    fun handleResetPasswordError(code: Int): String {
        return when (code) {
            ResponseCode.INVALID_PARAMS.value -> "重置密码失败：参数无效"
            ResponseCode.UNAUTHORIZED.value -> "重置密码失败：原密码错误"
            ResponseCode.NOT_FOUND.value -> "重置密码失败：用户不存在"
            ResponseCode.INTERNAL_ERROR.value -> "重置密码失败：服务器内部错误"
            else -> "重置密码失败：未知错误 (错误码: $code)"
        }
    }
    
    /**
     * 处理通用API错误
     */
    fun handleApiError(code: Int): String {
        return when (code) {
            ResponseCode.SUCCESS.value -> "操作成功"
            ResponseCode.ERROR.value -> "操作失败"
            ResponseCode.INVALID_PARAMS.value -> "参数无效"
            ResponseCode.UNAUTHORIZED.value -> "未授权访问"
            ResponseCode.FORBIDDEN.value -> "访问被禁止"
            ResponseCode.NOT_FOUND.value -> "资源不存在"
            ResponseCode.INTERNAL_ERROR.value -> "服务器内部错误"
            else -> "未知错误 (错误码: $code)"
        }
    }
    
    /**
     * 将响应码转换为对应的异常
     * 增强的错误处理，支持更详细的错误信息
     */
    fun codeToException(code: Int, operation: String): APIError {
        val message = when (operation) {
            "login" -> handleLoginError(code)
            "register" -> handleRegisterError(code)
            "userInfo" -> handleUserInfoError(code)
            "refreshToken" -> handleRefreshTokenError(code)
            "logout" -> handleLogoutError(code)
            "resetPassword" -> handleResetPasswordError(code)
            else -> handleApiError(code)
        }
        
        return when (code) {
            ResponseCode.UNAUTHORIZED.value -> {
                when (operation) {
                    "refreshToken" -> APIError.AuthenticationError(message, isTokenExpired = true)
                    else -> APIError.AuthenticationError(message)
                }
            }
            ResponseCode.INVALID_PARAMS.value -> APIError.ServerError(code, message, "参数验证失败")
            ResponseCode.FORBIDDEN.value -> APIError.ServerError(code, message, "访问权限不足")
            ResponseCode.NOT_FOUND.value -> APIError.ServerError(code, message, "请求的资源不存在")
            ResponseCode.INTERNAL_ERROR.value -> APIError.ServerError(code, message, "服务器内部处理错误")
            ResponseCode.ERROR.value -> APIError.ServerError(code, message, "业务逻辑错误")
            else -> APIError.ServerError(code, message, "未知错误")
        }
    }
}
