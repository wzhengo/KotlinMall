package com.kotlin.goods.injection.component

import com.kotlin.base.injection.PerComponentScope
import com.kotlin.base.injection.component.ActivityComponent
import com.kotlin.goods.injection.module.GoodsModule
import com.kotlin.goods.ui.activity.GoodsActivity
import dagger.Component

/*
    商品Component
 */
@PerComponentScope
@Component(
    dependencies = [ActivityComponent::class],
    modules = [GoodsModule::class/*, CartModule::class*/]
)
interface GoodsComponent {
    fun inject(activity: GoodsActivity)
//    fun inject(fragment: GoodsDetailTabOneFragment)
}
