package com.kotlin.user.presenter

import com.kotlin.base.presenter.BasePresenter
import com.kotlin.user.presenter.view.RegisterView

/**
 * @author wangzhen
 * @date 2019/10/17
 */
class RegisterPresenter : BasePresenter<RegisterView>() {

    fun register(mobile: String, verifyCode: String) {
        /**
         * 业务逻辑
         */
        mView.onRegisterResult(true)
    }
}