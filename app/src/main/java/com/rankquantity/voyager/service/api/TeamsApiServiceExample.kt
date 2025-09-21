package com.rankquantity.voyager.service.api

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * TeamsAPI 服务使用示例
 * 展示如何使用 TeamsAPI 进行各种操作
 */
class TeamsApiServiceExample {
    
    private val apiService = ApiService.getInstance()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    
    companion object {
        private const val TAG = "TeamsApiExample"
    }
    
    /**
     * 用户登录示例
     */
    fun loginExample() {
        coroutineScope.launch {
            try {
                Log.d(TAG, "开始用户登录示例")
                
                val loginData = apiService.login(
                    account = "test@example.com",
                    password = "password123",
                    loginType = 1
                )
                
                Log.d(TAG, "登录成功: ${loginData.userId}")
                
                // 保存Token到全局状态
                GlobalToken.setToken(loginData.token)
                
                // 获取用户信息
                val userInfo = apiService.getUserInfo(loginData.userId)
                Log.d(TAG, "用户信息: ${userInfo.info.name}")
                
            } catch (e: TeamsApiError) {
                Log.e(TAG, "登录失败: ${e.getUserFriendlyMessage()}")
                handleTeamsApiError(e)
            } catch (e: Exception) {
                Log.e(TAG, "登录异常: ${e.message}")
            }
        }
    }
    
    /**
     * 用户注册示例
     */
    fun registerExample() {
        coroutineScope.launch {
            try {
                Log.d(TAG, "开始用户注册示例")
                
                apiService.register(
                    account = "newuser@example.com",
                    password = "password123",
                    name = "新用户",
                    email = "newuser@example.com",
                    phone = "13800138000"
                )
                
                Log.d(TAG, "注册成功")
                
            } catch (e: TeamsApiError) {
                Log.e(TAG, "注册失败: ${e.getUserFriendlyMessage()}")
                handleTeamsApiError(e)
            } catch (e: Exception) {
                Log.e(TAG, "注册异常: ${e.message}")
            }
        }
    }
    
    /**
     * 创建组织示例
     */
    fun createGroupExample() {
        coroutineScope.launch {
            try {
                Log.d(TAG, "开始创建组织示例")
                
                val group = apiService.createGroup(
                    userId = "user123",
                    name = "我的组织",
                    description = "这是一个测试组织",
                    avatar = ""
                )
                
                Log.d(TAG, "创建组织成功: ${group.groupId}")
                
            } catch (e: TeamsApiError) {
                Log.e(TAG, "创建组织失败: ${e.getUserFriendlyMessage()}")
                handleTeamsApiError(e)
            } catch (e: Exception) {
                Log.e(TAG, "创建组织异常: ${e.message}")
            }
        }
    }
    
    /**
     * 创建故事示例
     */
    fun createStoryExample() {
        coroutineScope.launch {
            try {
                Log.d(TAG, "开始创建故事示例")
                
                val storyParams = StoryParams(
                    storyDescription = "一个关于冒险的故事",
                    refImage = "",
                    negativePrompt = "",
                    prompt = "创建一个冒险故事",
                    layoutStyle = "standard",
                    style = "fantasy",
                    background = "medieval",
                    styleRefImage = "",
                    subject = "adventure",
                    sceneCount = 5
                )
                
                val storyData = apiService.createStory(
                    name = "冒险故事",
                    title = "勇者的冒险",
                    shortDesc = "一个关于勇者冒险的故事",
                    creatorId = "user123",
                    ownerId = "user123",
                    groupId = "group123",
                    params = storyParams
                )
                
                Log.d(TAG, "创建故事成功: ${storyData.storyId}")
                
            } catch (e: TeamsApiError) {
                Log.e(TAG, "创建故事失败: ${e.getUserFriendlyMessage()}")
                handleTeamsApiError(e)
            } catch (e: Exception) {
                Log.e(TAG, "创建故事异常: ${e.message}")
            }
        }
    }
    
    /**
     * 获取故事信息示例
     */
    fun getStoryInfoExample() {
        coroutineScope.launch {
            try {
                Log.d(TAG, "开始获取故事信息示例")
                
                val storyInfo = apiService.getStoryInfo("story123")
                
                Log.d(TAG, "故事信息: ${storyInfo.info.name}")
                Log.d(TAG, "故事描述: ${storyInfo.info.desc}")
                Log.d(TAG, "创建者: ${storyInfo.creator.name}")
                
            } catch (e: TeamsApiError) {
                Log.e(TAG, "获取故事信息失败: ${e.getUserFriendlyMessage()}")
                handleTeamsApiError(e)
            } catch (e: Exception) {
                Log.e(TAG, "获取故事信息异常: ${e.message}")
            }
        }
    }
    
