package com.example.shoppingcart.ui.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CartItemModel
import com.example.domain.network.ResultWrapper
import com.example.domain.usecase.DeleteQuantityUseCase
import com.example.domain.usecase.GetCartUseCase
import com.example.domain.usecase.UpdateQuantityUseCase
import com.example.shoppingcart.ShopperSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    val cartUseCase: GetCartUseCase,
    val updateQuantityUseCase: UpdateQuantityUseCase,
    val deleteQuantityUseCase: DeleteQuantityUseCase,
    val shopperSession: ShopperSession
) : ViewModel() {

    private val uiState = MutableStateFlow<CartEvent>(CartEvent.Loading)
    val state = uiState.asStateFlow()
    val userDomainModel = shopperSession.getUser()

    init {
        getCart()
    }

    fun getCart() {
        viewModelScope.launch {
            uiState.value = CartEvent.Loading
            val result = cartUseCase.execute(userDomainModel!!.id!!.toLong())
            when (result) {
                is ResultWrapper.Success -> {
                    uiState.value = CartEvent.Success(result.value.data)
                }

                is ResultWrapper.Failure -> {
                    uiState.value = CartEvent.Error("Something went wrong")
                }
            }
        }
    }

    fun incrementQuantity(cartItem: CartItemModel) {
        if (cartItem.quantity == 10) return
        val updatedItem = cartItem.copy(quantity = cartItem.quantity + 1)
        updateQuantity(updatedItem)
    }

    private fun updateQuantity(cartItem: CartItemModel) {
        viewModelScope.launch {
            uiState.value = CartEvent.Loading
            val result = updateQuantityUseCase.execute(cartItem, userDomainModel!!.id!!.toLong())
            when (result) {
                is ResultWrapper.Success -> {
                    uiState.value = CartEvent.Success(result.value.data)
                }

                is ResultWrapper.Failure -> {
                    uiState.value = CartEvent.Error("Something went wrong")
                }
            }
        }
    }

    fun decrementQuantity(cartItem: CartItemModel) {
        if (cartItem.quantity == 1) return
        val updatedItem = cartItem.copy(quantity = cartItem.quantity - 1)
        updateQuantity(updatedItem)
    }

    fun removeFromCart(cartItem: CartItemModel) {
        viewModelScope.launch {
            uiState.value = CartEvent.Loading
            val result = deleteQuantityUseCase.execute(cartItem.id, 1)
            when (result) {
                is ResultWrapper.Success -> {
                    uiState.value = CartEvent.Success(result.value.data)
                }

                is ResultWrapper.Failure -> {
                    uiState.value = CartEvent.Error("Something went wrong")
                }
            }
        }
    }

}

sealed class CartEvent {
    data object Loading : CartEvent()
    data class Success(val message: List<CartItemModel>) : CartEvent()
    data class Error(val message: String) : CartEvent()
}