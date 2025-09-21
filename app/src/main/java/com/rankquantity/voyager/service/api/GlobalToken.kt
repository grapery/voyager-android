package com.rankquantity.voyager.service.api

import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * 全局用户Token管理
 * 用于存储和获取用户的认证Token
 * 线程安全的实现
 */
object GlobalToken {
    /** 用户Token */
    @Volatile
    private var _userToken: String? = null
    
    /** 读写锁，确保线程安全 */
    private val lock = ReentrantReadWriteLock()
    
    /**
     * 获取用户Token（线程安全）
     */
    val userToken: String?
        get() = lock.read { _userToken }
    
    /**
     * 设置用户Token（线程安全）
     * @param token 用户Token
     */
    fun setToken(token: String) {
        lock.write {
            _userToken = token
        }
    }
    
    /**
     * 清除用户Token（线程安全）
     */
    fun clearToken() {
        lock.write {
            _userToken = null
        }
    }
    
    /**
     * 检查Token是否存在且有效（线程安全）
     * @return true如果Token存在且非空
     */
    fun hasValidToken(): Boolean {
        return lock.read { !_userToken.isNullOrEmpty() }
    }
}
