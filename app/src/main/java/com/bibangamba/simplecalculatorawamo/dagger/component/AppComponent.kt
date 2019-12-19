package com.bibangamba.simplecalculatorawamo.dagger.component

import android.app.Application
import com.bibangamba.simplecalculatorawamo.dagger.module.AppModule
import com.bibangamba.simplecalculatorawamo.dagger.module.ViewModelModule
import com.bibangamba.simplecalculatorawamo.view.SimpleCalculatorActivity
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [AppModule::class, ViewModelModule::class]
)
@Singleton
interface AppComponent {
    fun inject(application: Application)
    fun inject(calculatorActivity: SimpleCalculatorActivity)
}