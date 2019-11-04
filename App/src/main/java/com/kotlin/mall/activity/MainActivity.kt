package com.kotlin.mall.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wz.kotlinmall.R
import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mBottomNavBar.checkCartBadge(20)
        mBottomNavBar.checkMsgBadge(false)

        Observable.timer(2,TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { mBottomNavBar.checkMsgBadge(true) }
    }
}
