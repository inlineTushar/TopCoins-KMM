package com.tushar.topcoins

import android.app.Application
import com.tushar.topcoins.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TopCoinsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@TopCoinsApplication)
            modules(appModule)
        }
    }
}