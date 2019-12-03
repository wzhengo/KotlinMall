package com.kotlin.order.injection.component

import com.kotlin.base.injection.PerComponentScope
import com.kotlin.base.injection.component.ActivityComponent
import com.kotlin.order.injection.module.ShipAddressModule
import com.kotlin.order.ui.activity.ShipAddressActivity
import com.kotlin.order.ui.activity.ShipAddressEditActivity
import dagger.Component

/**
 * @author wangzhen
 * @date 2019/11/27
 */
/*
    收货人信息Component
 */
@PerComponentScope
@Component(
    dependencies = [ActivityComponent::class],
    modules = [ShipAddressModule::class]
)
interface ShipAddressComponent {

    fun inject(activity: ShipAddressEditActivity)
    fun inject(activity: ShipAddressActivity)
}