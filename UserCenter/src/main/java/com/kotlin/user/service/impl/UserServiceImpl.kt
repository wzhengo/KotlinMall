package com.kotlin.user.service.impl

import com.kotlin.base.rx.BaseFuncBoolean
import com.kotlin.user.data.repository.UserRepository
import com.kotlin.user.service.UserService
import rx.Observable
import javax.inject.Inject

/**
 * @author wangzhen
 * @date 2019/10/18
 */
class UserServiceImpl @Inject constructor() : UserService {

    @Inject
    lateinit var repository: UserRepository

    override fun register(mobile: String, verifyCode: String, pwd: String): Observable<Boolean> {
        return repository.register(mobile, verifyCode, pwd)
            .flatMap(BaseFuncBoolean())
    }
}