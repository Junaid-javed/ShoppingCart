package com.example.domain.di

import com.example.domain.usecase.AddToCartUseCase
import com.example.domain.usecase.CartSummaryUseCase
import com.example.domain.usecase.DeleteQuantityUseCase
import com.example.domain.usecase.GetCartUseCase
import com.example.domain.usecase.GetCategoryUseCase
import com.example.domain.usecase.GetProductUseCase
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.OrderListUseCase
import com.example.domain.usecase.PlaceOrderUseCase
import com.example.domain.usecase.RegisterUseCase
import com.example.domain.usecase.UpdateQuantityUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetProductUseCase(get()) }
    factory { GetCategoryUseCase(get()) }
    factory { AddToCartUseCase(get()) }
    factory { GetCartUseCase(get()) }
    factory { UpdateQuantityUseCase(get()) }
    factory { DeleteQuantityUseCase(get()) }
    factory { CartSummaryUseCase(get()) }
    factory { PlaceOrderUseCase(get()) }
    factory { OrderListUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { RegisterUseCase(get()) }






}