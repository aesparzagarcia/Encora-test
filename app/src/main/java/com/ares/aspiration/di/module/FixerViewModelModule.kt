package com.ares.aspiration.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ares.aspiration.abstraction.util.viewmodel.ViewModelFactory
import com.ares.aspiration.abstraction.util.viewmodel.ViewModelKey
import com.ares.aspiration.di.FixerScope
import com.ares.aspiration.viewmodel.FixerViewModel
import com.ares.aspiration.viewmodel.LatestRatesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FixerViewModelModule {

    @FixerScope
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FixerViewModel::class)
    internal abstract fun bindSymbolsViewModel(viewModel: FixerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LatestRatesViewModel::class)
    internal abstract fun bindLatestRatesViewModel(viewModel: LatestRatesViewModel): ViewModel
}