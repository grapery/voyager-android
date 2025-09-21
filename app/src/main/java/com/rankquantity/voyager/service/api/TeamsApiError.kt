package com.rankquantity.voyager.service.api

/**
 * TeamsAPI 错误类型定义
 * 基于响应码和错误类型的结构化错误处理
 */
sealed class TeamsApiError(message: String, val code: Int = -1, val originalError: Throwable? = null) : Exception(message, originalError) {
    
    // MARK: - 基础错误类型
    
    /**
     * 无效响应数据
     */
    object InvalidResponse : TeamsApiError("无效的响应数据")
    
    /**
     * 网络错误
     */
    data class NetworkError(
        val originalError: Throwable,
        val httpStatusCode: Int? = null,
        val isTimeout: Boolean = false,
        val isConnectionError: Boolean = false
    ) : TeamsApiError(
        message = when {
            isTimeout -> "网络请求超时，请检查网络连接"
            isConnectionError -> "网络连接失败，请检查网络设置"
            httpStatusCode != null -> "网络错误 [$httpStatusCode]: ${originalError.message}"
            else -> "网络错误: ${originalError.message}"
        },
        originalError = originalError,
        code = httpStatusCode ?: -1
    )
    
    /**
     * 服务器错误
     */
    data class ServerError(
        val statusCode: Int,
        val errorMessage: String,
        val details: String? = null
    ) : TeamsApiError(
        message = if (details != null) "服务器错误 [$statusCode]: $errorMessage - $details" else "服务器错误 [$statusCode]: $errorMessage",
        code = statusCode
    )
    
    /**
     * 认证错误
     */
    data class AuthenticationError(
        val errorMessage: String,
        val isTokenExpired: Boolean = false
    ) : TeamsApiError(
        message = if (isTokenExpired) "认证失败：Token已过期" else "认证错误: $errorMessage",
        code = 401
    )
    
    /**
     * 权限错误
     */
    data class PermissionError(
        val errorMessage: String,
        val requiredPermission: String? = null
    ) : TeamsApiError(
        message = if (requiredPermission != null) "权限不足: $errorMessage (需要权限: $requiredPermission)" else "权限不足: $errorMessage",
        code = 403
    )
    
    /**
     * 资源未找到错误
     */
    data class NotFoundError(
        val resourceType: String,
        val resourceId: String? = null
    ) : TeamsApiError(
        message = if (resourceId != null) "$resourceType 未找到: $resourceId" else "$resourceType 未找到",
        code = 404
    )
    
    /**
     * 参数验证错误
     */
    data class ValidationError(
        val field: String,
        val errorMessage: String,
        val invalidValue: String? = null
    ) : TeamsApiError(
        message = "参数验证失败 [$field]: $errorMessage${if (invalidValue != null) " (值: $invalidValue)" else ""}",
        code = 400
    )
    
    /**
     * 业务逻辑错误
     */
    data class BusinessError(
        val errorCode: String,
        val errorMessage: String,
        val context: Map<String, Any>? = null
    ) : TeamsApiError(
        message = "业务错误 [$errorCode]: $errorMessage",
        code = -1
    )
    
    /**
     * 限流错误
     */
    data class RateLimitError(
        val limit: Int,
        val remaining: Int,
        val resetTime: Long? = null
    ) : TeamsApiError(
        message = "请求过于频繁，请稍后再试 (限制: $limit, 剩余: $remaining)",
        code = 429
    )
    
    /**
     * 解析错误
     */
    data class ParseError(
        val originalError: Throwable,
        val dataType: String
    ) : TeamsApiError(
        message = "数据解析失败 [$dataType]: ${originalError.message}",
        originalError = originalError
    )
    
    // MARK: - 特定业务错误
    
    /**
     * 用户相关错误
     */
    object UserNotFound : TeamsApiError("用户不存在", 404)
    object UserAlreadyExists : TeamsApiError("用户已存在", 409)
    object InvalidUserCredentials : TeamsApiError("用户凭据无效", 401)
    object UserAccountLocked : TeamsApiError("用户账户已锁定", 423)
    object UserAccountDisabled : TeamsApiError("用户账户已禁用", 423)
    
