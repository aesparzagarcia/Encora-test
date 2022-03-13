package com.ares.aspiration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ares.aspiration.abstraction.base.BaseViewModel
import com.ares.aspiration.abstraction.state.LoaderState
import com.ares.aspiration.abstraction.util.Constants.INFO
import com.ares.aspiration.abstraction.util.rx.SchedulerProvider
import com.ares.aspiration.data.domain.FixerUseCase
import com.ares.aspiration.data.entity.Symbol
import javax.inject.Inject

class FixerViewModel @Inject constructor(
    private val useCase: FixerUseCase,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(), SymbolsContract {

    private val _symbols = MutableLiveData<Symbol>()
    val symbols: LiveData<Symbol>
        get() = _symbols

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _state = MutableLiveData<LoaderState>()
    val state: LiveData<LoaderState>
        get() = _state

    override fun loadSymbols() {
        subscribe(useCase.getSymbols()
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { showLoading() }
            .doOnError { onErrorSymbols(it) }
            .subscribe {
                hideLoading()
                if (it.success.not()) onSuccessError(it.error.get(INFO).toString()) else _symbols.postValue(it)
            }
        )
    }

    override fun onErrorSymbols(throwable: Throwable) {
        _error.postValue(throwable.message)
    }

    override fun onSuccessError(message: String) {
        _error.postValue(message)
    }

    private fun showLoading() {
        _state.postValue(LoaderState.ShowLoading)
    }

    private fun hideLoading() {
        _state.postValue(LoaderState.HideLoading)
    }
}

interface SymbolsContract {
    fun loadSymbols()
    fun onErrorSymbols(throwable: Throwable)
    fun onSuccessError(message: String)
}