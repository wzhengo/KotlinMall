package com.kotlin.user.service

import rx.Observable

/**
 * @author wangzhen
 * @date 2019/10/18
 */
interface UserService {

    fun register(mobile: String, verifyCode: String, pwd: String): Observable<Boolean>
}