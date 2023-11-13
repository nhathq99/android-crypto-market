package com.example.cryptomarket.di

import android.content.Context
import com.example.cryptomarket.repositories.AppRepository
import com.example.cryptomarket.repositories.CoinsRepository
import com.example.cryptomarket.repositories.LocalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    fun provideContext(
        @ApplicationContext context: Context,
    ): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideAppRepository(
        coinsRepository: CoinsRepository,
        localRepository: LocalRepository
    ): AppRepository {
        return AppRepository(coinsRepository, localRepository)
    }
}