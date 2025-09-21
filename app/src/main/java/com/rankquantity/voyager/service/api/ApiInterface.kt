package com.rankquantity.voyager.service.api

import retrofit2.http.*

/**
 * API接口定义
 * 使用 Retrofit 注解定义所有 API 端点
 */
interface ApiInterface {
    
    // MARK: - 认证相关接口
    
    /**
     * 用户登录
     */
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
    
    /**
     * 用户注册
     */
    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): RegisterResponse
    
    /**
     * 用户登出
     */
    @POST("/api/auth/logout")
    suspend fun logout(@Body request: LogoutRequest): LogoutResponse
    
    /**
     * 刷新Token
     */
    @POST("/api/auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): RefreshTokenResponse
    
    /**
     * 重置密码
     */
    @POST("/api/auth/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResetPasswordResponse
    
    // MARK: - 用户信息相关接口
    
    /**
     * 获取用户信息
     */
    @POST("/api/user/info")
    suspend fun getUserInfo(@Body request: UserInfoRequest): UserInfoResponse
    
    /**
     * 更新用户信息
     */
    @PUT("/api/user/info")
    suspend fun updateUserInfo(@Body request: UserInfo): ApiResponse<UserInfo>
    
    // MARK: - 系统接口
    
    /**
     * 健康检查
     */
    @GET("/api/health")
    suspend fun healthCheck(): ApiResponse<Map<String, String>>
}
