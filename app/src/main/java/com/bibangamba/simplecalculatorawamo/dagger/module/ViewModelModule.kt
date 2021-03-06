package com.bibangamba.simplecalculatorawamo.dagger.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bibangamba.simplecalculatorawamo.viewmodel.CalculationViewModel
import com.bibangamba.simplecalculatorawamo.viewmodel.CustomViewModelFactory
import com.bibangamba.simplecalculatorawamo.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindsViewModelFactory(factory: CustomViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CalculationViewModel::class)
    abstract fun bindsAuthViewModel(calculationViewModel: CalculationViewModel): ViewModel

}