package com.kotlin.base.rx

import rx.Subscriber

/**
 * @author wangzhen
 * @date 2019/10/18
 */
open  class BaseSubscriber<T> : Subscriber<T>() {
    override fun onNext(t: T) {
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
    }
}