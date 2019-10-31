package com.kotlin.user.presenter

import com.kotlin.base.presenter.BasePresenter
import com.kotlin.user.presenter.view.UserInfoView
import com.kotlin.user.service.UserService
import javax.inject.Inject

/*
    用户信息Presenter
 */
class UserInfoPresenter @Inject constructor() : BasePresenter<UserInfoView>() {

    @Inject
    lateinit var userService: UserService


}
