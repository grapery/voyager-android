package com.rankquantity.voyager.service.api

import com.google.gson.annotations.SerializedName

/**
 * API错误类型
 * 增强的错误处理，支持更详细的错误信息
 */
sealed class APIError(message: String, val code: Int = -1, val originalError: Throwable? = null) : Exception(message, originalError) {
    object InvalidResponse : APIError("无效的响应数据")
    
    data class NetworkError(
        val originalError: Throwable,
        val httpStatusCode: Int? = null,
        val isTimeout: Boolean = false,
        val isConnectionError: Boolean = false
    ) : APIError(
        message = when {
            isTimeout -> "网络请求超时，请检查网络连接"
            isConnectionError -> "网络连接失败，请检查网络设置"
            httpStatusCode != null -> "网络错误 [$httpStatusCode]: ${originalError.message}"
            else -> "网络错误: ${originalError.message}"
        },
        originalError = originalError,
        code = httpStatusCode ?: -1
    )
    
    data class ServerError(
        val statusCode: Int, 
        val message: String,
        val details: String? = null
    ) : APIError(
        message = if (details != null) "服务器错误 [$statusCode]: $message - $details" else "服务器错误 [$statusCode]: $message",
        code = statusCode
    )
    
    data class AuthenticationError(
        val message: String,
        val isTokenExpired: Boolean = false
    ) : APIError(
        message = if (isTokenExpired) "认证失败：Token已过期" else "认证错误: $message",
        code = 401
    )
    
    object InvalidToken : APIError("无效的认证令牌", 401)
    object TokenExpired : APIError("Token已过期", 401)
    object LoginFailed : APIError("登录失败", 401)
    object RegisterFailed : APIError("注册失败", 400)
    object UserNotFound : APIError("用户不存在", 404)
    object PasswordIncorrect : APIError("密码错误", 401)
    object AccountExists : APIError("账户已存在", 409)
    object InvalidAccount : APIError("无效的账户", 400)
    object InvalidPassword : APIError("无效的密码", 400)
    
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
            else -> message
        }
    }
}

/**
 * 响应码枚举
 */
enum class ResponseCode(val value: Int) {
    SUCCESS(0),
    ERROR(1),
    INVALID_PARAMS(2),
    UNAUTHORIZED(3),
    FORBIDDEN(4),
    NOT_FOUND(5),
    INTERNAL_ERROR(6);
    
    companion object {
        fun fromValue(value: Int): ResponseCode {
            return values().find { it.value == value } ?: ERROR
        }
    }
}

/**
 * 登录请求
 */
data class LoginRequest(
    @SerializedName("account") val account: String,
    @SerializedName("password") val password: String
)

/**
 * 登录响应数据
 */
data class LoginData(
    @SerializedName("token") val token: String,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("account") val account: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

/**
 * 登录响应
 */
data class LoginResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: LoginData?
)

/**
 * 注册请求
 */
data class RegisterRequest(
    @SerializedName("account") val account: String,
    @SerializedName("password") val password: String,
    @SerializedName("name") val name: String
)

/**
 * 注册响应数据
 */
data class RegisterData(
    @SerializedName("user_id") val userId: Long,
    @SerializedName("account") val account: String,
    @SerializedName("name") val name: String,
    @SerializedName("created_at") val createdAt: String
)

/**
 * 注册响应
 */
data class RegisterResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: RegisterData?
)

/**
 * 用户信息请求
 */
data class UserInfoRequest(
    @SerializedName("user_id") val userId: Long,
    @SerializedName("account") val account: String = ""
)

/**
 * 用户信息数据
 */
data class UserInfo(
    @SerializedName("user_id") val userId: Long,
    @SerializedName("account") val account: String,
    @SerializedName("name") val name: String,
    @SerializedName("avatar") val avatar: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("status") val status: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("last_login_at") val lastLoginAt: String?
)

/**
 * 用户信息响应数据
 */
data class UserInfoData(
    @SerializedName("info") val info: UserInfo
)

/**
 * 用户信息响应
 */
data class UserInfoResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UserInfoData?
)

/**
 * 刷新Token请求
 */
data class RefreshTokenRequest(
    @SerializedName("token") val token: String
)

/**
 * 刷新Token响应数据
 */
data class RefreshTokenData(
    @SerializedName("token") val token: String,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("expires_at") val expiresAt: Long
)

/**
 * 刷新Token响应
 */
data class RefreshTokenResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: RefreshTokenData?
)

/**
 * 登出请求
 */
data class LogoutRequest(
    @SerializedName("token") val token: String
)

/**
 * 登出响应数据
 */
data class LogoutData(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)

/**
 * 登出响应
 */
data class LogoutResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: LogoutData?
)

/**
 * 重置密码请求
 */
data class ResetPasswordRequest(
    @SerializedName("account") val account: String,
    @SerializedName("old_pwd") val oldPwd: String,
    @SerializedName("new_pwd") val newPwd: String
)

/**
 * 重置密码响应数据
 */
data class ResetPasswordData(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String
)

/**
 * 重置密码响应
 */
data class ResetPasswordResponse(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ResetPasswordData?
)

/**
 * 通用API响应包装器
 */
data class ApiResponse<T>(
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T?
)

/**
 * Token刷新结果
 */
data class TokenRefreshResult(
    val userId: Long,
    val token: String,
    val error: Throwable? = null
)
