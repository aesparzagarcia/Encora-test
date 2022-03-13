package com.ares.aspiration.data.repository

import com.ares.aspiration.data.entity.Latest
import com.ares.aspiration.data.entity.Symbol
import com.google.gson.JsonObject
import io.reactivex.Flowable

interface FixerRepository {
    fun getSymbols(): Flowable<Symbol>
    fun getLatest(currency: String): Flowable<Latest>
}