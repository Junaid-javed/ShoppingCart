package com.example.shoppingcart.di

import com.example.shoppingcart.ui.feature.account.login.LoginViewModel
import com.example.shoppingcart.ui.feature.account.register.RegisterViewModel
import com.example.shoppingcart.ui.feature.cart.CartViewModel
import com.example.shoppingcart.ui.feature.home.HomeViewModel
import com.example.shoppingcart.ui.feature.orders.OrdersViewModel
import com.example.shoppingcart.ui.feature.product_details.ProductDetailViewModel
import com.example.shoppingcart.ui.feature.summary.CartSummaryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        HomeViewModel(get(), get())
    }
    viewModel {
        ProductDetailViewModel(get(), get())
    }
    viewModel {
        CartViewModel(get(), get(), get(), get())
    }

    viewModel {
        CartSummaryViewModel(get(), get(), get())
    }

    viewModel {
        OrdersViewModel(get(),get())
    }
    viewModel {
        LoginViewModel(get(), get())
    }
    viewModel {
        RegisterViewModel(get(), get())

    }
}