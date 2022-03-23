package com.ares.aspiration.abstraction.base

import com.ares.aspiration.data.entity.Latest
import io.reactivex.Flowable

abstract class UseCase<T> {
    abstract fun getSymbols(): T
    abstract fun getLatest(base: String): Flowable<Latest>
}