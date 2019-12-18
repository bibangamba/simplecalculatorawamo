package com.bibangamba.simplecalculatorawamo;

import android.app.Application;

import com.bibangamba.simplecalculatorawamo.dagger.component.AppComponent;
import com.bibangamba.simplecalculatorawamo.dagger.component.DaggerAppComponent;
import com.bibangamba.simplecalculatorawamo.dagger.module.AppModule;

import timber.log.Timber;

public class BaseApplication extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initTimberLogger();
        initAppComponent();
    }

    private void initAppComponent() {
        this.appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
        this.appComponent.inject(this);
    }

    private void initTimberLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
