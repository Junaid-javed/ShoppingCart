package com.example.domain.network

import com.example.domain.model.AddressDomainModel
import com.example.domain.model.CartItemModel
import com.example.domain.model.CartModel
import com.example.domain.model.CartSummary
import com.example.domain.model.CategoryListModel
import com.example.domain.model.OrdersListModel
import com.example.domain.model.Product
import com.example.domain.model.ProductListModel
import com.example.domain.model.UserDomainModel
import com.example.domain.model.request.AddCartRequestModel
import java.lang.Exception

interface NetworkService {

    suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel>
    suspend fun getCategories(): ResultWrapper<CategoryListModel>
    suspend fun addProductToCart(request: AddCartRequestModel, userId: Long): ResultWrapper<CartModel>
    suspend fun getCart(userId: Long): ResultWrapper<CartModel>
    suspend fun updateQuantity(model: CartItemModel,userId: Long): ResultWrapper<CartModel>
    suspend fun deleteItem(cartItemId: Int, userId: Long): ResultWrapper<CartModel>
    suspend fun getCartSummary(userId: Long): ResultWrapper<CartSummary>
    suspend fun placeOrder(address: AddressDomainModel, userId: Long): ResultWrapper<Long>
    suspend fun getOrdersList(userId: Long): ResultWrapper<OrdersListModel>
    suspend fun login(username: String, password: String): ResultWrapper<UserDomainModel>
    suspend fun register(email: String, password: String, name: String): ResultWrapper<UserDomainModel>
}

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data class Failure(val exception: Exception) : ResultWrapper<Nothing>()
}