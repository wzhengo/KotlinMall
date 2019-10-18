package com.kotlin.base.presenter

import com.kotlin.base.presenter.view.BaseView

/**
 * @author wangzhen
 * @date 2019/10/17
 */
open class BasePresenter<T : BaseView> {

    lateinit var mView: T
}