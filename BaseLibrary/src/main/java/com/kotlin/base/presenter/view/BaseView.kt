package com.kotlin.base.presenter.view

/**
 * @author wangzhen
 * @date 2019/10/17
 */
interface BaseView {
    fun showLoading()
    fun hideLoading()
    fun onError()
}