    /**
     * Token刷新示例
     */
    fun refreshTokenExample() {
        coroutineScope.launch {
            try {
                Log.d(TAG, "开始刷新Token示例")
                
                val currentToken = GlobalToken.getToken()
                if (currentToken != null) {
                    val refreshResponse = apiService.refreshToken(currentToken)
                    
                    Log.d(TAG, "Token刷新成功: ${refreshResponse.token}")
                    
                    // 更新全局Token
                    GlobalToken.setToken(refreshResponse.token)
                } else {
                    Log.w(TAG, "当前没有Token，无法刷新")
                }
                
            } catch (e: TeamsApiError) {
                Log.e(TAG, "刷新Token失败: ${e.getUserFriendlyMessage()}")
                handleTeamsApiError(e)
            } catch (e: Exception) {
                Log.e(TAG, "刷新Token异常: ${e.message}")
            }
        }
    }
    
    /**
     * 用户登出示例
     */
    fun logoutExample() {
        coroutineScope.launch {
            try {
                Log.d(TAG, "开始用户登出示例")
                
                val token = GlobalToken.getToken()
                if (token != null) {
                    apiService.logout(token, "user123")
                    
                    Log.d(TAG, "登出成功")
                    
                    // 清除全局Token
                    GlobalToken.clearToken()
                } else {
                    Log.w(TAG, "当前没有Token，无需登出")
                }
                
            } catch (e: TeamsApiError) {
                Log.e(TAG, "登出失败: ${e.getUserFriendlyMessage()}")
                handleTeamsApiError(e)
            } catch (e: Exception) {
                Log.e(TAG, "登出异常: ${e.message}")
            }
        }
    }
    
    /**
     * 处理TeamsAPI错误
     */
    private fun handleTeamsApiError(error: TeamsApiError) {
        when (error) {
            is TeamsApiError.AuthenticationError -> {
                if (error.isTokenExpired) {
                    Log.w(TAG, "Token已过期，需要重新登录")
                    // 可以在这里触发重新登录流程
                    GlobalToken.clearToken()
                }
            }
            is TeamsApiError.NetworkError -> {
                Log.w(TAG, "网络错误: ${error.getUserFriendlyMessage()}")
                // 可以在这里显示网络错误提示
            }
            is TeamsApiError.PermissionError -> {
                Log.w(TAG, "权限不足: ${error.getUserFriendlyMessage()}")
                // 可以在这里显示权限错误提示
            }
            is TeamsApiError.ValidationError -> {
                Log.w(TAG, "参数验证失败: ${error.getUserFriendlyMessage()}")
                // 可以在这里显示参数错误提示
            }
            is TeamsApiError.RateLimitError -> {
                Log.w(TAG, "请求过于频繁: ${error.getUserFriendlyMessage()}")
                // 可以在这里显示频率限制提示
            }
            is TeamsApiError.ServerError -> {
                Log.w(TAG, "服务器错误: ${error.getUserFriendlyMessage()}")
                // 可以在这里显示服务器错误提示
            }
            is TeamsApiError.BusinessError -> {
                Log.w(TAG, "业务错误: ${error.getUserFriendlyMessage()}")
                // 可以在这里显示业务错误提示
            }
            else -> {
                Log.w(TAG, "未知错误: ${error.getUserFriendlyMessage()}")
            }
        }
    }
    
    /**
     * 运行所有示例
     */
    fun runAllExamples() {
        Log.d(TAG, "开始运行所有TeamsAPI示例")
        
        // 按顺序运行示例
        loginExample()
        
        // 延迟一段时间后运行下一个示例
        coroutineScope.launch {
            kotlinx.coroutines.delay(2000)
            registerExample()
        }
        
        coroutineScope.launch {
            kotlinx.coroutines.delay(4000)
            createGroupExample()
        }
        
        coroutineScope.launch {
            kotlinx.coroutines.delay(6000)
            createStoryExample()
        }
        
        coroutineScope.launch {
            kotlinx.coroutines.delay(8000)
            getStoryInfoExample()
        }
        
        coroutineScope.launch {
            kotlinx.coroutines.delay(10000)
            refreshTokenExample()
        }
        
        coroutineScope.launch {
            kotlinx.coroutines.delay(12000)
            logoutExample()
        }
    }
}
