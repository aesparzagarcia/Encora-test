package com.ares.aspiration.data.repository

import com.ares.aspiration.data.entity.Latest
import com.ares.aspiration.data.entity.Symbol
import com.ares.aspiration.network.ApiService
import com.google.gson.JsonObject
import io.reactivex.Flowable
import javax.inject.Inject

class FixerRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): FixerRepository {

    override fun getSymbols(): Flowable<Symbol> {
        return apiService.getSymbols()
    }

    override fun getLatest(base: String): Flowable<Latest> {
        return apiService.getLatest(base)
    }
}