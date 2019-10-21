package com.kotlin.user.presenter.view

import com.kotlin.base.presenter.view.BaseView

/**
 * @author wangzhen
 * @date 2019/10/17
 */
interface RegisterView : BaseView {

    fun onRegisterResult(result: String)
}