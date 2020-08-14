package ua.ck.zabochen.android.paging3.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import ua.ck.zabochen.android.paging3.data.network.NetworkDataSource
import ua.ck.zabochen.android.paging3.data.network.NetworkDataSourceImpl
import ua.ck.zabochen.android.paging3.data.network.service.NetworkService
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideNetworkService(): NetworkService {
        return NetworkService.create()
    }

    @Singleton
    @Provides
    fun provideNetworkDataSource(networkService: NetworkService): NetworkDataSource {
        return NetworkDataSourceImpl(networkService)
    }
}
