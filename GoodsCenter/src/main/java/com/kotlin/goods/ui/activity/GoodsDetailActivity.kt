package com.kotlin.goods.ui.activity

import android.os.Bundle
import android.view.Gravity
import com.eightbitlab.rxbus.Bus
import com.eightbitlab.rxbus.registerInBus
import com.google.android.material.tabs.TabLayout
import com.kotlin.base.ext.onClick
import com.kotlin.base.ui.activity.BaseActivity
import com.kotlin.base.utils.AppPrefsUtils
import com.kotlin.goods.R
import com.kotlin.goods.common.GoodsConstant
import com.kotlin.goods.event.AddCartEvent
import com.kotlin.goods.event.UpdateCartSizeEvent
import com.kotlin.goods.ui.adapter.GoodsDetailVpAdapter
import com.kotlin.provider.common.afterLogin
import kotlinx.android.synthetic.main.activity_goods_detail.*
import q.rorbin.badgeview.QBadgeView

/*
    商品详情 Activity
 */
class GoodsDetailActivity:BaseActivity() {

    private lateinit var mCartBdage: QBadgeView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_goods_detail)
        initView()
        initObserve()
        loadCartSize()
    }

    /*
        初始化视图
     */
    private fun initView() {
        mGoodsDetailTab.tabMode = TabLayout.MODE_FIXED
        mGoodsDetailVp.adapter = GoodsDetailVpAdapter(supportFragmentManager,this)
        //TabLayout关联ViewPager
        mGoodsDetailTab.setupWithViewPager(mGoodsDetailVp)

        mAddCartBtn.onClick {
            afterLogin {
                Bus.send(AddCartEvent())
            }
        }

        mLeftIv.onClick {
            finish()
        }

        mCartBdage = QBadgeView(this)

    }

    /*
          加载购物车数量
       */
    private fun loadCartSize() {
        setCartBadge()
    }

    private fun initObserve() {
        Bus.observe<UpdateCartSizeEvent>()
            .subscribe {
                setCartBadge()
            }.registerInBus(this)
    }

    private fun setCartBadge() {
        mCartBdage.badgeGravity = Gravity.END or Gravity.TOP
        mCartBdage.setGravityOffset(22f, -2f, true)
        mCartBdage.setBadgeTextSize(6f, true)
        mCartBdage.bindTarget(mEnterCartTv).badgeNumber =
            AppPrefsUtils.getInt(GoodsConstant.SP_CART_SIZE)
    }

    override fun onDestroy() {
        super.onDestroy()
        Bus.unregister(this)
    }
}
