package com.ares.aspiration.di

import com.ares.aspiration.ui.MainActivity
import com.ares.aspiration.di.module.FixerModule
import com.ares.aspiration.di.module.FixerViewModelModule
import com.ares.aspiration.ui.LatestRatesActivity
import dagger.Component

@FixerScope
@Component(
    modules = [
        FixerModule::class,
        FixerViewModelModule::class
    ]
)
interface MainComponent {
    fun injectMain(activity: MainActivity)
    fun injectLatest(activity: LatestRatesActivity)
}