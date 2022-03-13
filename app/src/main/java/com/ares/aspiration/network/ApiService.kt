package com.ares.aspiration.network

import com.ares.aspiration.abstraction.util.Constants.LATEST_END_POINT
import com.ares.aspiration.abstraction.util.Constants.SYMBOLS_END_POINT
import com.ares.aspiration.data.entity.Latest
import com.ares.aspiration.data.entity.Symbol
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(SYMBOLS_END_POINT)
    fun getSymbols(): Flowable<Symbol>

    @GET(LATEST_END_POINT)
    fun getLatest(@Query("symbol") symbols: String,
                  @Query("base") base: String = "EUR"): Flowable<Latest>
}