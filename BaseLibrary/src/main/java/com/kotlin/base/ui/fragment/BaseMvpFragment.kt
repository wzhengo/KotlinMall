package com.kotlin.base.ui.fragment

import android.os.Bundle
import com.kotlin.base.common.BaseApplication
import com.kotlin.base.injection.component.ActivityComponent
import com.kotlin.base.injection.component.DaggerActivityComponent
import com.kotlin.base.injection.module.ActivityModule
import com.kotlin.base.injection.module.LifecycleProviderModule
import com.kotlin.base.presenter.BasePresenter
import com.kotlin.base.presenter.view.BaseView
import org.jetbrains.anko.support.v4.act
import javax.inject.Inject

/**
 * @author wangzhen
 * @date 2019/10/17
 */
abstract class BaseMvpFragment<T : BasePresenter<*>> : BaseFragment(), BaseView {

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onError() {
    }

    lateinit var activityComponent: ActivityComponent

    @Inject
    lateinit var mPresenter: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActivityInjection()
        injectComponent()
    }

    abstract fun injectComponent()


    private fun initActivityInjection() {
        activityComponent = DaggerActivityComponent.builder()
            .appComponent((act.application as BaseApplication).appComponent)
            .activityModule(ActivityModule(act))
            .lifecycleProviderModule(LifecycleProviderModule(this))
            .build()
    }
}