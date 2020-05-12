package com.bibangamba.simplecalculatorawamo.dagger.module

import com.bibangamba.simplecalculatorawamo.BuildConfig
import com.bibangamba.simplecalculatorawamo.data.service.MathService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class WebServiceModule(private val baseUrl: String) {

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            client.addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BASIC
                )
            )
        }

        return client.build()
    }

    @Provides
    @Singleton
    fun providesRetrofitInstance(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    fun providesMathService(retrofit: Retrofit): MathService {
        return retrofit.create(MathService::class.java)
    }

}