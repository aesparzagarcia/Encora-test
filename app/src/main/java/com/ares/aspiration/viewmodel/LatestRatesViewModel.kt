package com.ares.aspiration.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ares.aspiration.abstraction.base.BaseViewModel
import com.ares.aspiration.abstraction.state.LoaderState
import com.ares.aspiration.abstraction.util.Constants.TYPE
import com.ares.aspiration.abstraction.util.rx.SchedulerProvider
import com.ares.aspiration.data.domain.FixerUseCase
import com.ares.aspiration.data.entity.Latest
import javax.inject.Inject

class LatestRatesViewModel @Inject constructor(
    private val useCase: FixerUseCase,
    private val schedulerProvider: SchedulerProvider
) : BaseViewModel(), LatestRates {

    private val _latest = MutableLiveData<Latest>()
    val latest: LiveData<Latest>
        get() = _latest

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val _state = MutableLiveData<LoaderState>()
    val state: LiveData<LoaderState>
        get() = _state

    override fun loadLatest(currency: String) {
        subscribe(useCase.getLatest(currency)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .doOnSubscribe { showLoading() }
            .doOnError { onErrorLatest(it) }
            .subscribe {
                hideLoading()
                if (it.success) {
                    _latest.postValue(it)
                } else {
                    _error.postValue(it.error.get(TYPE).toString())
                }
            }
        )
    }

    override fun onErrorLatest(throwable: Throwable) {
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

interface LatestRates {
    fun loadLatest(currency: String)
    fun onErrorLatest(throwable: Throwable)
    fun onSuccessError(message: String)
}