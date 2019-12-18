package com.bibangamba.simplecalculatorawamo.dagger.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [WebServiceModule::class, DatabaseModule::class])
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun providesAppContext(): Context {
        return application
    }
}