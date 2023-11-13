package com.example.cryptomarket.di

import com.example.cryptomarket.BuildConfig
import com.example.cryptomarket.data.network.api.CoinsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttp(): OkHttpClient {
         val httpClient = OkHttpClient.Builder()
        return if (BuildConfig.DEBUG) {
            httpClient.connectTimeout(1, TimeUnit.MINUTES).readTimeout(4, TimeUnit.MINUTES).writeTimeout(4, TimeUnit.MINUTES).addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()
        } else {
            OkHttpClient.Builder().build()
        }

    }
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, baseUrl: String): retrofit2.Retrofit {
        return retrofit2.Retrofit.Builder().baseUrl(baseUrl).client(okHttpClient).addConverterFactory(
            GsonConverterFactory.create()).build()
    }

    @Singleton
    @Provides
    fun provideCoinApis(retrofit: retrofit2.Retrofit): CoinsApi {
        return retrofit.create(CoinsApi::class.java)
    }
}