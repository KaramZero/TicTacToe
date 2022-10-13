package com.example.tictactoe

import android.app.Application
import com.example.tictactoe.di.adapterModule
import com.example.tictactoe.di.appModule
import com.example.tictactoe.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@BaseApplication)
            // Load modules
            modules(appModule, viewModelModule, adapterModule)
        }
    }
}