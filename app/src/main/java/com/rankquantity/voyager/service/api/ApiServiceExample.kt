package com.rankquantity.voyager.service.api

import android.util.Log
import kotlinx.coroutines.*

/**
 * ApiService 使用示例
 * 展示如何使用基础API服务
 */
class ApiServiceExample {
    
    companion object {
        private const val TAG = "ApiServiceExample"
    }
    
    /**
     * 健康检查示例
     */
    suspend fun healthCheckExample() {
        try {
            val healthData = ApiService.getInstance().healthCheck()
            Log.d(TAG, "API服务健康状态 - code: ${healthData.code}, message: ${healthData.message}")
        } catch (e: APIError) {
            Log.e(TAG, "健康检查失败: ${e.message}")
        }
    }
    
    /**
     * 用户注册示例
     */
    suspend fun registerExample() {
        try {
            val registerResponse = ApiService.getInstance().register(
                account = "test@example.com",
                password = "password123",
                name = "测试用户"
            )
            
            Log.d(TAG, "注册结果:")
            Log.d(TAG, "  - code: ${registerResponse.code}")
            Log.d(TAG, "  - message: ${registerResponse.message}")
            
            if (registerResponse.code == 0 && registerResponse.data != null) {
                val userData = registerResponse.data!!
                Log.d(TAG, "用户数据:")
                Log.d(TAG, "  - userId: ${userData.userId}")
                Log.d(TAG, "  - account: ${userData.account}")
                Log.d(TAG, "  - name: ${userData.name}")
                Log.d(TAG, "  - createdAt: ${userData.createdAt}")
            }
            
        } catch (e: APIError) {
            when (e) {
                is APIError.AccountExists -> Log.e(TAG, "账户已存在: ${e.message}")
                is APIError.InvalidAccount -> Log.e(TAG, "无效账户格式: ${e.message}")
                is APIError.InvalidPassword -> Log.e(TAG, "无效密码: ${e.message}")
                is APIError.ServerError -> Log.e(TAG, "服务器错误: ${e.statusCode} - ${e.message}")
                else -> Log.e(TAG, "注册失败: ${e.message}")
            }
        }
    }
    
    /**
     * 用户登录示例
     */
    suspend fun loginExample() {
        try {
            val loginResponse = ApiService.getInstance().login(
                account = "test@example.com",
                password = "password123"
            )
            
            Log.d(TAG, "登录结果:")
            Log.d(TAG, "  - code: ${loginResponse.code}")
            Log.d(TAG, "  - message: ${loginResponse.message}")
            
            if (loginResponse.code == 0 && loginResponse.data != null) {
                val loginData = loginResponse.data!!
                Log.d(TAG, "登录数据:")
                Log.d(TAG, "  - userId: ${loginData.userId}")
                Log.d(TAG, "  - account: ${loginData.account}")
                Log.d(TAG, "  - name: ${loginData.name}")
                Log.d(TAG, "  - token: ${loginData.token.take(20)}...")
                
                // 登录成功后，Token会自动保存到GlobalToken中
                Log.d(TAG, "Token已自动保存到全局状态")
            }
            
        } catch (e: APIError) {
            when (e) {
                is APIError.LoginFailed -> Log.e(TAG, "登录失败: ${e.message}")
                is APIError.UserNotFound -> Log.e(TAG, "用户不存在: ${e.message}")
                is APIError.PasswordIncorrect -> Log.e(TAG, "密码错误: ${e.message}")
                is APIError.AuthenticationError -> Log.e(TAG, "认证错误: ${e.message}")
                is APIError.ServerError -> Log.e(TAG, "服务器错误: ${e.statusCode} - ${e.message}")
                else -> Log.e(TAG, "登录失败: ${e.message}")
            }
        }
    }
    
