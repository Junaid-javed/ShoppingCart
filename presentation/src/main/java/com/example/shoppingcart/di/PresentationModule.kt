package com.example.shoppingcart.di

import com.example.shoppingcart.ShopperSession
import org.koin.dsl.module

val presentationModule = module {
    includes(viewModelModule)
    single { ShopperSession(get()) }
}