    /**
     * 组织相关错误
     */
    object GroupNotFound : TeamsApiError("组织不存在", 404)
    object GroupAccessDenied : TeamsApiError("无权限访问该组织", 403)
    object GroupMemberLimitExceeded : TeamsApiError("组织成员数量已达上限", 409)
    object GroupNameAlreadyExists : TeamsApiError("组织名称已存在", 409)
    
    /**
     * 故事相关错误
     */
    object StoryNotFound : TeamsApiError("故事不存在", 404)
    object StoryAccessDenied : TeamsApiError("无权限访问该故事", 403)
    object StoryAlreadyExists : TeamsApiError("故事已存在", 409)
    object StoryIncomplete : TeamsApiError("故事信息不完整", 400)
    
    /**
     * 角色相关错误
     */
    object RoleNotFound : TeamsApiError("角色不存在", 404)
    object RoleAccessDenied : TeamsApiError("无权限访问该角色", 403)
    object RoleNameAlreadyExists : TeamsApiError("角色名称已存在", 409)
    object RoleGenerationFailed : TeamsApiError("角色生成失败", 500)
    
    /**
     * 故事板相关错误
     */
    object StoryBoardNotFound : TeamsApiError("故事板不存在", 404)
    object StoryBoardAccessDenied : TeamsApiError("无权限访问该故事板", 403)
    object StoryBoardGenerationFailed : TeamsApiError("故事板生成失败", 500)
    object StoryBoardRenderFailed : TeamsApiError("故事板渲染失败", 500)
    
    /**
     * 文件上传错误
     */
    object FileUploadFailed : TeamsApiError("文件上传失败", 500)
    object FileSizeExceeded : TeamsApiError("文件大小超出限制", 413)
    object UnsupportedFileType : TeamsApiError("不支持的文件类型", 415)
    
    /**
     * 聊天相关错误
     */
    object ChatSessionNotFound : TeamsApiError("聊天会话不存在", 404)
    object ChatMessageFailed : TeamsApiError("消息发送失败", 500)
    object ChatRateLimited : TeamsApiError("聊天频率限制", 429)
    
    /**
     * 任务相关错误
     */
    object TaskNotFound : TeamsApiError("任务不存在", 404)
    object TaskFailed : TeamsApiError("任务执行失败", 500)
    object TaskTimeout : TeamsApiError("任务执行超时", 408)
    
    // MARK: - 工具方法
    
    /**
     * 获取用户友好的错误消息
     */
    fun getUserFriendlyMessage(): String? {
        return when (this) {
            is NetworkError -> when {
                isTimeout -> "网络连接超时，请检查网络设置"
                isConnectionError -> "网络连接失败，请检查网络"
                else -> "网络错误，请稍后重试"
            }
            is AuthenticationError -> if (isTokenExpired) "登录已过期，请重新登录" else "认证失败，请重新登录"
            is PermissionError -> "权限不足，请联系管理员"
            is NotFoundError -> "请求的内容不存在"
            is ValidationError -> "输入信息有误，请检查后重试"
            is RateLimitError -> "操作过于频繁，请稍后再试"
            is ParseError -> "数据解析错误，请重试"
            is BusinessError -> errorMessage
            is ServerError -> "服务器错误，请稍后重试"
            else -> message
        }
    }
    
    /**
     * 获取错误分类
     */
    fun getErrorCategory(): String {
        return when (this) {
            is NetworkError -> "NETWORK"
            is AuthenticationError -> "AUTHENTICATION"
            is PermissionError -> "PERMISSION"
            is NotFoundError -> "NOT_FOUND"
            is ValidationError -> "VALIDATION"
            is BusinessError -> "BUSINESS"
            is RateLimitError -> "RATE_LIMIT"
            is ParseError -> "PARSE"
            is ServerError -> "SERVER"
            else -> "UNKNOWN"
        }
    }
    
    /**
     * 是否可重试
     */
    fun isRetryable(): Boolean {
        return when (this) {
            is NetworkError -> !isConnectionError
            is ServerError -> statusCode in 500..599
            is RateLimitError -> true
            is TaskTimeout -> true
            else -> false
        }
    }
    
    /**
     * 获取建议的重试延迟（毫秒）
     */
    fun getRetryDelay(): Long {
        return when (this) {
            is NetworkError -> 1000L
            is ServerError -> 2000L
            is RateLimitError -> 5000L
            is TaskTimeout -> 3000L
            else -> 1000L
        }
    }
}