    /**
     * 获取用户信息示例
     */
    suspend fun getUserInfoExample() {
        try {
            // 确保已登录
            if (!GlobalToken.hasValidToken()) {
                Log.e(TAG, "用户未登录，请先登录")
                return
            }
            
            val userInfo = ApiService.getInstance().getUserInfo(userId = 12345L)
            
            Log.d(TAG, "用户信息:")
            Log.d(TAG, "  - userId: ${userInfo.userId}")
            Log.d(TAG, "  - account: ${userInfo.account}")
            Log.d(TAG, "  - name: ${userInfo.name}")
            Log.d(TAG, "  - avatar: ${userInfo.avatar ?: "无头像"}")
            Log.d(TAG, "  - email: ${userInfo.email ?: "无邮箱"}")
            Log.d(TAG, "  - phone: ${userInfo.phone ?: "无电话"}")
            Log.d(TAG, "  - status: ${userInfo.status}")
            Log.d(TAG, "  - createdAt: ${userInfo.createdAt}")
            Log.d(TAG, "  - updatedAt: ${userInfo.updatedAt}")
            Log.d(TAG, "  - lastLoginAt: ${userInfo.lastLoginAt ?: "从未登录"}")
            
        } catch (e: APIError) {
            when (e) {
                is APIError.AuthenticationError -> Log.e(TAG, "认证错误，请重新登录: ${e.message}")
                is APIError.UserNotFound -> Log.e(TAG, "用户不存在: ${e.message}")
                is APIError.ServerError -> Log.e(TAG, "服务器错误: ${e.statusCode} - ${e.message}")
                else -> Log.e(TAG, "获取用户信息失败: ${e.message}")
            }
        }
    }
    
    /**
     * Token刷新示例
     */
    suspend fun refreshTokenExample() {
        try {
            val currentToken = GlobalToken.userToken ?: throw APIError.InvalidToken
            
            val refreshResult = ApiService.getInstance().refreshToken(currentToken)
            
            if (refreshResult.error != null) {
                Log.e(TAG, "刷新Token失败: ${refreshResult.error!!.message}")
                // Token刷新失败，需要重新登录
                GlobalToken.clearToken()
                Log.d(TAG, "Token已清除，请重新登录")
            } else {
                Log.d(TAG, "Token刷新成功:")
                Log.d(TAG, "  - userId: ${refreshResult.userId}")
                Log.d(TAG, "  - newToken: ${refreshResult.token.take(20)}...")
                Log.d(TAG, "新Token已自动保存到全局状态")
            }
            
        } catch (e: APIError) {
            when (e) {
                is APIError.InvalidToken -> Log.e(TAG, "无效Token: ${e.message}")
                is APIError.TokenExpired -> Log.e(TAG, "Token已过期: ${e.message}")
                is APIError.AuthenticationError -> Log.e(TAG, "认证错误: ${e.message}")
                else -> Log.e(TAG, "刷新Token失败: ${e.message}")
            }
        }
    }
    
    /**
     * 密码重置示例
     */
    suspend fun resetPasswordExample() {
        try {
            val resetResponse = ApiService.getInstance().resetPassword(
                account = "test@example.com",
                oldPwd = "oldpassword123",
                newPwd = "newpassword123"
            )
            
            Log.d(TAG, "密码重置结果:")
            Log.d(TAG, "  - code: ${resetResponse.code}")
            Log.d(TAG, "  - message: ${resetResponse.message}")
            
            if (resetResponse.code == 0 && resetResponse.data != null) {
                val resetData = resetResponse.data!!
                Log.d(TAG, "重置数据:")
                Log.d(TAG, "  - success: ${resetData.success}")
                Log.d(TAG, "  - message: ${resetData.message}")
            }
            
        } catch (e: APIError) {
            when (e) {
                is APIError.PasswordIncorrect -> Log.e(TAG, "原密码错误: ${e.message}")
                is APIError.UserNotFound -> Log.e(TAG, "用户不存在: ${e.message}")
                is APIError.AuthenticationError -> Log.e(TAG, "认证错误: ${e.message}")
                is APIError.ServerError -> Log.e(TAG, "服务器错误: ${e.statusCode} - ${e.message}")
                else -> Log.e(TAG, "密码重置失败: ${e.message}")
            }
        }
    }
    
