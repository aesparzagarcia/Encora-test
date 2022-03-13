package com.ares.aspiration.di.module

import com.ares.aspiration.abstraction.util.rx.AppSchedulerProvider
import com.ares.aspiration.abstraction.util.rx.SchedulerProvider
import com.ares.aspiration.data.domain.FixerUseCase
import com.ares.aspiration.data.repository.FixerRepository
import com.ares.aspiration.data.repository.FixerRepositoryImpl
import com.ares.aspiration.di.FixerScope
import com.ares.aspiration.network.ApiService
import com.ares.aspiration.network.Network
import dagger.Module
import dagger.Provides
import retrofit2.create

@Module
class FixerModule {

    @Provides
    @FixerScope
    fun provideNetworkBuilder(): ApiService {
        return Network.builder().create()
    }

    @Provides
    @FixerScope
    fun providesFixerRepository(
        service: ApiService
    ): FixerRepository {
        return FixerRepositoryImpl(service)
    }

    @Provides
    @FixerScope
    fun providesFixerUseCase(
        repository: FixerRepository
    ): FixerUseCase {
        return FixerUseCase(repository)
    }

    @Provides
    @FixerScope
    fun provideSchedulerProvider(): SchedulerProvider {
        return AppSchedulerProvider()
    }
}