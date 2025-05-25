package com.example.shoppingcart.navigation

import com.example.shoppingcart.model.UiProductModel
import kotlinx.serialization.Serializable

@Serializable
object LoginScreen

@Serializable
object RegisterScreen

@Serializable
object HomeScreen

@Serializable
object CartScreen

@Serializable
object OrdersScreen

@Serializable
object ProfileScreen

@Serializable
data class ProductDetails(
    val product: UiProductModel)

@Serializable
object CartSummaryScreen

@Serializable
data class UserAddressRoute(val userAddressRouteWrapper: UserAddressRouteWrapper)