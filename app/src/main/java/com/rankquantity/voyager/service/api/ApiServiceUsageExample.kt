package com.rankquantity.voyager.service.api

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ApiService使用示例
 * 展示如何正确使用改进后的API服务
 */
class ApiServiceUsageExample {
    
    companion object {
        private const val TAG = "ApiServiceExample"
    }
    
    /**
     * 用户登录示例
     */
    fun loginExample(account: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ApiService.getInstance()
                val response = apiService.login(account, password)
                
                Log.d(TAG, "登录成功: ${response.data?.name}")
                
                // 获取用户信息
                val userInfo = apiService.getUserInfo(response.data?.userId ?: 0L)
                Log.d(TAG, "用户信息: ${userInfo.name} - ${userInfo.account}")
                
            } catch (e: APIError) {
                // 使用增强的错误处理
                when (e) {
                    is APIError.NetworkError -> {
                        Log.e(TAG, "网络错误: ${e.getUserFriendlyMessage()}")
                        if (e.isTimeout) {
                            Log.e(TAG, "请求超时，建议检查网络连接")
                        } else if (e.isConnectionError) {
                            Log.e(TAG, "连接失败，建议检查网络设置")
                        }
                    }
                    is APIError.AuthenticationError -> {
                        Log.e(TAG, "认证错误: ${e.message}")
                        if (e.isTokenExpired) {
                            Log.e(TAG, "Token已过期，需要重新登录")
                        }
                    }
                    is APIError.ServerError -> {
                        Log.e(TAG, "服务器错误: ${e.getUserFriendlyMessage()}")
                    }
                    else -> {
                        Log.e(TAG, "其他错误: ${e.message}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "未知错误: ${e.message}")
            }
        }
    }
    
    /**
     * 用户注册示例
     */
    fun registerExample(account: String, password: String, name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ApiService.getInstance()
                val response = apiService.register(account, password, name)
                
                Log.d(TAG, "注册成功: ${response.data?.name}")
                
            } catch (e: APIError) {
                Log.e(TAG, "注册失败: ${e.getUserFriendlyMessage()}")
                
                when (e) {
                    is APIError.ServerError -> {
                        if (e.statusCode == 409) {
                            Log.e(TAG, "账户已存在，请使用其他账户名")
                        }
                    }
                    else -> {
                        Log.e(TAG, "注册错误: ${e.message}")
                    }
                }
            }
        }
    }
    
    /**
     * Token刷新示例
     */
    fun refreshTokenExample() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ApiService.getInstance()
                val currentToken = GlobalToken.userToken ?: return@launch
                
                val result = apiService.refreshToken(currentToken)
                
                if (result.error == null) {
                    Log.d(TAG, "Token刷新成功: ${result.token.take(20)}...")
                } else {
                    Log.e(TAG, "Token刷新失败: ${result.error?.message}")
                }
                
            } catch (e: APIError) {
                Log.e(TAG, "Token刷新错误: ${e.getUserFriendlyMessage()}")
            }
        }
    }
    
    /**
     * 健康检查示例
     */
    fun healthCheckExample() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ApiService.getInstance()
                val response = apiService.healthCheck()
                
                Log.d(TAG, "健康检查成功: ${response.data}")
                
            } catch (e: APIError) {
                Log.e(TAG, "健康检查失败: ${e.getUserFriendlyMessage()}")
            }
        }
    }
    
    /**
     * 清理资源示例
     */
    fun cleanupExample() {
        try {
            // 清理Token
            GlobalToken.clearToken()
            
            // 清理ApiService资源
            ApiService.clearInstance()
            
            Log.d(TAG, "资源清理完成")
            
        } catch (e: Exception) {
            Log.e(TAG, "资源清理失败: ${e.message}")
        }
    }
}