    /**
     * 用户登出示例
     */
    suspend fun logoutExample() {
        try {
            val logoutResponse = ApiService.getInstance().logout()
            
            Log.d(TAG, "登出结果:")
            Log.d(TAG, "  - code: ${logoutResponse.code}")
            Log.d(TAG, "  - message: ${logoutResponse.message}")
            
            if (logoutResponse.code == 0 && logoutResponse.data != null) {
                val logoutData = logoutResponse.data!!
                Log.d(TAG, "登出数据:")
                Log.d(TAG, "  - success: ${logoutData.success}")
                Log.d(TAG, "  - message: ${logoutData.message}")
                
                // 登出成功后清除本地Token
                GlobalToken.clearToken()
                Log.d(TAG, "本地Token已清除")
            }
            
        } catch (e: APIError) {
            when (e) {
                is APIError.InvalidToken -> Log.e(TAG, "无效Token: ${e.message}")
                is APIError.AuthenticationError -> Log.e(TAG, "认证错误: ${e.message}")
                is APIError.ServerError -> Log.e(TAG, "服务器错误: ${e.statusCode} - ${e.message}")
                else -> Log.e(TAG, "登出失败: ${e.message}")
            }
        }
    }
    
    /**
     * 错误处理示例
     */
    suspend fun errorHandlingExample() {
        try {
            // 故意使用无效的Token来演示错误处理
            GlobalToken.setToken("invalid_token")
            
            val userInfo = ApiService.getInstance().getUserInfo(12345L)
            Log.d(TAG, "用户信息: ${userInfo.name}")
            
        } catch (e: APIError) {
            when (e) {
                is APIError.AuthenticationError -> {
                    Log.e(TAG, "认证错误，需要重新登录: ${e.message}")
                    // 跳转到登录页面
                }
                is APIError.TokenExpired -> {
                    Log.e(TAG, "Token已过期，需要刷新Token: ${e.message}")
                    // 尝试刷新Token或重新登录
                }
                is APIError.NetworkError -> {
                    Log.e(TAG, "网络错误: ${e.originalError.message}")
                    // 显示网络错误提示
                }
                is APIError.ServerError -> {
                    Log.e(TAG, "服务器错误 [${e.statusCode}]: ${e.message}")
                    // 显示服务器错误提示
                }
                is APIError.InvalidResponse -> {
                    Log.e(TAG, "无效响应")
                    // 显示数据解析错误提示
                }
                else -> {
                    Log.e(TAG, "未知错误: ${e.message}")
                    // 显示通用错误提示
                }
            }
        }
    }
    
    /**
     * 完整的用户认证流程示例
     */
    suspend fun completeAuthFlowExample() {
        try {
            // 1. 健康检查
            val healthData = ApiService.getInstance().healthCheck()
            Log.d(TAG, "服务健康状态: ${healthData.code}")
            
            // 2. 用户注册
            val registerResponse = ApiService.getInstance().register(
                account = "newuser@example.com",
                password = "password123",
                name = "新用户"
            )
            
            if (registerResponse.code != 0) {
                Log.e(TAG, "注册失败，可能用户已存在")
                // 尝试登录现有用户
            }
            
            // 3. 用户登录
            val loginResponse = ApiService.getInstance().login(
                account = "newuser@example.com",
                password = "password123"
            )
            
            if (loginResponse.code == 0 && loginResponse.data != null) {
                Log.d(TAG, "登录成功，用户ID: ${loginResponse.data!!.userId}")
                
                // 4. 获取用户信息
                val userInfo = ApiService.getInstance().getUserInfo(loginResponse.data!!.userId)
                Log.d(TAG, "用户信息获取成功: ${userInfo.name}")
                
                // 5. 模拟Token刷新
                val refreshResult = ApiService.getInstance().refreshToken(GlobalToken.userToken!!)
                if (refreshResult.error == null) {
                    Log.d(TAG, "Token刷新成功")
                } else {
                    Log.e(TAG, "Token刷新失败: ${refreshResult.error!!.message}")
                }
                
                // 6. 用户登出
                val logoutResponse = ApiService.getInstance().logout()
                if (logoutResponse.code == 0) {
                    Log.d(TAG, "登出成功")
                    GlobalToken.clearToken()
                }
            }
            
        } catch (e: APIError) {
            Log.e(TAG, "完整认证流程失败: ${e.message}")
        }
    }
}
