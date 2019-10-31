package com.kotlin.user.presenter.view

import com.kotlin.base.presenter.view.BaseView

interface UserInfoView : BaseView {


    /*
        获取上传凭证回调
     */
    fun onGetUploadTokenResult(result: String)

}