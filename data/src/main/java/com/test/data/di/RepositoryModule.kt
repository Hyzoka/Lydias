package com.test.data.di

import com.test.data.repo.ContactRepositoryImpl
import com.test.data.util.DefaultNetworkConnectivityHelper
import com.test.domain.NetworkConnectivityHelper
import com.test.domain.repo.ContactRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindContactRepository(
        impl: ContactRepositoryImpl
    ): ContactRepository

    @Binds
    @Singleton
    abstract fun bindNetworkConnectivityHelper(
        impl: DefaultNetworkConnectivityHelper
    ): NetworkConnectivityHelper

}
