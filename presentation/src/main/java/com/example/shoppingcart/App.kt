package com.example.shoppingcart

import android.app.Application
import com.example.data.di.dataModule
import com.example.domain.di.domainModule
import com.example.shoppingcart.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(presentationModule, domainModule, dataModule))
        }
    }
}