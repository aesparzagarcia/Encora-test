package com.ares.aspiration.data.domain

import com.ares.aspiration.abstraction.base.UseCase
import com.ares.aspiration.data.entity.Latest
import com.ares.aspiration.data.entity.Symbol
import com.ares.aspiration.data.repository.FixerRepository
import io.reactivex.Flowable
import javax.inject.Inject

open class FixerUseCase @Inject constructor(
    private val repository: FixerRepository
): UseCase<Flowable<Symbol>>() {

    override fun getSymbols(): Flowable<Symbol> {
        return repository.getSymbols()
    }

    override fun getLatest(currency: String): Flowable<Latest> {
        return repository.getLatest(currency)
    }
}