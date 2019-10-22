package com.kotlin.user.presenter

import android.util.Log
import com.kotlin.base.ext.execute
import com.kotlin.base.presenter.BasePresenter
import com.kotlin.base.rx.BaseSubscriber
import com.kotlin.user.presenter.view.RegisterView
import com.kotlin.user.service.UserService
import javax.inject.Inject

/**
 * @author wangzhen
 * @date 2019/10/17
 */
class RegisterPresenter @Inject constructor() : BasePresenter<RegisterView>() {

    @Inject
    lateinit var userService: UserService

    fun register(mobile: String, verifyCode: String, pwd: String) {
        /**
         * 业务逻辑
         */
        if (!checkNetWork()) {
            Log.e("RegisterPresenter", "网络不可用")
            return
        }
        mView.showLoading()

        userService.register(mobile, verifyCode, pwd)
            .execute(object : BaseSubscriber<Boolean>(mView) {
                override fun onNext(t: Boolean) {
                    if (t) {
                        mView.onRegisterResult("注册成功")
                    }
                }
            }, lifecycleProvider)

    }
}